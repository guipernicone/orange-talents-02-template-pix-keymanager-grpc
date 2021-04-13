package com.zup.br.orange.client.bcb.response

import java.time.LocalDateTime

class DeletePixClientResponse (
    val key: String,
    val participant: String,
    val deletedAt: LocalDateTime,
)