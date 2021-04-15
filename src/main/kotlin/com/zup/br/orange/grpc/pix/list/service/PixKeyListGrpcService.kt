package com.zup.br.orange.grpc.pix.list.service

import com.zup.br.orange.KeyType
import com.zup.br.orange.PixKeyListGrpcResponse
import com.zup.br.orange.entity.pix.request.ListPixKeyRequest
import com.zup.br.orange.exceptions.handler.ErrorHandler
import com.zup.br.orange.repository.PixRepository
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class PixKeyListGrpcService(
    private val pixRepository: PixRepository
) {

    @Transactional
    fun listPixs(@Valid listPixKeyRequest: ListPixKeyRequest): PixKeyListGrpcResponse {
        val pixList = pixRepository.findByClientId(listPixKeyRequest.clientId).map {
            PixKeyListGrpcResponse.Pix.newBuilder()
                .setPixId(it.id.toString())
                .setPixType(KeyType.valueOf(it.pixType.name))
                .setPixValue(it.pixValue)
                .setAccountType(it.accountType)
                .setCreatedAt(it.createAt.toString())
                .build()
        }

       return PixKeyListGrpcResponse.newBuilder()
            .setClientId(listPixKeyRequest.clientId)
            .addAllPixList(pixList)
            .build()
    }


}