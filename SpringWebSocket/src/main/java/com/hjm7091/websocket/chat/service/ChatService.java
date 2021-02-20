package com.hjm7091.websocket.chat.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.hjm7091.websocket.chat.domain.ChatMessage;
import com.hjm7091.websocket.chat.domain.MessageType;
import com.hjm7091.websocket.chat.repo.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
	
	private final ChannelTopic channelTopic;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	
	/*
	 * destination 정보에서 roomId 추출
	 */
	public String getRoomId(String destination) {
		int lastIdx = destination.lastIndexOf('/');
		if(lastIdx != -1)
			return destination.substring(lastIdx + 1);
		else
			return "";
	}
	
	/*
	 * 채팅방에 메시지 발송
	 */
	public void sendChatMessage(ChatMessage chatMessage) {
		chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
		
		if(MessageType.ENTER.equals(chatMessage.getType())) {
			chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
			chatMessage.setSender("[알림]");
		} else if(MessageType.QUIT.equals(chatMessage.getType())) {
			chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다.");
			chatMessage.setSender("[알림]");
		}
		
		//redis template으로 메시지 발행 
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
	}
	
}
