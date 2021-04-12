package com.zup.br.orange.validation;

import com.zup.br.orange.AccountType
import com.zup.br.orange.entity.pix.enum.PixType
import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.Assert
import org.junit.jupiter.api.Test

@MicronautTest
class ValidPixTest {

    @Test
    fun testValidPixFalse(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.EMAIL,
            null,
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixWithoutType(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            null,
            "test@test.com",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixTrue(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.EMAIL,
            "test@test.com",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }
}
