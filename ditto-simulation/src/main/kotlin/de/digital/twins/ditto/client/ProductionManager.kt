package de.digital.twins.ditto.client

import mu.KotlinLogging

object ProductionManager {

    private val logger = KotlinLogging.logger{}
    private val productionWorkFlowList: MutableList<ProductionWorkFlow> = mutableListOf()
    private val productionItemFinishedList: MutableList<ProductionItem> = mutableListOf()

    init {
        for (index in 0..1) {
            val productionWorkFlow = generateProductionWorkFlow()
            GlobalProductionListService.productionWorkFlowList.add(productionWorkFlow)
            productionWorkFlowList.add(productionWorkFlow)
        }
        DittoClientService.startConsumption()
    }

    fun processStep() {
        productionWorkFlowList.forEach { productionWorkFlow ->
            if (productionWorkFlow.inProduction) {
                productionWorkFlow.processStep()
            } else {
                logger.info { "Production finished for $productionWorkFlow" }
                productionItemFinishedList.add(productionWorkFlow.reInitRandom())
                productionWorkFlow.processStep()
            }
        }
    }

    private fun generateProductionWorkFlow(): ProductionWorkFlow {
        return ProductionWorkFlow(ProductionItem(ProductType.BusinessDesktopPc), ProductionType.RANDOM)
    }



}