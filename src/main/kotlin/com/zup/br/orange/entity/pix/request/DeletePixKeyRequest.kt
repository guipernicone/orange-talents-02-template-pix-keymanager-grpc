package com.zup.br.orange.entity.pix.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class DeletePixKeyRequest (
    @field: NotBlank val pixId: Long,
    @field: NotBlank val clientId: String
)