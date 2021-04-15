package com.zup.br.orange.grpc.pix.list.utils

import com.zup.br.orange.PixKeyListGrpcRequest
import com.zup.br.orange.entity.pix.request.ListPixKeyRequest

fun PixKeyListGrpcRequest.toModel() : ListPixKeyRequest {
    return ListPixKeyRequest(this.clientId)
}