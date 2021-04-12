package com.zup.br.orange.client.bcb.response

import java.time.LocalDateTime

class CreatePixClientResponse (
    val keyType: String,
    val key: String,
    val createdAt: LocalDateTime
)