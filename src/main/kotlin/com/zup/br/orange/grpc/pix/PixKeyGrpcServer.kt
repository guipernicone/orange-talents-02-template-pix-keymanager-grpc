package com.zup.br.orange.grpc.pix

import com.zup.br.orange.*
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest
import com.zup.br.orange.exceptions.handler.ErrorHandler
import com.zup.br.orange.grpc.pix.service.RegisterKeyGrpcService
import com.zup.br.orange.grpc.pix.utils.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton

@ErrorHandler
@Singleton
    class PixKeyRegisterGrpcServer(val registerKeyGrpcService: RegisterKeyGrpcService) : PixKeyRegisterServiceGrpc.PixKeyRegisterServiceImplBase() {

    private val logger = LoggerFactory.getLogger(PixKeyRegisterGrpcServer::class.java)

    override fun register(request: PixKeyRegisterGrpcRequest, responseObserver: StreamObserver<PixKeyRegisterGrpcResponse>) {

        logger.info("Received request :\n$request");

        val pix = registerKeyGrpcService.registerNewPixKey(request.toModel())

        val response = PixKeyRegisterGrpcResponse.newBuilder()
            .setClientId(pix.clientId)
            .setPixId(pix.id.toString())
            .build()

        logger.info("Sending response :\n$response");
        responseObserver.onNext(response);
        responseObserver.onCompleted()

    }
}