package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.common.TapAlreadyExistsException
import com.mateuszjanczak.barrelsofbeer.common.TapNotFoundException
import com.mateuszjanczak.barrelsofbeer.common.UserAlreadyExistsException
import com.mateuszjanczak.barrelsofbeer.security.common.AccountNotEnabledException
import com.mateuszjanczak.barrelsofbeer.security.common.InvalidPasswordException
import com.mateuszjanczak.barrelsofbeer.security.common.UserNotFoundException
import com.mateuszjanczak.barrelsofbeer.security.data.dto.ErrorMessage
import com.mateuszjanczak.barrelsofbeer.security.data.dto.ValidationErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ValidationErrorMessage> {
        val fields: Map<String, List<String>> = e.bindingResult.fieldErrors.groupBy { it.field }.mapValues { fieldError -> fieldError.value.map { error ->  error.defaultMessage!!} }
        return ResponseEntity(ValidationErrorMessage("Validation failed.", HttpStatus.BAD_REQUEST.name, fields), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Invalid argument.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Invalid request body.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Illegal argument.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("User not found.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("User already exists.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPasswordException(e: InvalidPasswordException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Invalid password.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccountNotEnabledException::class)
    fun handleAccountNotEnabledException(e: AccountNotEnabledException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Account is not enabled.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TapNotFoundException::class)
    fun handleTapNotFoundException(e: TapNotFoundException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Tap not found.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TapAlreadyExistsException::class)
    fun handleTapAlreadyExistsException(e: TapAlreadyExistsException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage("Tap already exists.", HttpStatus.BAD_REQUEST.name), HttpStatus.BAD_REQUEST)
    }
}