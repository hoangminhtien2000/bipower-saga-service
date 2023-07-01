package com.biplus.saga.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties({SocketBrokerConfiguration.SocketBrokerConfigurationProperties.class})
public class SocketBrokerConfiguration implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private SocketBrokerConfigurationProperties properties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/saga-ws");
        config.setUserDestinationPrefix("/saga-ws/user");
        if (properties.brokerType == BrokerType.SIMPLE){
            config.enableSimpleBroker("/topic", "/queue");
        } else {
            config.enableStompBrokerRelay("/topic", "/queue")
                    .setUserDestinationBroadcast("/topic/unresolved.user.dest")
                    .setUserRegistryBroadcast("/topic/registry.broadcast")
                    .setRelayHost(properties.relayHost)
                    .setRelayPort(properties.relayPort);
        }
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/saga-ws").setAllowedOrigins("*")
                .withSockJS();
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "websocket")
    public static class SocketBrokerConfigurationProperties {
        private BrokerType brokerType = BrokerType.RELAY;
        private String relayHost = "localhost";
        private int relayPort = 61613;
    }

    public static enum BrokerType{
        SIMPLE, RELAY
    }
}

