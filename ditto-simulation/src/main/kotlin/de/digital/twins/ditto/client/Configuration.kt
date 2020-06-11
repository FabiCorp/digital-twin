package de.digital.twins.ditto.client

import org.eclipse.ditto.client.DittoClient
import org.eclipse.ditto.client.DittoClients
import org.eclipse.ditto.client.configuration.BasicAuthenticationConfiguration
import org.eclipse.ditto.client.configuration.ProxyConfiguration
import org.eclipse.ditto.client.configuration.WebSocketMessagingConfiguration
import org.eclipse.ditto.client.messaging.AuthenticationProviders
import org.eclipse.ditto.client.messaging.MessagingProvider
import org.eclipse.ditto.client.messaging.MessagingProviders
import org.eclipse.ditto.model.base.json.JsonSchemaVersion
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class Configuration {

}