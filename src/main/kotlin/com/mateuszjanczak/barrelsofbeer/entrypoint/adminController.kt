package com.mateuszjanczak.barrelsofbeer.entrypoint

import com.mateuszjanczak.barrelsofbeer.domain.AdminService
import com.mateuszjanczak.barrelsofbeer.entrypoint.AdminEndpoints.ADMIN_ENABLE_TAP
import com.mateuszjanczak.barrelsofbeer.entrypoint.AdminEndpoints.ADMIN_DISABLE_TAP
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping(value = ["/api"])
class AdminController(
    private val adminService: AdminService
) {

    @PostMapping(ADMIN_ENABLE_TAP)
    fun enableTap(@PathVariable tapId: Int): ResponseEntity<Unit> =
        ResponseEntity.ok(adminService.toggleTap(tapId, true))

    @PostMapping(ADMIN_DISABLE_TAP)
    fun disableTap(@PathVariable tapId: Int): ResponseEntity<Unit> =
        ResponseEntity.ok(adminService.toggleTap(tapId, false))
}

object AdminEndpoints {
    const val ADMIN_ENABLE_TAP = "/admin/tap/{tapId}/enable"
    const val ADMIN_DISABLE_TAP = "/admin/tap/{tapId}/disable"
}