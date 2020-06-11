package de.digital.twins.ditto.client

object ProductionTime {

    private val motherboardStep: Map<ProductionStepType, Double> = mapOf(
            ProductionStepType.PowerSupplyStep to 1.0,
            ProductionStepType.ConnectPowerStep to 15.0,
            ProductionStepType.CpuStep to 1.0,
            ProductionStepType.RamStep to 1.0,
            ProductionStepType.GraphicsCardStep to 5.0,
            ProductionStepType.MemoryStep to 1.0
    )

    private val powerSupplyStep: Map<ProductionStepType, Double> = mapOf(
            ProductionStepType.MotherboardStep to 1.0,
            ProductionStepType.ConnectPowerStep to 10.0,
            ProductionStepType.CpuStep to 1.0,
            ProductionStepType.RamStep to 1.0,
            ProductionStepType.GraphicsCardStep to 1.0,
            ProductionStepType.MemoryStep to 1.0
    )
    private val cpuStep: Map<ProductionStepType, Double> = mapOf(
            ProductionStepType.PowerSupplyStep to 1.0,
            ProductionStepType.ConnectPowerStep to 1.0,
            ProductionStepType.MotherboardStep to 5.0,
            ProductionStepType.RamStep to 1.0,
            ProductionStepType.GraphicsCardStep to 1.0,
            ProductionStepType.MemoryStep to 1.0
    )
    private val ramStep: Map<ProductionStepType, Double> = mapOf(
            ProductionStepType.PowerSupplyStep to 1.0,
            ProductionStepType.ConnectPowerStep to 1.0,
            ProductionStepType.CpuStep to 1.0,
            ProductionStepType.MotherboardStep to 5.0,
            ProductionStepType.GraphicsCardStep to 2.0,
            ProductionStepType.MemoryStep to 1.0
    )
    private val connectPowerStep: Map<ProductionStepType, Double> = mapOf(
            ProductionStepType.PowerSupplyStep to 1.0,
            ProductionStepType.MotherboardStep to 2.0,
            ProductionStepType.CpuStep to 1.0,
            ProductionStepType.RamStep to 1.0,
            ProductionStepType.GraphicsCardStep to 1.0,
            ProductionStepType.MemoryStep to 1.0
    )
    private val graphicsCardStep: Map<ProductionStepType, Double> = mapOf(
            ProductionStepType.PowerSupplyStep to 1.0,
            ProductionStepType.ConnectPowerStep to 1.0,
            ProductionStepType.CpuStep to 1.0,
            ProductionStepType.RamStep to 1.0,
            ProductionStepType.MotherboardStep to 2.0,
            ProductionStepType.MemoryStep to 1.0
    )
    private val memoryStep: Map<ProductionStepType, Double> = mapOf(
            ProductionStepType.PowerSupplyStep to 1.0,
            ProductionStepType.ConnectPowerStep to 1.0,
            ProductionStepType.CpuStep to 3.0,
            ProductionStepType.RamStep to 1.0,
            ProductionStepType.GraphicsCardStep to 4.0,
            ProductionStepType.MotherboardStep to 5.0
    )

     val productTimeMap = mapOf(
            ProductionStepType.MotherboardStep to motherboardStep,
            ProductionStepType.ConnectPowerStep to connectPowerStep,
            ProductionStepType.MemoryStep to memoryStep,
            ProductionStepType.GraphicsCardStep to graphicsCardStep,
            ProductionStepType.CpuStep to cpuStep,
            ProductionStepType.RamStep to ramStep,
            ProductionStepType.PowerSupplyStep to powerSupplyStep
    )

}