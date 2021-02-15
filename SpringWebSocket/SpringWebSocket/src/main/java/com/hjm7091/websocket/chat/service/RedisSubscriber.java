package com.hjm7091.websocket.chat.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjm7091.websocket.chat.domain.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;
	
	/*
	 * Redis에서 메시지가 발행되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리
	 */
	public void sendMessage(String publishMessage) {
		try {
			//ChatMessage 객체로 매핑
			ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
			
			//채팅방을 구독중인 유저에게 채팅 메시지 전송 
			messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
	}

}
