package com.zup.br.orange.validation

import com.zup.br.orange.KeyType
import com.zup.br.orange.entity.pix.Pix
import com.zup.br.orange.entity.pix.request.RegisterPixKeyRequest
import io.micronaut.core.annotation.AnnotationMetadata
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixValidator::class])
annotation class ValidPix(
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
    val massage: String = "Invalid Pix Key"
)

@Singleton
class ValidPixValidator(val entityManager: EntityManager) : ConstraintValidator<ValidPix, RegisterPixKeyRequest> {

    override fun isValid(
        @Nullable value: RegisterPixKeyRequest?,
        @NonNull annotationMetadata: AnnotationValue<ValidPix>,
        @NonNull context: ConstraintValidatorContext
    ): Boolean {

        if(value?.keyType == null)
            return false

        return value.keyType.valid(value.keyValue)
    }
}