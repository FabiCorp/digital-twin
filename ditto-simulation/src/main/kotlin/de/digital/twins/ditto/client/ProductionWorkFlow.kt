package de.digital.twins.ditto.client

import mu.KotlinLogging

class ProductionWorkFlow (
        private var productionItem: ProductionItem,
        private var productionType: ProductionType
) {
    private val id = UniqueIdGenerator.generateUniqueWorkFlowId()
    private val productionPlaceList: MutableList<ProductionPlace> = mutableListOf()
    private val logger = KotlinLogging.logger{}

    private var stepCounter = 0
    var inProduction = true
    private var totalTime = 0.0

    init {
        GlobalProductionListService.productionItemList.add(productionItem)
        enumValues<ProductionStepType>().forEach { productionStepType ->
            val productionPlace = ProductionPlace(productionStepType)
            GlobalProductionListService.productionPlaceList.add(productionPlace)
            productionPlaceList.add(productionPlace)
        }

        if (productionType == ProductionType.RANDOM) {
            productionPlaceList.shuffle()
        }

        DittoClientService.createProductionWorkFlow(id)
    }

    fun processStep() {
        if (!inProduction) return
        totalTime += productionPlaceList[stepCounter].processStep(productionItem)

        logger.info { "${productionPlaceList.get(stepCounter).productionStepType} -> TotalTime: $totalTime" }
        DittoClientService.updateProductionWorkFlowFeature(id, "status", "totalTime", totalTime.toString())

        if (stepCounter < ProductionStepType.values().size - 1) {
            stepCounter++
        } else {
            inProduction = false
        }

    }

    fun reInitRandom() : ProductionItem {
        val oldProductionItem = productionItem
        productionItem = ProductionItem(productionItem.productType)
        GlobalProductionListService.productionItemList.add(productionItem)
        productionType = ProductionType.RANDOM
        stepCounter = 0
        inProduction = true
        totalTime = 0.0
        productionPlaceList.shuffle()
        return oldProductionItem
    }
}

enum class ProductionType {
    RANDOM,
    CUSTOM
}