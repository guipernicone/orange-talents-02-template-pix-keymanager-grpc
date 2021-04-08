package com.zup.br.orange

import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class PixKeyGrpcServer : PixKeyServiceGrpc.PixKeyServiceImplBase() {

    private val logger = LoggerFactory.getLogger(PixKeyGrpcServer::class.java)

    override fun register(request: RegisterPixKeyRequest?, responseObserver: StreamObserver<RegisterPixKeyResponse>?) {

        logger.info("Received request :\n$request");

        val response = RegisterPixKeyResponse.newBuilder()
            .setClientId(request?.clientId)
            .setPixId("id")
            .build()

        logger.info("Sending response :\n$response");
        responseObserver!!.onNext(response);
        responseObserver.onCompleted()

    }
}