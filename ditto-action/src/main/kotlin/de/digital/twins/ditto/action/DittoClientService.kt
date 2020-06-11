package de.digital.twins.ditto.action


import mu.KotlinLogging
import org.eclipse.ditto.client.DittoClient
import org.eclipse.ditto.client.DittoClients
import org.eclipse.ditto.client.configuration.BasicAuthenticationConfiguration
import org.eclipse.ditto.client.configuration.WebSocketMessagingConfiguration
import org.eclipse.ditto.client.messaging.AuthenticationProviders
import org.eclipse.ditto.client.messaging.MessagingProvider
import org.eclipse.ditto.client.messaging.MessagingProviders
import org.eclipse.ditto.model.base.json.JsonSchemaVersion
import org.eclipse.ditto.model.things.ThingId
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

    fun subscribeForChanges() {

        logger.info { "Subscribed for Twin events" }

        dittoClient.twin().registerForThingChanges("globalThingHandler") { change ->
            if (change.isFull) {
                logger.info { "Received full Thing change: $change" }
                ChangeService.thingCreatedChange(change)
            } else {
                logger.debug { "Received Thing change: $change" }
            }
        }

        dittoClient.twin().registerForFeatureChanges("globalFeatureHandler") { change ->
            if (change.isFull) {
                logger.info { "Received full Feature change: $change" }
            } else {
                logger.debug { "Received Feature change: $change" }
            }
            ChangeService.evaluateChange(change)
        }

        dittoClient.twin().registerForAttributesChanges("globalAttributeHandler") { change ->
            if (change.isFull) {
                logger.debug { "Received full Attribute change: $change" }
            } else {
                logger.debug { "Received Attribute change: $change" }
            }
        }

    }

    fun sendRobotRepairMessage(id: Int) {
        val thingId = ThingId.of("robot", id.toString())
        val subject = "robot.repair.$id"
        sendMessage(thingId, subject, "Repair Robot")
    }

    fun sendIncreaseConditionMessage(id: Int) {
        val thingId = ThingId.of("robot", id.toString())
        val subject = "robot.condition.$id"
        sendMessage(thingId, subject, "Increase Condition")
    }

    private fun sendMessage(thingId: ThingId, subject: String, payload: String) {
        dittoClient
                .live()
                .forId(thingId)
                .message<String>()
                .from()
                .subject(subject)
                .payload(payload)
                .send(String::class.java) { _, _ -> }
    }


}
