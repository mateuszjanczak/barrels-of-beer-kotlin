package com.mateuszjanczak.barrelsofbeer.configuration

import com.github.tomakehurst.wiremock.WireMockServer

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

class WireMock {

    static WireMockServer wireMockServer = new WireMockServer(options().dynamicPort())

}
