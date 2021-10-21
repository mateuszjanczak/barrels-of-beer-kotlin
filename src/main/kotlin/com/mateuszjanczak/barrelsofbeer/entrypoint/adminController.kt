package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.common.TableType
import com.mateuszjanczak.barrelsofbeer.domain.AdminService
import com.mateuszjanczak.barrelsofbeer.domain.data.dto.ErrorMessage
import com.mateuszjanczak.barrelsofbeer.entrypoint.AdminEndpoints.ADMIN_DISABLE_TAP
import com.mateuszjanczak.barrelsofbeer.entrypoint.AdminEndpoints.ADMIN_ENABLE_TAP
import com.mateuszjanczak.barrelsofbeer.entrypoint.AdminEndpoints.ADMIN_REMOVE_TAP
import com.mateuszjanczak.barrelsofbeer.entrypoint.AdminEndpoints.ADMIN_RESET_DATABASE
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestController
@CrossOrigin
@RequestMapping(value = ["/api"])
class AdminController(
    private val adminService: AdminService
) {

    @PostMapping(ADMIN_ENABLE_TAP)
    fun enableTap(@PathVariable tapId: Int): ResponseEntity<Unit> =
        ok(adminService.toggleTap(tapId, true))

    @PostMapping(ADMIN_DISABLE_TAP)
    fun disableTap(@PathVariable tapId: Int): ResponseEntity<Unit> =
        ok(adminService.toggleTap(tapId, false))

    @PostMapping(ADMIN_REMOVE_TAP)
    fun removeTap(@PathVariable tapId: Int): ResponseEntity<Unit> =
        ok(adminService.removeTap(tapId))

    @PostMapping(ADMIN_RESET_DATABASE)
    fun resetDatabase(@PathVariable tableType: TableType): ResponseEntity<Unit> =
        ok(adminService.resetDatabase(tableType))

    @ExceptionHandler
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException) =
        status(BAD_REQUEST).body(ErrorMessage("MethodArgumentTypeMismatchException", BAD_REQUEST.name))
}

object AdminEndpoints {
    const val ADMIN_ENABLE_TAP = "/admin/tap/{tapId}/enable"
    const val ADMIN_DISABLE_TAP = "/admin/tap/{tapId}/disable"
    const val ADMIN_REMOVE_TAP = "/admin/tap/{tapId}/remove"
    const val ADMIN_RESET_DATABASE = "/admin/database/{tableType}/reset"
}