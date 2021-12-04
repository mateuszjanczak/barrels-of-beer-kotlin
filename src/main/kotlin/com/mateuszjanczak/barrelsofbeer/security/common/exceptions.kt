package com.mateuszjanczak.barrelsofbeer.security.common

class UserNotFoundException : RuntimeException("UserNotFoundException")
class InvalidPasswordException : RuntimeException("InvalidPasswordException")
class AccountNotEnabledException : RuntimeException("AccountNotEnabledException")