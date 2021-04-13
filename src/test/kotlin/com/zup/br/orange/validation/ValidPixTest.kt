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
    fun testValidPixCpfFalse(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.CPF,
            "4783082707",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixCpfTrue(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.CPF,
            "47830827079",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixPhoneNull(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.PHONE,
            null,
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }
    @Test
    fun testValidPixPhoneFalse(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.PHONE,
            "88991606762",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixPhoneTrue(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.PHONE,
            "+5598991606762",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixEmailNull(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.EMAIL,
            null,
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }
    @Test
    fun testValidPixEmailFalse(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.EMAIL,
            "email",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixEmailTrue(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.EMAIL,
            "email@email.com",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixRandomFalse(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.RANDOM,
            "false",
            AccountType.CONTA_CORRENTE
        )
        Assert.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixRandomTrue(){
        val registerPixKeyRequest = RegisterPixKeyRequest(
            "clientId",
            PixType.RANDOM,
            null,
            AccountType.CONTA_CORRENTE
        )
        Assert.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }
}
