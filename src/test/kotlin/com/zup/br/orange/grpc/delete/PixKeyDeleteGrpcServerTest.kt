package com.zup.br.orange.grpc.delete

import com.zup.br.orange.*
import com.zup.br.orange.client.bcb.BCBClient
import com.zup.br.orange.client.bcb.response.DeletePixClientResponse
import com.zup.br.orange.entity.pix.Pix
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.repository.PixRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import utils.MockitoHelper
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
class PixKeyDeleteGrpcServerTest(
    val grpcClients: PixKeyDeleteServiceGrpc.PixKeyDeleteServiceBlockingStub,
    val pixRepository: PixRepository
) {

    @Inject
    lateinit var bcbClient: BCBClient

    @AfterEach
    fun end(){
        pixRepository.deleteAll()
    }

    @Test
    fun testDeletePix() {
        val pix = Pix(
            "bd5b4460-bfcb-495b-a250-298a53157f03",
            AccountType.CONTA_CORRENTE,
            PixType.RANDOM,
            UUID.randomUUID().toString(),
            "60701190",
            LocalDateTime.now()
        )
        pixRepository.save(pix)

        val grpcRequest = PixKeyDeleteGrpcRequest.newBuilder()
            .setClientId(pix.clientId)
            .setPixId(pix.id.toString())
            .build()

        val bcbCLientResponse = DeletePixClientResponse(pix.pixValue, pix.ispb, LocalDateTime.now())

        Mockito.`when`(bcbClient.deletePix(MockitoHelper.anyObject(), MockitoHelper.anyObject())).thenReturn(bcbCLientResponse)

        val response = grpcClients.delete(grpcRequest)

        Assert.assertEquals(pix.clientId, response.clientId)
        Assert.assertTrue(pixRepository.findByIdAndClientId(pix.id, pix.clientId).isEmpty)
    }

    @Test
    fun testDeletePixNotFound() {
        val pix = Pix(
            "bd5b4460-bfcb-495b-a250-298a53157f03",
            AccountType.CONTA_CORRENTE,
            PixType.RANDOM,
            UUID.randomUUID().toString(),
            "60701190",
            LocalDateTime.now()
        )

        val grpcRequest = PixKeyDeleteGrpcRequest.newBuilder()
            .setClientId(pix.clientId)
            .setPixId("1")
            .build()

        val exception = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java) {
            grpcClients.delete(grpcRequest)
        }

        Assert.assertEquals(
            "NOT_FOUND: Pix with id: 1 and clientId: bd5b4460-bfcb-495b-a250-298a53157f03 not found",
            exception.message
        )
    }

    @Test
    fun testDeletePixInternal() {
        val pix = Pix(
            "bd5b4460-bfcb-495b-a250-298a53157f03",
            AccountType.CONTA_CORRENTE,
            PixType.RANDOM,
            UUID.randomUUID().toString(),
            "60701190",
            LocalDateTime.now()
        )

        pixRepository.save(pix)

        val grpcRequest = PixKeyDeleteGrpcRequest.newBuilder()
            .setClientId(pix.clientId)
            .setPixId(pix.id.toString())
            .build()

        val exception = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java) {
            grpcClients.delete(grpcRequest)
        }

        Assert.assertEquals(
            "INTERNAL: An unexpected error occurred while trying to delete key 2",
            exception.message
        )
    }
    @MockBean(BCBClient::class)
    fun bcbClient(): BCBClient? {
        return Mockito.mock(BCBClient::class.java)
    }
}