package com.zup.br.orange.grpc.pix.register.service

import com.zup.br.orange.client.bcb.BCBClient
import com.zup.br.orange.client.bcb.request.AccountTypeBCBClientRequest
import com.zup.br.orange.client.bcb.request.CreatePixClientRequest
import com.zup.br.orange.client.bcb.response.CreatePixClientResponse
import com.zup.br.orange.client.itau.ItauClient
import com.zup.br.orange.entity.pix.Pix
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest
import com.zup.br.orange.exceptions.DuplicatePixKeyException
import com.zup.br.orange.exceptions.InternalErrorException
import com.zup.br.orange.exceptions.NotFoundException
import com.zup.br.orange.repository.PixRepository
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class RegisterKeyGrpcService(
    var pixRepository: PixRepository,
    var itauClient: ItauClient,
    var bcbClient: BCBClient
) {

    @Transactional
    fun registerNewPixKey(@Valid registerPixKeyRequest: RegisterPixKeyRequest) : Pix {
        if (pixRepository.existsByPixValue(registerPixKeyRequest.keyValue))
            throw DuplicatePixKeyException("Pix key - ${registerPixKeyRequest.keyValue} is already register")

        val accountResponse = itauClient.consultAccount(registerPixKeyRequest.clientId, registerPixKeyRequest.accountType!!.name)
            ?: throw NotFoundException(
                "Account with id: ${registerPixKeyRequest.clientId} and type: ${registerPixKeyRequest.keyType} not found"
            )

        val bcbResponse = bcbClient.createPix(CreatePixClientRequest(registerPixKeyRequest, accountResponse))
            ?: throw InternalErrorException("Error while trying to create pix key")

        return pixRepository.save(registerPixKeyRequest.toModel(
            bcbResponse.createdAt,
            bcbResponse.key,
            accountResponse.instituicao.ispb
        ));
    }

}