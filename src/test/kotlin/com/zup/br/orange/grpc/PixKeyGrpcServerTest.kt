package com.zup.br.orange.grpc

import com.zup.br.orange.AccountType
import com.zup.br.orange.KeyType
import com.zup.br.orange.PixKeyRegisterGrpcRequest
import com.zup.br.orange.PixKeyRegisterServiceGrpc
import com.zup.br.orange.client.bcb.BCBClient
import com.zup.br.orange.client.bcb.response.CreatePixClientResponse
import com.zup.br.orange.client.itau.ItauClient
import com.zup.br.orange.client.itau.response.ConsultAccountInstituicaoResponse
import com.zup.br.orange.client.itau.response.ConsultAccountResponse
import com.zup.br.orange.client.itau.response.ConsultAccountTitularResponse
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.grpc.pix.register.utils.toModel
import com.zup.br.orange.repository.PixRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import utils.MockitoHelper
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest(transactional = false)
class PixKeyGrpcServerTest(
    val grpcClients: PixKeyRegisterServiceGrpc.PixKeyRegisterServiceBlockingStub,
    val pixRepository: PixRepository
) {

    @Inject
    lateinit var itauClient: ItauClient

    @Inject
    lateinit var bcbClient: BCBClient

    lateinit var grpcRequest: PixKeyRegisterGrpcRequest

    @BeforeEach
    fun setup(){
        grpcRequest = PixKeyRegisterGrpcRequest.newBuilder()
            .setClientId("bd5b4460-bfcb-495b-a250-298a53157f03")
            .setKeyType(KeyType.EMAIL)
            .setKeyValue("test@email.com")
            .setAccount(AccountType.CONTA_CORRENTE)
            .build()
    }

    @AfterEach
    fun end(){
        pixRepository.deleteAll()
    }

    @Test
    fun testRegisterPix(){
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
        Mockito.`when`(
            itauClient.consultAccount(grpcRequest.clientId, grpcRequest.account.name)
        ).thenReturn(consutlAccountResponse)
        Mockito.`when`(bcbClient.createPix(MockitoHelper.anyObject())).thenReturn(createPixClientResponse)
        val grpcResponse = grpcClients.register(grpcRequest)

        Assert.assertEquals("bd5b4460-bfcb-495b-a250-298a53157f03", grpcResponse.clientId)
        pixRepository.findById(grpcResponse.pixId.toLong()).let{
            Assert.assertTrue(it.isPresent)
            Assert.assertEquals("bd5b4460-bfcb-495b-a250-298a53157f03", it.get().clientId)
        }

    }

    @Test
    fun testRegisterPixDuplicatePix(){
        pixRepository.save(grpcRequest.toModel().toModel(LocalDateTime.now(),grpcRequest.keyValue))

        val exception = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java) {
            grpcClients.register(grpcRequest)
        }
        Assert.assertEquals("ALREADY_EXISTS: Pix key - test@email.com is already register", exception.message)
    }

    @Test
    fun testRegisterPixNotFound(){

        val exception = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java) {
            grpcClients.register(grpcRequest)
        }

        Assert.assertEquals("NOT_FOUND: Account with id: bd5b4460-bfcb-495b-a250-298a53157f03 and type: EMAIL not found", exception.message)
    }

    @Test
    fun testRegisterPixInternalError(){
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
        Mockito.`when`(
            itauClient.consultAccount(grpcRequest.clientId, grpcRequest.account.name)
        ).thenReturn(consutlAccountResponse)


        val exception = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java) {
            grpcClients.register(grpcRequest)
        }

        Assert.assertEquals("INTERNAL: Error while trying to create pix key", exception.message)
    }

    @MockBean(ItauClient::class)
    fun itauClient(): ItauClient? {
        return Mockito.mock(ItauClient::class.java)
    }

    @MockBean(BCBClient::class)
    fun bcbClient(): BCBClient? {
        return Mockito.mock(BCBClient::class.java)
    }
}