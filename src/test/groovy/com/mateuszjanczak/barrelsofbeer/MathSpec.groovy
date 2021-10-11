package com.mateuszjanczak.barrelsofbeer

import spock.lang.Specification

class MathSpec extends Specification {

    def "1 + 1 = 2"() {
        given:
        def a = 1
        def b = 1

        when:
        def result = a + b

        then:
        result == 2
    }
}
