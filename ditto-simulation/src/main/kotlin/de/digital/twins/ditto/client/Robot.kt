package de.digital.twins.ditto.client

import mu.KotlinLogging
import kotlin.random.Random.Default.nextDouble

data class Robot(
        @Volatile var condition: Double,
        @Volatile var productivity: Double,
        var isDefect: Boolean
) {
    private val id = UniqueIdGenerator.generateUniqueRobotId()
    private val logger = KotlinLogging.logger{}


    init {
        DittoClientService.createProductionRobot(id, condition.toString(), productivity.toString())
        DittoClientService.receiveRepairMessages(id, ({ repairRobot() }))
        DittoClientService.receiveIncreaseConditionMessages(id + 1000, ({ increaseCondition() }))
    }

    private fun conditionUpdate() {
        val maxConditionDecrease = if (isDefect) 5.0 else 3.0
        val conditionChange = nextDouble(1.0, maxConditionDecrease)
        condition -= conditionChange
        DittoClientService.updateProductionRobotFeature(id, "status", "conditionChange", conditionChange.toString())
        DittoClientService.updateProductionRobotFeature(id, "status", "condition", condition.toString())
    }

    fun getWorkDone() : Double {
        val workTime = 1.0 * (100.0 / condition)
        conditionUpdate()
        return workTime
    }

    private fun repairRobot() {
        logger.info { "Robot with ID $id repaired" }
        condition = 100.0
        isDefect = false
        DittoClientService.updateProductionRobotFeature(id, "status", "condition", condition.toString())
    }

    private fun increaseCondition() {
        condition = 100.0
        DittoClientService.updateProductionRobotFeature(id, "status", "condition", condition.toString())
    }

}

