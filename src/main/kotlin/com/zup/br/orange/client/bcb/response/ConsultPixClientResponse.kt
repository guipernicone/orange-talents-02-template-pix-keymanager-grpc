package com.zup.br.orange.client.bcb.response

import java.time.LocalDateTime

class ConsultPixClientResponse(
    val keyType: String,
    val key: String,
    val bankAccount: ConsultBankAccountPixClientResponse,
    val owner: ConsultOwnerPixClientResponse,
    val createdAt: LocalDateTime
)