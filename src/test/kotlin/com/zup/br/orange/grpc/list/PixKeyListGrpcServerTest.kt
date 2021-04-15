package com.zup.br.orange.grpc.list

import com.zup.br.orange.AccountType
import com.zup.br.orange.PixKeyListGrpcRequest
import com.zup.br.orange.PixKeyListServiceGrpc
import com.zup.br.orange.entity.pix.Pix
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.repository.PixRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

@MicronautTest(transactional = false)
class PixKeyListGrpcServerTest(
    private val pixRepository: PixRepository,
    private val grpcClients: PixKeyListServiceGrpc.PixKeyListServiceBlockingStub
) {

    @AfterEach
    fun close(){
        pixRepository.deleteAll()
    }

    @Test
    fun testListPixs() {
        val clientId = "bd5b4460-bfcb-495b-a250-298a53157f03"
        val pixList = listOf<Pix>(
            Pix(
                clientId,
                AccountType.CONTA_CORRENTE,
                PixType.RANDOM,
                UUID.randomUUID().toString(),
                "60701190",
                LocalDateTime.now()
            ),
            Pix(
                clientId,
                AccountType.CONTA_CORRENTE,
                PixType.RANDOM,
                UUID.randomUUID().toString(),
                "60701190",
                LocalDateTime.now()
            )
        )

        pixRepository.saveAll(pixList)

        val response = grpcClients.list(PixKeyListGrpcRequest.newBuilder().setClientId(clientId).build())

        Assert.assertEquals(2, response.pixListCount)
    }

    @Test
    fun testListPixsEmpty() {
        val clientId = "bd5b4460-bfcb-495b-a250-298a53157f03"

        val response = grpcClients.list(PixKeyListGrpcRequest.newBuilder().setClientId(clientId).build())

        Assert.assertEquals(0, response.pixListCount)
    }

}