package com.mateuszjanczak.barrelsofbeer.mock

import com.github.tomakehurst.wiremock.WireMockServer

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.mateuszjanczak.barrelsofbeer.IntegrationTestBase.templateUrl

class SensorMock {

    WireMockServer wireMockServer

    SensorMock(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer
    }

    def getSensorDataSuccessWith(args = [:]) {
        wireMockServer.stubFor(
            get(urlEqualTo(testUrl(args)))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader('Content-Type', 'application/json')
                        .withBody(correctResponseBody(args))
                        .withFixedDelay(args.timeout ?: 0)
                )
        )
    }

    def getSensorDataFailWithInvalidResponse(args = [:]) {
        wireMockServer.stubFor(
            get(urlEqualTo(testUrl(args)))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader('Content-Type', 'application/json')
                        .withBody(incorrectResponseBody()))
        )
    }

    def getSensorDataFailWith(args = [:]) {
        wireMockServer.stubFor(
            get(urlEqualTo(testUrl(args)))
                .willReturn(
                    aResponse()
                        .withStatus(args.statusCode ?: 404)
                )
        )
    }

    private static testUrl(args = [:]) {
        templateUrl()
            .replace("SENSOR_ID", args.id as String ?: "1")
            .replace("[", "%5B")
            .replace("]", "%5D")
    }

    private static correctResponseBody(args = [:]) {
        /{
           "cid":-1,
           "data":{
              "value":"${args.hex as String ?: "43BAC7AE00000204"}"
           },
           "code":200
        }/
    }

    private static incorrectResponseBody(args = [:]) {
        /{
           "data":-1,
           "atad":{
              "value":"43BAC7AE00000204"
           },
           "cid":200
        }/
    }
}
