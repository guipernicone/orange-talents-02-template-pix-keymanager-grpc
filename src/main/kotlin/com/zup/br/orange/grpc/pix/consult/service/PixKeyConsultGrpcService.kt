package com.zup.br.orange.grpc.pix.consult.service

import com.zup.br.orange.AccountType
import com.zup.br.orange.PixKeyConsultGrpcResponse
import com.zup.br.orange.client.bcb.BCBClient
import com.zup.br.orange.client.bcb.response.ConsultPixClientResponse
import com.zup.br.orange.client.itau.ItauClient
import com.zup.br.orange.client.itau.response.ConsultAccountResponse
import com.zup.br.orange.entity.pix.Pix
import com.zup.br.orange.entity.pix.request.ConsultPixIdRequest
import com.zup.br.orange.entity.pix.request.ConsultPixRequest
import com.zup.br.orange.exceptions.InternalErrorException
import com.zup.br.orange.exceptions.NotFoundException
import com.zup.br.orange.grpc.pix.consult.utils.Institutions
import com.zup.br.orange.repository.PixRepository
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class PixKeyConsultGrpcService(
    var pixRepository: PixRepository,
    var itauClient: ItauClient,
    var bcbClient: BCBClient
) {
    @Transactional
    fun consultPixId(@Valid consultPixIdRequest: ConsultPixIdRequest) : PixKeyConsultGrpcResponse {
        val optionalPix =
            pixRepository.findByIdAndClientId(consultPixIdRequest.pixId.toLong(), consultPixIdRequest.clientId)

        if (optionalPix.isEmpty) {
            throw NotFoundException(
                "Pix with id: ${consultPixIdRequest.pixId} and clientId: ${consultPixIdRequest.clientId} not found"
            )
        }
        val pix = optionalPix.get()

        val accountResponse = itauClient.consultAccount(pix.clientId, pix.accountType.name)
            ?: throw InternalErrorException("An unexpected error occurred")

        return buildResponse(pix, accountResponse)

    }
    @Transactional
    fun consultPixValue(@Valid consultPixRequest: ConsultPixRequest) : PixKeyConsultGrpcResponse {
        val optionalPix = pixRepository.findByPixValue(consultPixRequest.pixValue)
        if (optionalPix.isPresent){
            val pix = optionalPix.get()

            val accountResponse = itauClient.consultAccount(pix.clientId, pix.accountType.name)
                ?: throw InternalErrorException("An unexpected error occurred")

            return buildResponse(pix, accountResponse)
        }

        val bcbResponse = bcbClient.consultPix(consultPixRequest.pixValue) ?:
            throw NotFoundException("Pix with value ${consultPixRequest.pixValue}, not found")

        return buildResponse(bcbResponse)
    }

    private fun buildResponse(pix: Pix, accountResponse: ConsultAccountResponse): PixKeyConsultGrpcResponse{
        val accountBuilder = PixKeyConsultGrpcResponse.newBuilder().pixBuilder.accountBuilder
            .setType(pix.accountType)
            .setInstitution(accountResponse.instituicao.nome)
            .setIspb(pix.ispb)
            .setTitular(accountResponse.titular.nome)
            .setCpf(accountResponse.titular.cpf)
            .setAgency(accountResponse.agencia)
            .setAccountNumber(accountResponse.numero)
            .build()

        val pixBuilder = PixKeyConsultGrpcResponse.newBuilder().pixBuilder
            .setType(pix.pixType.name)
            .setKey(pix.pixValue)
            .setCreatedAt(pix.createAt.toString())
            .setAccount(accountBuilder)
            .build()

        return PixKeyConsultGrpcResponse.newBuilder()
            .setClientId(pix.clientId)
            .setPixId(pix.id.toString())
            .setPix(pixBuilder)
            .build()
    }

    private fun buildResponse(consultResponse: ConsultPixClientResponse): PixKeyConsultGrpcResponse{
        val accountBuilder = PixKeyConsultGrpcResponse.newBuilder().pixBuilder.accountBuilder
            .setType(when(consultResponse.bankAccount.accountType){
                "CACC" -> AccountType.CONTA_CORRENTE
                "SVGS" -> AccountType.CONTA_POUPANCA
                else -> AccountType.UNKNOWN_ACCOUNT
            })
            .setInstitution(Institutions.name(consultResponse.bankAccount.participant))
            .setIspb(consultResponse.bankAccount.participant)
            .setTitular(consultResponse.owner.name)
            .setCpf(consultResponse.owner.taxIdNumber)
            .setAgency(consultResponse.bankAccount.branch)
            .setAccountNumber(consultResponse.bankAccount.accountNumber)
            .build()

        val pixBuilder = PixKeyConsultGrpcResponse.newBuilder().pixBuilder
            .setType(consultResponse.keyType)
            .setKey(consultResponse.key)
            .setCreatedAt(consultResponse.createdAt.toString())
            .setAccount(accountBuilder)
            .build()

        return PixKeyConsultGrpcResponse.newBuilder()
            .setPix(pixBuilder)
            .build()
    }

}