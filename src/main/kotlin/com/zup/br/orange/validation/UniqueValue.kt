package com.zup.br.orange.validation

//import io.micronaut.core.annotation.AnnotationValue
//import io.micronaut.validation.validator.constraints.ConstraintValidator
//import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
//import javax.inject.Singleton
//import javax.persistence.EntityManager
//import javax.validation.Constraint
//import javax.validation.Payload
//import kotlin.reflect.KClass
//
//@MustBeDocumented
//@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR)
//@Retention(AnnotationRetention.RUNTIME)
//@Constraint(validatedBy = [UniqueValueValidator::class])
//annotation class UniqueValue(
//    val groups: Array<KClass<Any>> = [],
//    val payload: Array<KClass<Payload>> = [],
//    val massega: String = "Field must be unique",
//    val field: String,
//    val domainClass: KClass<Any>
//)
//
//@Singleton
//class UniqueValueValidator(val entityManager: EntityManager) : ConstraintValidator<UniqueValue, String> {
//
//    override fun isValid(
//        value: String?,
//        annotationMetadata: AnnotationValue<UniqueValue>,
//        context: ConstraintValidatorContext
//    ): Boolean {
//
//        val field = annotationMetadata.stringValue("field").get()
//        val domainClass = annotationMetadata.classValue("domainClass").get()
//
//    }
//}
