package de.digital.twins.ditto.action

object WorkFlowHandler {
    private val workFlowList = mutableListOf<WorkFlow>()

    fun getWorkFlowTotalTimeListById(id: Int): MutableList<Double> {
        val workFlow = workFlowList.filter { workFlow -> workFlow.id == id }
        return workFlow.first().totalTimeList
    }

    fun addWorkFlowToList(id: Int) {
        workFlowList.add(WorkFlow(id))
    }

    fun returnWorkFlowSize(): Int {
        return workFlowList.size
    }

    fun addTotalTimeToWorkFlow(id: Int, totalTime: Double) {
        val workFlow = workFlowList.filter { robot -> robot.id == id }
        workFlow.first().totalTimeList.add(totalTime)
    }
}