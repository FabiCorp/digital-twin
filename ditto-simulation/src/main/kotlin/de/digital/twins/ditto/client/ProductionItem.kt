package de.digital.twins.ditto.client

class ProductionItem (
        val productType: ProductType
) {
    private val id = UniqueIdGenerator.generateUniqueItemId()
    val productionStepList: MutableList<ProductionStepType> = mutableListOf()

    init {
        // DittoClientService.createProductionItem(id)
    }

    fun addProductionStepToList (productionStepType: ProductionStepType) {
        productionStepList.add(productionStepType)
    }
}