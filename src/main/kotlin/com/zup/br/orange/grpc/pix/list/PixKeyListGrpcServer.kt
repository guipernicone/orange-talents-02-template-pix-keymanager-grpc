package com.zup.br.orange.grpc.pix.list

import com.zup.br.orange.PixKeyListGrpcRequest
import com.zup.br.orange.PixKeyListGrpcResponse
import com.zup.br.orange.PixKeyListServiceGrpc
import com.zup.br.orange.exceptions.handler.ErrorHandler
import com.zup.br.orange.grpc.pix.list.service.PixKeyListGrpcService
import com.zup.br.orange.grpc.pix.list.utils.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixKeyListGrpcServer(val pixKeyListGrpcService: PixKeyListGrpcService) : PixKeyListServiceGrpc.PixKeyListServiceImplBase() {
    private val logger = LoggerFactory.getLogger(PixKeyListGrpcServer::class.java)

    override fun list(request: PixKeyListGrpcRequest, responseObserver: StreamObserver<PixKeyListGrpcResponse>) {
        logger.info("Received list request :\n$request");

        val response = pixKeyListGrpcService.listPixs(request.toModel())

        logger.info("Sending list response :\n$response");
        responseObserver.onNext(response);
        responseObserver.onCompleted()
    }
}