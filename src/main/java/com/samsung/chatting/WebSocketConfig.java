package com.samsung.chatting;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.samsung.chatting.controllers.EchoHandler;

import lombok.AllArgsConstructor;

@EnableWebSocket
@AllArgsConstructor
@Configuration 
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final EchoHandler echoHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoHandler, "/echo")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}