package de.digital.twins.ditto.client

object UniqueIdGenerator {
    private var robotID = 0
    private var placeID = 0
    private var workflowID = 0
    private var itemID = 0

    fun generateUniqueRobotId() : Int {
        return robotID++
    }
    fun generateUniquePlaceId() : Int {
        return placeID++
    }
    fun generateUniqueWorkFlowId() : Int {
        return workflowID++
    }
    fun generateUniqueItemId() : Int {
        return itemID++
    }
}