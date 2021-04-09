package com.zup.br.orange.entity.pix.request

import com.zup.br.orange.AccountType
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.validation.ValidPix
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPix
data class RegisterPixKeyRequest(
    @field: NotBlank val clientId: String?,
    @field: NotNull val keyType: PixType?,
    @field: Size(max = 77) val keyValue: String?,
    @field: NotNull val accountType: AccountType?

    ) {
}