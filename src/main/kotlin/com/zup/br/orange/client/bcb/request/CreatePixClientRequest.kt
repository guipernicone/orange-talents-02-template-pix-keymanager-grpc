package com.zup.br.orange.client.bcb.request

import com.zup.br.orange.client.itau.response.ConsultAccountResponse
import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest

class CreatePixClientRequest (
    registerPixKeyRequest: RegisterPixKeyRequest,
    consultAccountResponse: ConsultAccountResponse
){
    val keyType = registerPixKeyRequest.keyType?.name
    val key = registerPixKeyRequest.keyValue
    val bankAccount = CreatePixBankAccountRequest(
        consultAccountResponse.instituicao.ispb,
        consultAccountResponse.agencia,
        consultAccountResponse.numero,
        AccountTypeBCBClientRequest.valueOf(consultAccountResponse.tipo).value
    )
    val owner = CreatePixOwnerRequest("NATURAL_PERSON", consultAccountResponse.titular.nome, consultAccountResponse.titular.cpf)

    override fun toString(): String {
        return "CreatePixClientRequest(keyType='$keyType', key=$key, bankAccount=$bankAccount, owner=$owner)"
    }


}
