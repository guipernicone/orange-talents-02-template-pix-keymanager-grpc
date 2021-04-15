package com.zup.br.orange.grpc.pix.consult

import com.zup.br.orange.PixKeyConsultGrpcRequest
import com.zup.br.orange.PixKeyConsultGrpcResponse
import com.zup.br.orange.PixKeyConsultServiceGrpc
import com.zup.br.orange.entity.pix.request.ConsultPixIdRequest
import com.zup.br.orange.entity.pix.request.ConsultPixKeyRequest
import com.zup.br.orange.entity.pix.request.ConsultPixRequest
import com.zup.br.orange.exceptions.handler.ErrorHandler
import com.zup.br.orange.grpc.pix.consult.service.PixKeyConsultGrpcService
import com.zup.br.orange.grpc.pix.consult.utils.toModel
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixKeyConsultGrpcServer(val pixKeyConsultGrpcService: PixKeyConsultGrpcService) : PixKeyConsultServiceGrpc.PixKeyConsultServiceImplBase() {
    private val logger = LoggerFactory.getLogger(PixKeyConsultGrpcServer::class.java)

    override fun consult(
        request: PixKeyConsultGrpcRequest,
        responseObserver: StreamObserver<PixKeyConsultGrpcResponse>
    ) {
        logger.info("Received consult request :\n$request");

        val consultRequest = request.toModel()
        val response = if (consultRequest.isPixId) {
            pixKeyConsultGrpcService.consultPixId(consultRequest as ConsultPixIdRequest)
        }
        else{
            pixKeyConsultGrpcService.consultPixValue(consultRequest as ConsultPixRequest)
        }

        logger.info("Sending consult response :\n$response");
        responseObserver.onNext(response);
        responseObserver.onCompleted()
    }
}