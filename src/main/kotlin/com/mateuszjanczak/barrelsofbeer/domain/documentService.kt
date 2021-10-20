package com.mateuszjanczak.barrelsofbeer.domain

import com.mateuszjanczak.barrelsofbeer.domain.data.document.ActionEvent
import com.mateuszjanczak.barrelsofbeer.domain.data.document.TemperatureEvent
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.time.format.DateTimeFormatter.ofPattern

interface DocumentService {
    fun getActionEventsCsv(): Resource
    fun getTemperatureEventsCsv(): Resource
}

@Service
class DefaultDocumentService(
    private val eventService: EventService
) : DocumentService {

    override fun getActionEventsCsv(): Resource {
        val actionEventsStringList = parseActionEventsToStringList(eventService.getActionEvents())
        val format = CSVFormat.DEFAULT.withHeader(
            "actionId",
            "tap",
            "barrelContent",
            "currentLevel",
            "totalUsage",
            "singleUsage",
            "date",
            "actionType"
        )
        return writeDataToCsv(actionEventsStringList, format)
    }

    override fun getTemperatureEventsCsv(): Resource {
        val temperatureEventsStringList = parseTemperatureEventsToStringList(eventService.getTemperatureEvents())
        val format = CSVFormat.DEFAULT.withHeader("temperatureId", "tap", "barrelContent", "temperature", "date")
        return writeDataToCsv(temperatureEventsStringList, format)
    }

    private fun parseActionEventsToStringList(actionEventList: List<ActionEvent>): List<List<String>> =
        actionEventList.map { actionEvent ->
            listOf(
                actionEvent.id,
                actionEvent.tapId.toString(),
                actionEvent.barrelContent,
                actionEvent.currentLevel.toString(),
                actionEvent.totalUsage.toString(),
                actionEvent.singleUsage.toString(),
                actionEvent.date.format(ofPattern("yyyy-MM-dd HH:mm:ss")),
                actionEvent.logType.toString()
            )
        }

    private fun parseTemperatureEventsToStringList(temperatureEventList: List<TemperatureEvent>): List<List<String>> =
        temperatureEventList.map { temperatureEvent ->
            listOf(
                temperatureEvent.id,
                temperatureEvent.tapId.toString(),
                temperatureEvent.barrelContent,
                temperatureEvent.temperature.toString(),
                temperatureEvent.date.format(ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        }

    private fun writeDataToCsv(eventList: List<List<String>>, format: CSVFormat): Resource {
        try {
            ByteArrayOutputStream().use { stream ->
                CSVPrinter(PrintWriter(stream), format).use { printer ->
                    eventList.forEach { printer.printRecord(it) }
                    printer.flush()
                    return InputStreamResource(ByteArrayInputStream(stream.toByteArray()))
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("IOException: " + e.message)
        }
    }

}