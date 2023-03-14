package com.dungzi.backend.domain.chat.dao.mongo;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageDao extends MongoRepository<ChatMessage, String> {
    Page<ChatMessage> findByChatRoomIdOrderByIdDesc(String chatRoomId, Pageable pageable);
}