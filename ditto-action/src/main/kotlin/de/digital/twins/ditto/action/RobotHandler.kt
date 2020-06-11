package de.digital.twins.ditto.action

import mu.KotlinLogging

object RobotHandler {

    private val logger = KotlinLogging.logger{}
    private val robotMutableList: MutableList<Robot> = mutableListOf()
    private val conditionChangeMap: MutableMap<Robot, Double> = mutableMapOf()
    private val averageConditionChangeList: MutableList<Double> = mutableListOf()
    private val repairedRobotList = mutableListOf<Robot>()

    @Volatile private var processBorder = 10
    @Volatile private var processBorderIncrease = 10

    fun addRobot(id: Int) {
        robotMutableList.add(Robot(id))
    }

    fun addConditionChange(id: Int, conditionChangeValue: Double) {
        val robot = robotMutableList.filter { robot -> robot.id == id }
        conditionChangeMap[robot.first()] = conditionChangeValue

        if (conditionChangeMap.size > processBorder) {
            if (processBorder < conditionChangeMap.size) {
                processBorder += processBorderIncrease
            } else {
                processBorder = 0
            }

            processConditionChangeInformation()
        }
    }

    fun addConditionToRobot(id: Int, condition: Double) {
        val robot = robotMutableList.filter { robot -> robot.id == id }
        robot.first().conditionList.add(condition)
    }

    fun returnRobotSize() : Int {
        return robotMutableList.size
    }

    fun returnRepairedRobotSize() : Int {
        return repairedRobotList.size
    }

    fun returnRobotConditionListById(id: Int) : MutableList<Double> {
        val robot = robotMutableList.filter { robot -> robot.id == id }
        return robot.first().conditionList
    }

    fun returnAverageConditionChangeList() : MutableList<Double> {
        return averageConditionChangeList
    }

    private fun processConditionChangeInformation() {
        val averageConditionChange = calculateAverageCondition()
        val currentConditionChange = conditionChangeMap.toMutableMap()
        val conditionChangeIterator = currentConditionChange.iterator()
        for (entry in conditionChangeIterator) {
            if (averageConditionChange + 1.5 < entry.value) {
                DittoClientService.sendRobotRepairMessage(entry.key.id)
                conditionChangeMap.remove(entry.key)
                repairedRobotList.add(entry.key)
            }
        }
        currentConditionChange.forEach {
            if (averageConditionChange + 1.5 < it.value) {
                DittoClientService.sendRobotRepairMessage(it.key.id)
            }
        }
    }

    private fun calculateAverageCondition() : Double {
        var totalConditionChange = 0.0
        conditionChangeMap.forEach {
            totalConditionChange += it.value
        }
        val averageConditionChange = totalConditionChange / conditionChangeMap.size
        averageConditionChangeList.add(averageConditionChange)
        logger.info { "Average Condition Change: $averageConditionChange" }
        return averageConditionChange
    }

    fun checkProductivity(id: Int, condition: Double) {
        val robot = robotMutableList[id]
        val increaseProdRobotList: MutableList<Int> = mutableListOf()

        if (condition == 100.0) {
            increaseProdRobotList.add(robot.id)
        }

    }

    fun checkCondition(id: Int, condition: Double) {
        val robot = robotMutableList[id]

        if (condition <= 50.0) {
            DittoClientService.sendIncreaseConditionMessage(robot.id)
        }


    }

}