package de.digital.twins.ditto.client

import kotlin.random.Random.Default.nextInt

class ProductionPlace (
    val productionStepType: ProductionStepType
) {
    private val id = UniqueIdGenerator.generateUniquePlaceId()
    private val robotList: MutableList<Robot> = mutableListOf()

    init {
        var isDefect = false
        for (index in 0..4) {
            if (nextInt(0, 100) >= 50) {
               isDefect = true
            }
        val robot = Robot(100.0, 100.0, isDefect)
        GlobalProductionListService.productionRobotList.add(robot)
        robotList.add(robot)
        }
        DittoClientService.createProductionPlace(id)
    }

    fun processStep(productionItem: ProductionItem) : Double {
        var time = 0.0
        robotList.forEach { robot ->
            time += robot.getWorkDone()
        }

        if (productionItem.productionStepList.size > 0) {
            time *= calculateProductionStepTime(productionItem.productionStepList)
        }

        productionItem.addProductionStepToList(productionStepType)
        return time
    }

    private fun calculateProductionStepTime(productionStepList: MutableList<ProductionStepType>) : Double {
        var time = 0.0
        productionStepList.forEach() { productionStepTypeOfList ->
            time += ProductionTime.productTimeMap[productionStepType]?.get(productionStepTypeOfList) ?: error("")
        }
        return time;
    }
}