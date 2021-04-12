package com.zup.br.orange.exceptions.handler.exceptions

import com.zup.br.orange.exceptions.InternalErrorException
import com.zup.br.orange.exceptions.NotFoundException
import com.zup.br.orange.exceptions.handler.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class InternalErrorExceptionHandler : ExceptionHandler<InternalErrorException> {
    override fun handle(e: InternalErrorException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.INTERNAL
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is InternalErrorException
    }
}