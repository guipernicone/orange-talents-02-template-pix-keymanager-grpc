package com.zup.br.orange.grpc.pix.delete

import com.zup.br.orange.PixKeyDeleteGrpcRequest
import com.zup.br.orange.PixKeyDeleteGrpcResponse
import com.zup.br.orange.PixKeyDeleteServiceGrpc
import com.zup.br.orange.exceptions.handler.ErrorHandler
import com.zup.br.orange.grpc.pix.delete.service.PixKeyDeleteGrpcService
import com.zup.br.orange.grpc.pix.delete.utils.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixKeyDeleteGrpcServer(val pixKeyDeleteGrpcService: PixKeyDeleteGrpcService ) : PixKeyDeleteServiceGrpc.PixKeyDeleteServiceImplBase() {
    private val logger = LoggerFactory.getLogger(PixKeyDeleteGrpcServer::class.java)

    override fun delete(request: PixKeyDeleteGrpcRequest, responseObserver: StreamObserver<PixKeyDeleteGrpcResponse>) {

        logger.info("Received request :\n$request")

        val response = pixKeyDeleteGrpcService.deletePixKey(request.toModel())

        logger.info("Sending response :\n$response");
        responseObserver.onNext(response);
        responseObserver.onCompleted()

    }
}