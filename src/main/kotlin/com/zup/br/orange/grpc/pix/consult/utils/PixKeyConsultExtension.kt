package com.zup.br.orange.grpc.pix.consult.utils

import com.zup.br.orange.PixKeyConsultGrpcRequest
import com.zup.br.orange.entity.pix.request.ConsultPixIdRequest
import com.zup.br.orange.entity.pix.request.ConsultPixKeyRequest
import com.zup.br.orange.entity.pix.request.ConsultPixRequest
import javax.validation.ConstraintViolationException

fun PixKeyConsultGrpcRequest.toModel() : ConsultPixKeyRequest {
    val consult = when(filterCase) {
        PixKeyConsultGrpcRequest.FilterCase.PIXID -> ConsultPixIdRequest(pixId.clientId, pixId.pixId, true)
        PixKeyConsultGrpcRequest.FilterCase.PIXKEY -> ConsultPixRequest(pixKey, false)
        PixKeyConsultGrpcRequest.FilterCase.FILTER_NOT_SET -> throw IllegalArgumentException("Request must have pix key or pix id")
    }

    return consult
}