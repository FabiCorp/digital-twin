package de.digital.twins.ditto.client

import mu.KotlinLogging
import org.eclipse.ditto.client.DittoClient
import org.eclipse.ditto.client.DittoClients
import org.eclipse.ditto.client.configuration.BasicAuthenticationConfiguration
import org.eclipse.ditto.client.configuration.WebSocketMessagingConfiguration
import org.eclipse.ditto.client.messaging.AuthenticationProviders
import org.eclipse.ditto.client.messaging.MessagingProvider
import org.eclipse.ditto.client.messaging.MessagingProviders
import org.eclipse.ditto.model.base.common.HttpStatusCode
import org.eclipse.ditto.model.base.json.JsonSchemaVersion
import org.eclipse.ditto.model.things.*
import org.springframework.stereotype.Component

@Component
object DittoClientService {

    private val dittoClient = initDittoClient()

    private val logger = KotlinLogging.logger{}

    private final fun initDittoClient() : DittoClient {

        val authenticationProvider = AuthenticationProviders.basic(BasicAuthenticationConfiguration.newBuilder()
                .username("ditto")
                .password("ditto")
                .build())

        val messagingProvider: MessagingProvider = MessagingProviders.webSocket(WebSocketMessagingConfiguration.newBuilder()
                .endpoint("ws://localhost:8080/")
                .jsonSchemaVersion(JsonSchemaVersion.V_2)
                .build(), authenticationProvider)

        return DittoClients.newInstance(messagingProvider)
    }

    fun startConsumption() {
        dittoClient.twin().startConsumption().get()
        dittoClient.live().startConsumption().get()
    }


    fun createProductionItem(id: Int) {
        val thingId = ThingId.of("item", id.toString())
        createThing(thingId)
    }

    fun createProductionPlace(id: Int) {
        val thingId = ThingId.of("place", id.toString())
        createThing(thingId)
        putInitProperty(thingId, "status", "time", "0.0")
        putInitProperty(thingId, "status", "stepList", "[]")
    }

    fun createProductionWorkFlow(id: Int) {
        val thingId = ThingId.of("workflow", id.toString())
        createThing(thingId)
        putInitProperty(thingId, "status", "totalTime", "0.0")
    }

    fun createProductionRobot(id: Int, condition: String, productivity: String) {
        val thingId = ThingId.of("robot", id.toString())
        createThing(thingId)
        putInitProperty(thingId, "status", "condition", condition)
        putInitProperty(thingId, "status", "productivity", productivity)
    }

    fun updateProductionWorkFlowFeature(id: Int, feature: String, property: String, value: String) {
        val thingId = ThingId.of("workflow", id.toString())
        putInitProperty(thingId, feature, property, value)
    }

    fun updateProductionPlaceFeature(id: Int, feature: String, property: String, value: String) {
        val thingId = ThingId.of("place", id.toString())
        putInitProperty(thingId, feature, property, value)
    }

    fun updateProductionItemFeature(id: Int, feature: String, property: String, value: String) {
        val thingId = ThingId.of("item", id.toString())
        putInitProperty(thingId, feature, property, value)
    }

    fun updateProductionRobotFeature(id: Int, feature: String, property: String, value: String) {
        val thingId = ThingId.of("robot", id.toString())
        putInitProperty(thingId, feature, property, value)
    }

    private fun putInitProperty(thingId: ThingId, feature: String, property: String, value: String) {
        val features = ThingsModelFactory.newFeatureBuilder()
                .properties(ThingsModelFactory.newFeaturePropertiesBuilder()
                        .set(property, value)
                        .build())
                .withId(feature)
                .build()

        dittoClient.twin().forId(thingId).putFeature(features)
                .whenComplete { thing, throwable ->
                    if (throwable != null) {
                        logger.error("Received error when putting the property: {}",
                                throwable.message, throwable);
                    } else {
                        logger.debug("Putting the property: $property at feature: $feature with value: $value succeeded");
                    }
                }
                .get()
    }

    private fun createThing(thingId: ThingId) {

        val demoThing = Thing.newBuilder().setId(thingId).build()

        val result = dittoClient
            .twin()
            .create(demoThing)
            .whenComplete { thing, throwable ->
                whenCompleteLambda(thing, throwable, thingId)
            }
            .get()

        logger.debug { "createThing: Created new thing: $result" }
    }

    fun receiveRepairMessages(id: Int, repairRobot: () -> Unit) {
        val subject = "robot.repair.${id.toString()}"

        dittoClient
                .live()
                .registerForMessage<String>("globalMessageHandler${id.toString()}", subject) { message ->
                    message.reply().statusCode(HttpStatusCode.IM_A_TEAPOT).payload("Start to repair").send()
                    repairRobot()
                }
    }

    fun receiveIncreaseConditionMessages(id: Int, increaseCondition: () -> Unit) {
        val subject = "robot.condition.${id.toString()}"

        dittoClient
                .live()
                .registerForMessage<String>("globalMessageHandler${id.toString()}", subject) { message ->
                    println("Received Message with subject ${message.subject}")
                    message.reply().statusCode(HttpStatusCode.IM_A_TEAPOT).payload("Start to repair").send()
                    increaseCondition()
                }
    }

    private fun whenCompleteLambda(thing: Thing, throwable: Throwable?, thingId: ThingId) {
        return when {
            throwable != null -> {
                logger.error { "[AT BACKEND] Received error when creating the thing. $throwable" }
            }

            thing.entityId.filter(thingId::equals).isPresent -> {
                logger.debug { "[AT BACKEND] Successfully created live Thing and got response: {$thing}" }
            }

            else -> {
                logger.warn { "[AT BACKEND] Received unexpected thing {$thing}." }
            }
        }
    }

}
