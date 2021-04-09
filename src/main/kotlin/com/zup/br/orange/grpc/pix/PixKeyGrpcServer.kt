package com.zup.br.orange.grpc.pix

import com.zup.br.orange.*
import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest
import com.zup.br.orange.exceptions.handler.ErrorHandler
import com.zup.br.orange.grpc.pix.service.RegisterKeyGrpcService
import com.zup.br.orange.grpc.pix.utils.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixKeyGrpcServer(val registerKeyGrpcService: RegisterKeyGrpcService) : PixKeyServiceGrpc.PixKeyServiceImplBase() {

    private val logger = LoggerFactory.getLogger(PixKeyGrpcServer::class.java)

    override fun register(request: RegisterPixKeyGrpcRequest, responseObserver: StreamObserver<RegisterPixKeyGrpcResponse>) {

        logger.info("Received request :\n$request");


        val registerRequest: RegisterPixKeyRequest = request.toModel()
        println(registerRequest)
        registerKeyGrpcService.registerNewPixKey(registerRequest)

        val response = RegisterPixKeyGrpcResponse.newBuilder()
            .setClientId(request.clientId)
            .setPixId("id")
            .build()

        logger.info("Sending response :\n$response");
        responseObserver.onNext(response);
        responseObserver.onCompleted()

    }
}