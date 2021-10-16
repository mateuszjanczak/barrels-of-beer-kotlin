package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.domain.data.document.Tap
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import org.springframework.stereotype.Service

interface TapService {
    fun getTapList(): List<Tap>
}

@Service
class DefaultTapService(
    private val tapRepository: TapRepository
) : TapService {

    override fun getTapList(): List<Tap> = tapRepository.findAll()
}