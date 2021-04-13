package com.zup.br.orange.grpc.pix.delete.utils

import com.zup.br.orange.PixKeyDeleteGrpcRequest
import com.zup.br.orange.entity.pix.request.DeletePixKeyRequest

fun PixKeyDeleteGrpcRequest.toModel() : DeletePixKeyRequest {
    return DeletePixKeyRequest(pixId.toLong(), clientId)
}