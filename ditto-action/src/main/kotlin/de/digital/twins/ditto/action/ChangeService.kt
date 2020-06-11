package de.digital.twins.ditto.action

import com.google.gson.JsonParser
import mu.KotlinLogging
import org.eclipse.ditto.client.changes.Change

object ChangeService {

    private val logger = KotlinLogging.logger{}

    fun evaluateChange(change: Change) {
        val entityId = change.entityId
        val entitySplit = entityId.split(":")
        val idOfEntity = entitySplit[1].toInt()

        when (entitySplit[0]) {
            "robot" -> robotHandler(idOfEntity, change)
            "place" -> placeHandler(change)
            "item" -> itemHandler(change)
            "workflow" -> workflowHandler(idOfEntity, change)
        }
    }

    private fun workflowHandler(id: Int, change: Change) {
        val parser = JsonParser()

        val valueParsed = parser.parse(change.value.get().formatAsString())
        val properties = valueParsed.asJsonObject.get("properties")

        if (properties.asJsonObject.has("totalTime")) {
            val totalTime = properties.asJsonObject.get("totalTime")
            logger.info { "NEW TOTAL TIME: $totalTime" }
            WorkFlowHandler.addTotalTimeToWorkFlow(id, totalTime.asDouble)
        }
    }

    private fun itemHandler(change: Change) {}

    private fun placeHandler(change: Change) {}

    private fun robotHandler(id: Int, change: Change) {

        val parser = JsonParser()

        val valueParsed = parser.parse(change.value.get().formatAsString())
        val properties = valueParsed.asJsonObject.get("properties")

        if (properties.asJsonObject.has("conditionChange")) {
            val conditionChange = properties.asJsonObject.get("conditionChange")
            RobotHandler.addConditionChange(id, conditionChange.asDouble)
        }

        if (properties.asJsonObject.has("condition")) {
            val condition = properties.asJsonObject.get("condition")
            logger.info { "NEW CONDITION: $condition" }
            RobotHandler.addConditionToRobot(id, condition.asDouble)
        }

    }

    fun thingCreatedChange(change: Change) {
        val entityId = change.entityId
        val entitySplit = entityId.split(":")
        val idOfEntity = entitySplit[1].toInt()
        when (entitySplit[0]) {
            "robot" -> RobotHandler.addRobot(idOfEntity)
            "place" -> PlaceHandler.addPlaceToList(idOfEntity)
            "item" -> itemHandler(change)
            "workflow" -> WorkFlowHandler.addWorkFlowToList(idOfEntity)
        }
    }


}