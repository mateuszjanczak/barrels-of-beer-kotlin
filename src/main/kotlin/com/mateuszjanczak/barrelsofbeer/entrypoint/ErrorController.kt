package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.security.data.dto.Field
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
        val fields = e.bindingResult.fieldErrors.map { fieldError -> Field(fieldError.field, fieldError.defaultMessage!!.split(",")) }
        return ResponseEntity(ValidationErrorMessage("Validation failed.", HttpStatus.BAD_REQUEST.name, fields), HttpStatus.BAD_REQUEST)
    }
}