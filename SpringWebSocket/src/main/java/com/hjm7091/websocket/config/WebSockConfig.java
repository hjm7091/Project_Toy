package com.hjm7091.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.hjm7091.websocket.config.handler.StompHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker //Stomp를 사용하기 위한 설정 
@RequiredArgsConstructor
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {
	
	private final StompHandler stompHandler;
	
	/*
	 * 메세지를 발행하는 요청의 prefix는 /pub로 시작하도록 설정
	 * 메세지를 구독하는 요청의 prefix는 /sub로 시작하도록 설정
	 */
	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes("/pub");
        config.enableSimpleBroker("/sub");
    }
	
	/*
	 * Stomp WebSocket 연결의 endpoint를 /ws-stomp로 설정
	 * setAllowedOrigins를 사용하여 cross-origin HTTP 요청 허가
	 * withSockJS를 사용하여 SockJS를 사용 할 수 있도록 설정
	 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
    }
    
    /*
	 * StompHandler를 인터셉터로 설정
	 */
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}

}
