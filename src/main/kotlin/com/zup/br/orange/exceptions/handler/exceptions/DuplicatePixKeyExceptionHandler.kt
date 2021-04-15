package com.zup.br.orange.exceptions.handler.exceptions

import com.zup.br.orange.exceptions.DuplicatePixKeyException
import com.zup.br.orange.exceptions.handler.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class DuplicatePixKeyExceptionHandler : ExceptionHandler<DuplicatePixKeyException> {
    override fun handle(e: DuplicatePixKeyException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.ALREADY_EXISTS
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is DuplicatePixKeyException
    }

}