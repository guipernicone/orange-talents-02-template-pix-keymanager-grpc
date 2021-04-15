package com.zup.br.orange.entity.pix.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ListPixKeyRequest(
    @field:NotBlank val clientId: String
)