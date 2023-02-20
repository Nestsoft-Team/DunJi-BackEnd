package com.dungzi.backend.domain.chat.Redis;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage chatMessage) {
        //TODO Could not write JSON: Java 8 date/time type 문제 해결
        redisTemplate.convertAndSend(topic.getTopic(), chatMessage);
    }
}