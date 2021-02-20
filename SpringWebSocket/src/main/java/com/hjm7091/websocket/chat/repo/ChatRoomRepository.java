package com.hjm7091.websocket.chat.repo;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.hjm7091.websocket.chat.domain.ChatRoom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
	
	//redis cache key
	private static final String CHAT_ROOM = "CHAT_ROOM"; //채팅방 저장
	private static final String USER_COUNT = "USER_COUNT"; //채팅방에 입장한 클라이언트 수 저장
	private static final String ENTER_INFO = "ENTER_INFO"; //채팅방에 입장한 클라이언트의 세션 id와 채팅방의 id를 매핑한 정보 저장
	
	private final RedisTemplate<String, Object> redisTemplate;
	
	//첫번째 키는 CHAT_ROOM, 두번째 키는 roomId, value는 채팅방
	private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
	
	//첫번째 키는 ENTER_INFO, 두번째 키는 유저 세션 id, value는 roomId
	private HashOperations<String, String, String> hashOpsEnterInfo;
	
	//첫번째 키는 USER_COUNT, values는 유저 수 
	private ValueOperations<String, Object> valueOpsUserCount;

    @PostConstruct
    private void init() {
    	hashOpsChatRoom = redisTemplate.opsForHash();
    	hashOpsEnterInfo = redisTemplate.opsForHash();
    	valueOpsUserCount = redisTemplate.opsForValue();
    }
    
    //모든 채팅방 조회
    public List<ChatRoom> findAllRoom() {
        return hashOpsChatRoom.values(CHAT_ROOM);
    }
    
    //특정 채팅방 조회
    public ChatRoom findRoomById(String id) {
        return hashOpsChatRoom.get(CHAT_ROOM, id);
    }
    
    //채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장 
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        hashOpsChatRoom.put(CHAT_ROOM, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
	
	//유저가 입장한 채팅방 id와 유저  세션 id 매핑 정보 저장
	public void setUserEnterInfo(String sessionId, String roomId) {
		hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
	}
	
	//유저 세션으로 입장해 있는 채팅방 ID 조회
	public String getUserEnterRoomId(String sessionId) {
		return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
	}
	
	//유저 세션 id와 매핑되어 있는 roomId 삭제
	public void removeUserEnterInfo(String sessionId) {
		hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
	}
	
	//채팅방 유저 수 조회
	public long getUserCount(String roomId) {
		return Long.valueOf((String) Optional.ofNullable(valueOpsUserCount.get(USER_COUNT + "_" + roomId)).orElse("0"));
	}
	
	//채팅방에 입장한 유저 수 증가
	public long plusUserCount(String roomId) {
		return Optional.ofNullable(valueOpsUserCount.increment(USER_COUNT + "_" + roomId)).orElse(0L);
	}
	
	//채팅방에 입장한 유저 수 감소
	public long minusUserCount(String roomId) {
		return Optional.ofNullable(valueOpsUserCount.decrement(USER_COUNT + "_" + roomId)).filter(cnt -> cnt > 0).orElse(0L);
	}

}
