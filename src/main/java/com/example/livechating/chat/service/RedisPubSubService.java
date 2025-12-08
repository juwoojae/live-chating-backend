package com.example.livechating.chat.service;

import com.example.livechating.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
public class RedisPubSubService implements MessageListener {

    private final StringRedisTemplate stringRedisTemplate;
    private final SimpMessageSendingOperations messageTemplate;

    // 서버 단의 messageMapping 으로 받은후, redis 로 다시 쏴주는 과정
    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    // pattern 에는 topic 의 이름의 패턴이 담겨있고, 이 패턴을 기반으로 다이나믹한 코딩이 가능하다.
    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {

        String payload = new String(message.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
        messageTemplate.convertAndSend("/topic/" + chatMessageDto.getRoomId(), chatMessageDto);
    }
}
