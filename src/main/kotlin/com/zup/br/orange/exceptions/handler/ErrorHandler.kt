package com.zup.br.orange.exceptions.handler

import io.micronaut.aop.Around
import io.micronaut.context.annotation.Type

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Around
@Type(ExceptionHandlerInterceptor::class)
annotation class ErrorHandler()
