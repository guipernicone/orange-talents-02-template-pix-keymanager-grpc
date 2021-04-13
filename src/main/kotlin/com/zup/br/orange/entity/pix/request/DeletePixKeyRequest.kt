package com.zup.br.orange.entity.pix.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class DeletePixKeyRequest (
    @field: NotNull val pixId: Long,
    @field: NotBlank val clientId: String
)