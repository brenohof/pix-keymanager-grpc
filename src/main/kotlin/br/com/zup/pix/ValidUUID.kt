package br.com.zup.pix

import javax.validation.Constraint
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass
import kotlin.annotation.AnnotationTarget.*

@ReportAsSingleViolation
@Constraint(validatedBy = [])
@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}\$",
        flags = [Pattern.Flag.CASE_INSENSITIVE])
@Retention(AnnotationRetention.RUNTIME)
@Target(FIELD, CONSTRUCTOR, PROPERTY, VALUE_PARAMETER)
annotation class ValidUUID(
    val message: String = "não é um formato válido de UUID",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)
