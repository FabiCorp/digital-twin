package de.digital.twins.ditto.action

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {

    @GetMapping("/test")
    fun testRest() {
        DittoClientService.sendRobotRepairMessage(7)
    }

    @GetMapping("/init")
    fun initServer() {
        DittoClientService.startConsumption()
        DittoClientService.subscribeForChanges()
    }

    @GetMapping("/averageCondition")
    fun returnAverageConditionList(): MutableList<Double> {
        return RobotHandler.returnAverageConditionChangeList()
    }

    @GetMapping("/totalTimeList/{id}")
    fun returnTotalTimeListForId(@PathVariable id   : Int): MutableList<Double> {
        return WorkFlowHandler.getWorkFlowTotalTimeListById(id)
    }

    @GetMapping("/robotCondition/{id}")
    fun returnRobotConditionForId(@PathVariable id: Int): MutableList<Double> {
        return RobotHandler.returnRobotConditionListById(id)
    }

    @GetMapping("/repairedRobots")
    fun returnRepairedRobots(): Int {
        return RobotHandler.returnRepairedRobotSize()
    }

    @GetMapping("/robot/size")
    fun returnRobotSize(): Int {
        return RobotHandler.returnRobotSize()
    }

    @GetMapping("/place/size")
    fun returnPlaceSize(): Int {
        return PlaceHandler.returnPlaceSize()
    }

    @GetMapping("/workflow/size")
    fun returnWorkFlowSize(): Int {
        return WorkFlowHandler.returnWorkFlowSize()
    }
}
