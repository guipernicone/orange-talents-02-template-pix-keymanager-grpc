package com.zup.br.orange.entity.pix.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ConsultPixIdRequest(
    @field: NotBlank val clientId: String,
    @field: NotBlank val pixId: String,
    override val isPixId: Boolean,
) : ConsultPixKeyRequest