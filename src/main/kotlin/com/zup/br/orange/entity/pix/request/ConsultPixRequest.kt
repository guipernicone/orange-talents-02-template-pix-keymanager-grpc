package com.zup.br.orange.entity.pix.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
class ConsultPixRequest(
    @field: NotBlank @field: Size(max = 77) val pixValue: String,
    override val isPixId: Boolean
) : ConsultPixKeyRequest