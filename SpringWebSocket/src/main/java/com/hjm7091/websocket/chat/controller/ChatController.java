package com.hjm7091.websocket.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.hjm7091.websocket.chat.domain.ChatMessage;
import com.hjm7091.websocket.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final ChatService chatService;
	
	/*
	 * "/pub/chat/message"로 들어오는 메시지 처리
	 */
	@MessageMapping("/chat/message")
    public void message(ChatMessage message) {
		
		//webSocket에 발행된 메시지를 redis로 발행
		chatService.sendChatMessage(message);
		
    }
	
}
