package com.mateuszjanczak.barrelsofbeer.security.data.dto.validation

import org.passay.CharacterRule
import org.passay.EnglishCharacterData.Digit
import org.passay.EnglishCharacterData.LowerCase
import org.passay.EnglishCharacterData.Special
import org.passay.EnglishCharacterData.UpperCase
import org.passay.LengthRule
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.WhitespaceRule
import java.util.stream.Collectors
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordConstraintValidator::class])
@Target(FIELD)
@Retention(RUNTIME)
annotation class ValidPassword(
    val message: String = "Invalid Password",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PasswordConstraintValidator : ConstraintValidator<ValidPassword, String> {

    override fun initialize(arg0: ValidPassword) {}

    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {

        val validator = PasswordValidator(
            listOf(
                LengthRule(8, 30),
                CharacterRule(UpperCase, 1),
                CharacterRule(LowerCase, 1),
                CharacterRule(Digit, 1),
                CharacterRule(Special, 1),
                WhitespaceRule()
            )
        )

        val result = validator.validate(PasswordData(password))

        if (result.isValid) return true

        val messages = validator.getMessages(result)
        val messageTemplate = messages.stream().collect(Collectors.joining(","))

        context.buildConstraintViolationWithTemplate(messageTemplate)
            .addConstraintViolation()
            .disableDefaultConstraintViolation()

        return false
    }
}