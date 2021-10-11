package com.mateuszjanczak.barrelsofbeer.mock

import com.github.tomakehurst.wiremock.WireMockServer

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

class TestMock {

    WireMockServer wireMockServer

    TestMock(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer
    }

    def success(args = [:]) {
        wireMockServer.stubFor(
            get(urlEqualTo('/hello'))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader('Content-Type', 'application/json')
                        .withBody(responseBody())
                )
        )
    }

    def fail(args = [:]) {
        wireMockServer.stubFor(
            get(urlEqualTo('/hello'))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                )
        )
    }

    static responseBody(args = [:]) {
        /{
           "hello": "world"
        }/
    }
}
