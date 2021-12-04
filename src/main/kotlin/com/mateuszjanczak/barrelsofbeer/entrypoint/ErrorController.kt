package com.mateuszjanczak.barrelsofbeer.entrypoint

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
}