package com.hjm7091.websocket.config.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.hjm7091.websocket.chat.domain.ChatMessage;
import com.hjm7091.websocket.chat.domain.MessageType;
import com.hjm7091.websocket.chat.repo.ChatRoomRepository;
import com.hjm7091.websocket.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatService chatService;
	
	/*
	 * webSocket을 통해 들어온 요청이 처리 되기 전에 먼저 실행됨
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		
		if(StompCommand.CONNECT == accessor.getCommand()) { // webSocket 연결 요청
			log.info("========webSocket 연결 요청======");
			log.info("헤더 정보 : {}", message.getHeaders());
			
		} else if(StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅방 구독 요청
			log.info("==========채팅룸 구독 요청=========");
			log.info("헤더 정보 : {}", message.getHeaders());
			
			//헤더 정보에서 구독 destination 정보를 얻고, roomId를 추출
			String roomId = chatService.getRoomId(Optional.
					ofNullable((String) message.getHeaders().get("simpDestination")).
					orElse("InvalidRoomId"));
			
			//헤더 정보에서 세션 id 추출
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			
			//채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
			chatRoomRepository.setUserEnterInfo(sessionId, roomId);
			
			//채팅방의 인원수를 +1한다.
			chatRoomRepository.plusUserCount(roomId);
			
			//유저 입장 메시지를 채팅방에 발송한다.(redis publish)
			String name = Optional.ofNullable(accessor.getNativeHeader("sender").get(0)).orElse("UnknownUser");
			chatService.sendChatMessage(ChatMessage.builder().type(MessageType.ENTER).roomId(roomId).sender(name).build());
			
			log.info("SUBSCRIBED {}, {}", name, roomId);
		} else if(StompCommand.DISCONNECT == accessor.getCommand()) { //webSocket 연결 종료
			log.info("========webSocket 종료 요청======");
			log.info("헤더 정보 : {}", message.getHeaders());
			
			//채팅방을 나갈 때 webSocket 연결 종료 요청이 두번 됨, 따라서 한 번은 null값이므로 그대로 사용하면 에러 발생 
			List<String> sender = accessor.getNativeHeader("sender");
			
			//sender가 null이면 아무 전처리 없이 그대로 반환
			if(sender == null)
				return message;
			
			//연결이 종료된 유저의 세션 id로 채팅방 id를 얻는다.
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
			
			//채팅방의 인원수를 -1한다.
			chatRoomRepository.minusUserCount(roomId);
			
			//유저 퇴장 메시지를 채팅방에 발송한다.(redis publish)
			String name = Optional.ofNullable(sender.get(0)).orElse("UnknownUser");
			chatService.sendChatMessage(ChatMessage.builder().type(MessageType.QUIT).roomId(roomId).sender(name).build());
			
			//유저의 세션 정보 삭제
			chatRoomRepository.removeUserEnterInfo(sessionId);
			
			log.info("DISCONNECTED {}, {}", sessionId, roomId);
		}
		
		return message;
	}
	
	
}
