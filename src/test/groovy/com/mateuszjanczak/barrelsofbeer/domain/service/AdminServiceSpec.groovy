package com.mateuszjanczak.barrelsofbeer.domain.service


import com.mateuszjanczak.barrelsofbeer.common.TapNotFoundException
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.ActionEventRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TapRepository
import com.mateuszjanczak.barrelsofbeer.domain.data.repository.TemperatureEventRepository
import spock.lang.Specification
import spock.lang.Unroll

import static com.mateuszjanczak.barrelsofbeer.common.LogType.*
import static com.mateuszjanczak.barrelsofbeer.common.TableType.*
import static com.mateuszjanczak.barrelsofbeer.fixtures.TapFixture.tap

class AdminServiceSpec extends Specification {

    TapRepository tapRepository
    ActionEventRepository actionEventRepository
    TemperatureEventRepository temperatureEventRepository
    EventService eventService

    AdminService adminService

    def setup() {
        tapRepository = Mock()
        actionEventRepository = Mock()
        temperatureEventRepository = Mock()
        eventService = Mock()

        adminService = new DefaultAdminService(tapRepository, actionEventRepository, temperatureEventRepository, eventService)
    }

    def "Should throw exception when toggling the status of tap doesn't exist"() {
        given:
        def tapId = 1

        when:
        adminService.toggleTap(tapId, true)

        then:
        thrown(TapNotFoundException)

        and:
        1 * tapRepository.findByTapId(1) >> null

        and:
        0 * tapRepository.save(_)

        and:
        0 * eventService.saveEvent(_, _)
    }

    @Unroll
    def "Should toggle tap from #previous to #next with specified id"() {
        given:
        def tapId = 1
        def initTap = tap(tapId: tapId, enabled: previous)
        def targetTap = tap(tapId: tapId, enabled: next)

        when:
        adminService.toggleTap(tapId, next)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findByTapId(1) >> initTap

        and:
        1 * tapRepository.save(targetTap) >> targetTap

        and:
        1 * eventService.saveEvent(targetTap, eventType)

        where:
        previous | next  | eventType
        true     | false | TAP_DISABLE
        false    | true  | TAP_ENABLE
    }

    def "Should throw exception when removing a tap doesn't exist"() {
        given:
        def tapId = 1
        def tap = tap(tapId: tapId)

        when:
        adminService.removeTap(tapId)

        then:
        thrown(TapNotFoundException)

        then:
        1 * tapRepository.findByTapId(tapId) >> null

        and:
        0 * eventService.saveEvent(tap, TAP_REMOVE)
    }

    def "Should remove tap with specified id"() {
        given:
        def tapId = 1
        def tap = tap(tapId: tapId)

        when:
        adminService.removeTap(tapId)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.findByTapId(1) >> tap

        and:
        1 * tapRepository.deleteById(tapId)

        and:
        1 * eventService.saveEvent(tap, TAP_REMOVE)
    }

    @Unroll
    def "Should throw exception if invalid table type"() {
        when:
        adminService.resetDatabase(tableType)

        then:
        thrown(Exception)

        and:
        0 * tapRepository.deleteAll()
        0 * actionEventRepository.deleteAll()
        0 * temperatureEventRepository.deleteAll()

        where:
        tableType << [null, {}, "test"]
    }

    def "Should reset database with taps table"() {
        when:
        adminService.resetDatabase(TAPS)

        then:
        noExceptionThrown()

        and:
        1 * tapRepository.deleteAll()
        0 * actionEventRepository.deleteAll()
        0 * temperatureEventRepository.deleteAll()
    }

    def "Should reset database with action events table"() {
        when:
        adminService.resetDatabase(ACTION_EVENTS)

        then:
        noExceptionThrown()

        and:
        0 * tapRepository.deleteAll()
        1 * actionEventRepository.deleteAll()
        0 * temperatureEventRepository.deleteAll()
    }

    def "Should reset database with temperature events table"() {
        when:
        adminService.resetDatabase(TEMPERATURE_EVENTS)

        then:
        noExceptionThrown()

        and:
        0 * tapRepository.deleteAll()
        0 * actionEventRepository.deleteAll()
        1 * temperatureEventRepository.deleteAll()
    }
}
