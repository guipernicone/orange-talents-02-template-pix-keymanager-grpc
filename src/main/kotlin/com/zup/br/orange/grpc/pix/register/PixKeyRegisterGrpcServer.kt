package com.zup.br.orange.grpc.pix.register

import com.zup.br.orange.*
import com.zup.br.orange.exceptions.handler.ErrorHandler
import com.zup.br.orange.grpc.pix.register.service.RegisterKeyGrpcService
import com.zup.br.orange.grpc.pix.register.utils.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
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