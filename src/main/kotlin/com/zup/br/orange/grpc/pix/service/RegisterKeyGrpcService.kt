package com.zup.br.orange.grpc.pix.service

import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest
import com.zup.br.orange.exceptions.DuplicatePixKeyException
import com.zup.br.orange.repository.PixRepository
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class RegisterKeyGrpcService(var pixRepository: PixRepository) {

    @Transactional
    fun registerNewPixKey(@Valid registerPixKeyRequest: RegisterPixKeyRequest) {
        println("Chave Valida")

        if (pixRepository.existsByPixValue(registerPixKeyRequest.keyValue))
            throw DuplicatePixKeyException("Pix key - ${registerPixKeyRequest.keyValue} is already register")

        println("Chave Unica")
    }

}