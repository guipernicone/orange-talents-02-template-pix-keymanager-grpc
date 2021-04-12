package com.zup.br.orange.grpc

import com.zup.br.orange.AccountType
import com.zup.br.orange.KeyType
import com.zup.br.orange.PixKeyRegisterGrpcRequest
import com.zup.br.orange.PixKeyRegisterServiceGrpc
import com.zup.br.orange.client.bcb.BCBClient
import com.zup.br.orange.client.bcb.request.AccountTypeBCBClientRequest
import com.zup.br.orange.client.bcb.request.CreatePixBankAccountRequest
import com.zup.br.orange.client.bcb.request.CreatePixClientRequest
import com.zup.br.orange.client.bcb.request.CreatePixOwnerRequest
import com.zup.br.orange.client.bcb.response.CreatePixClientResponse
import com.zup.br.orange.client.itau.ItauClient
import com.zup.br.orange.client.itau.response.ConsultAccountInstituicaoResponse
import com.zup.br.orange.client.itau.response.ConsultAccountResponse
import com.zup.br.orange.client.itau.response.ConsultAccountTitularResponse
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.grpc.pix.utils.toModel
import io.micronaut.context.annotation.Bean
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest
class PixKeyGrpcServerTest(
    @Inject val grpcClients: PixKeyRegisterServiceGrpc.PixKeyRegisterServiceBlockingStub
) {

    @Inject
    lateinit var itauClient: ItauClient

    @Inject
    lateinit var bcbClient: BCBClient

    @Test
    fun testRegisterPix(){
        val grpcRequest = PixKeyRegisterGrpcRequest.newBuilder()
            .setClientId("bd5b4460-bfcb-495b-a250-298a53157f03")
            .setKeyType(KeyType.EMAIL)
            .setKeyValue("test@email.com")
            .setAccount(AccountType.CONTA_CORRENTE)
            .build()

        val consultAccountTitularResponse = ConsultAccountTitularResponse(
            "bd5b4460-bfcb-495b-a250-298a53157f03",
            "User Test",
            "95026867057"
        )
        val consultAccountInstituicaoResponse = ConsultAccountInstituicaoResponse(
            "ITAÚ UNIBANCO S.A.",
            "60701190"
        )
        val consutlAccountResponse = ConsultAccountResponse(
            "CONTA_CORRENTE",
            consultAccountInstituicaoResponse,
            "0001",
            "291900",
            consultAccountTitularResponse
        )

        val createPixClientResponse = CreatePixClientResponse(
            PixType.EMAIL.name,
            "test@email.com",
            LocalDateTime.now()
        )

        Mockito.`when`(itauClient.consultAccount("bd5b4460-bfcb-495b-a250-298a53157f03", AccountType.CONTA_CORRENTE.name)).thenReturn(consutlAccountResponse)
        Mockito.`when`(bcbClient.createPix(MockitoHelper.anyObject())).thenReturn(createPixClientResponse)
        val grpcResponse = grpcClients.register(grpcRequest)

        Assert.assertEquals("bd5b4460-bfcb-495b-a250-298a53157f03", grpcResponse.clientId)
    }

    @MockBean(ItauClient::class)
    fun itauClient(): ItauClient? {
        return Mockito.mock(ItauClient::class.java)
    }

    @MockBean(BCBClient::class)
    fun bcbClient(): BCBClient? {
        return Mockito.mock(BCBClient::class.java)
    }

    object MockitoHelper {
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }
        @Suppress("UNCHECKED_CAST")
        fun <T> uninitialized(): T =  null as T
    }
}