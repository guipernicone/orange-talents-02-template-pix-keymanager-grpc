package com.zup.br.orange.grpc.consult

import com.zup.br.orange.AccountType
import com.zup.br.orange.PixKeyConsultGrpcRequest
import com.zup.br.orange.PixKeyConsultServiceGrpc
import com.zup.br.orange.client.bcb.BCBClient
import com.zup.br.orange.client.bcb.response.ConsultBankAccountPixClientResponse
import com.zup.br.orange.client.bcb.response.ConsultOwnerPixClientResponse
import com.zup.br.orange.client.bcb.response.ConsultPixClientResponse
import com.zup.br.orange.client.itau.ItauClient
import com.zup.br.orange.client.itau.response.ConsultAccountInstituicaoResponse
import com.zup.br.orange.client.itau.response.ConsultAccountResponse
import com.zup.br.orange.client.itau.response.ConsultAccountTitularResponse
import com.zup.br.orange.entity.pix.Pix
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.repository.PixRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
class PixKeyConsultGrpcServerTest(
    val grpcClient: PixKeyConsultServiceGrpc.PixKeyConsultServiceBlockingStub,
    val pixRepository: PixRepository
) {

    @Inject
    lateinit var itauClient: ItauClient

    @Inject
    lateinit var bcbClient: BCBClient

    lateinit var pix: Pix

    @BeforeEach
    fun setup(){
        pix = Pix(
            "bd5b4460-bfcb-495b-a250-298a53157f03",
            AccountType.CONTA_CORRENTE,
            PixType.RANDOM,
            UUID.randomUUID().toString(),
            "60701190",
            LocalDateTime.now()
        )
        pixRepository.save(pix)
    }

    @AfterEach
    fun close(){
        pixRepository.deleteAll()
    }

    @Test
    fun testConsultPixId(){
        val pixIdRequest = PixKeyConsultGrpcRequest.newBuilder().pixIdBuilder
            .setPixId(pix.id.toString())
            .setClientId(pix.clientId)

        val grpcRequest = PixKeyConsultGrpcRequest.newBuilder()
            .setPixId(pixIdRequest)
            .build()

        val consultAccountTitularResponse = ConsultAccountTitularResponse(
            pix.clientId,
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

        Mockito.`when`(itauClient.consultAccount(pix.clientId, pix.accountType.name)).thenReturn(consutlAccountResponse)

        val grpcResponse = grpcClient.consult(grpcRequest)

        Assert.assertEquals(pix.clientId, grpcResponse.clientId)
        Assert.assertEquals(pix.id.toString(), grpcResponse.pixId)
        Assert.assertTrue(pixRepository.findByIdAndClientId(pix.id, pix.clientId).isPresent)

    }

    @Test
    fun testConsultPixIdNotFound(){
        val pixIdRequest = PixKeyConsultGrpcRequest.newBuilder().pixIdBuilder
            .setPixId("1")
            .setClientId("pix.clientId")

        val grpcRequest = PixKeyConsultGrpcRequest.newBuilder()
            .setPixId(pixIdRequest)
            .build()

        val response = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java){
            grpcClient.consult(grpcRequest)
        }

        Assert.assertEquals("NOT_FOUND: Pix with id: 1 and clientId: pix.clientId not found", response.message)
    }

    @Test
    fun testConsultPixIdInternalError(){
        val pixIdRequest = PixKeyConsultGrpcRequest.newBuilder().pixIdBuilder
            .setPixId(pix.id.toString())
            .setClientId(pix.clientId)

        val grpcRequest = PixKeyConsultGrpcRequest.newBuilder()
            .setPixId(pixIdRequest)
            .build()

        val response = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java){
            grpcClient.consult(grpcRequest)
        }

        Assert.assertEquals("INTERNAL: An unexpected error occurred", response.message)
    }

    @Test
    fun testConsultPixValue(){
        val grpcRequest = PixKeyConsultGrpcRequest.newBuilder()
            .setPixKey(pix.pixValue)
            .build()

        val consultAccountTitularResponse = ConsultAccountTitularResponse(
            pix.clientId,
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

        Mockito.`when`(itauClient.consultAccount(pix.clientId, pix.accountType.name)).thenReturn(consutlAccountResponse)

        val grpcResponse = grpcClient.consult(grpcRequest)

        Assert.assertEquals(pix.clientId, grpcResponse.clientId)
        Assert.assertEquals(pix.id.toString(), grpcResponse.pixId)
        Assert.assertTrue(pixRepository.findByIdAndClientId(pix.id, pix.clientId).isPresent)

    }

    @Test
    fun testConsultPixValueBcb(){
        val uuid = UUID.randomUUID().toString()
        val grpcRequest = PixKeyConsultGrpcRequest.newBuilder()
            .setPixKey(uuid)
            .build()

        val consultBankResponse = ConsultBankAccountPixClientResponse (
            "60701190",
            "0001",
            "291900",
            "CACC"
        )

        val consultOwnerResponse = ConsultOwnerPixClientResponse(
            "NATURAL_PERSON",
            "User Test",
            "95026867057"
        )

        val consultResponse = ConsultPixClientResponse(
            "RANDOM",
            uuid,
            consultBankResponse,
            consultOwnerResponse,
            LocalDateTime.now()
        )
        Mockito.`when`(bcbClient.consultPix(uuid)).thenReturn(consultResponse)

        val grpcResponse = grpcClient.consult(grpcRequest)

        Assert.assertTrue(grpcResponse.clientId.isEmpty())
        Assert.assertTrue(grpcResponse.pixId.isEmpty())
        Assert.assertEquals(uuid, grpcResponse.pix.key)
        Assert.assertFalse(pixRepository.existsByPixValue(uuid))

    }

    @Test
    fun testConsultPixValueBcbInternalError(){
        val uuid = UUID.randomUUID().toString()
        val grpcRequest = PixKeyConsultGrpcRequest.newBuilder()
            .setPixKey(uuid)
            .build()

        val response = Assert.assertThrows(io.grpc.StatusRuntimeException::class.java){
            grpcClient.consult(grpcRequest)
        }

        Assert.assertEquals("NOT_FOUND: Pix with value ${uuid}, not found", response.message)

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