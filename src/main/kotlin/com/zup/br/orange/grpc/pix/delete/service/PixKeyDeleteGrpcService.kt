package com.zup.br.orange.grpc.pix.delete.service

import com.zup.br.orange.PixKeyDeleteGrpcResponse
import com.zup.br.orange.client.bcb.BCBClient
import com.zup.br.orange.client.bcb.request.DeletePixClientRequest
import com.zup.br.orange.client.itau.ItauClient
import com.zup.br.orange.entity.pix.request.DeletePixKeyRequest
import com.zup.br.orange.exceptions.NotFoundException
import com.zup.br.orange.repository.PixRepository
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class PixKeyDeleteGrpcService (
    val pixRepository: PixRepository,
    var bcbClient: BCBClient
){
    @Transactional
    fun deletePixKey(@Valid deletePixKeyRequest: DeletePixKeyRequest): PixKeyDeleteGrpcResponse {
        val pixOptional = pixRepository.findByIdAndClientId(deletePixKeyRequest.pixId, deletePixKeyRequest.clientId)

        if (pixOptional.isEmpty)
            throw NotFoundException(
                "Pix with id: ${deletePixKeyRequest.pixId} and clientId: ${deletePixKeyRequest.clientId} not found"
            )
        val pix = pixOptional.get()

        val bcbResponse = bcbClient.deletePix(pix.pixValue, DeletePixClientRequest(pix.pixValue, pix.ispb))
            ?: throw InternalError("An unexpected error occurred while trying to delete key ${pix.id}")

        pixRepository.delete(pix)

        return PixKeyDeleteGrpcResponse.newBuilder()
            .setClientId(deletePixKeyRequest.clientId)
            .setIspb(bcbResponse.participant)
            .setDeletedAt(bcbResponse.deletedAt.toString())
            .build()
    }

}