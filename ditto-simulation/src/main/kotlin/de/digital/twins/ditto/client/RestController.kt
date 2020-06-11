package de.digital.twins.ditto.client

import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {

    @GetMapping("/process")
    fun processStep() {
        ProductionManager.processStep()
    }

    @GetMapping("/init")
    fun test() {
        DittoClientService.startConsumption()
    }

}
