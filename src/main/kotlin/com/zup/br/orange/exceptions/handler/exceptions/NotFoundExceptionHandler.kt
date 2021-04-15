package com.zup.br.orange.exceptions.handler.exceptions

import com.zup.br.orange.exceptions.DuplicatePixKeyException
import com.zup.br.orange.exceptions.NotFoundException
import com.zup.br.orange.exceptions.handler.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class NotFoundExceptionHandler : ExceptionHandler<NotFoundException> {
    override fun handle(e: NotFoundException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is NotFoundException
    }
}