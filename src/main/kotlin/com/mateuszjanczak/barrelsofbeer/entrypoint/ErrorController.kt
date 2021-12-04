package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.security.common.AccountNotEnabledException
import com.mateuszjanczak.barrelsofbeer.security.common.InvalidPasswordException
import com.mateuszjanczak.barrelsofbeer.security.common.UserNotFoundException
import com.mateuszjanczak.barrelsofbeer.security.data.dto.ErrorMessage
import com.mateuszjanczak.barrelsofbeer.security.data.dto.ValidationErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ValidationErrorMessage> {
        val fields: Map<String, List<String>> = e.bindingResult.fieldErrors.groupBy { it.field }.mapValues { fieldError -> fieldError.value.map { error ->  error.defaultMessage!!} }
        return ResponseEntity(ValidationErrorMessage("Validation failed.", HttpStatus.BAD_REQUEST.name, fields), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("User not found.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPasswordException(e: InvalidPasswordException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Invalid password.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccountNotEnabledException::class)
    fun handleAccountNotEnabledException(e: AccountNotEnabledException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Account is not enabled.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }
}