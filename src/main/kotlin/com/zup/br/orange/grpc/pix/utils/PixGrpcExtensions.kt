package com.zup.br.orange.grpc.pix.utils

import com.zup.br.orange.AccountType
import com.zup.br.orange.KeyType
import com.zup.br.orange.RegisterPixKeyGrpcRequest
import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest

fun RegisterPixKeyGrpcRequest.toModel() : RegisterPixKeyRequest {
    return RegisterPixKeyRequest(
        clientId = this.clientId,
        keyType = when(this.keyType) {
            KeyType.UNKNOWN_TYPE -> null
            else -> KeyType.valueOf(this.keyType.name)
        },
        keyValue = this.keyValue,
        accountType = when(this.account){
            AccountType.UNKNOWN_ACCOUNT -> null
            else -> AccountType.valueOf(this.account.name)
        }
    )
}