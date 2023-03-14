package com.dungzi.backend.domain.chat.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dungzi.backend.domain.chat.dao.ChatMessageDao;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
import com.dungzi.backend.domain.chat.dto.ChatMessageRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.dungzi.backend.domain.user.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatRoomDao chatRoomDao;
    @Mock
    private ChatMessageDao chatMessageDao;


    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    @DisplayName("채팅메세지 저장 테스트-일반 메세지 타입")
    public void saveChatMessageTest() {
        //given
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(UUID.randomUUID())
                .build();
        ChatMessageRequestDto messageSendDto = ChatMessageRequestDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .content("Hello")
                .build();
        User sender = User.builder()
                .userId(UUID.randomUUID())
                .build();
        //when
        when(chatRoomDao.findById(messageSendDto.getChatRoomId())).thenReturn(Optional.of(chatRoom));
        ChatMessage savedChatMessage = ChatMessage.builder()
                .chatMessageId(1L)
                .chatRoom(chatRoom)
                .content(messageSendDto.getContent())
                .chatMessageType(ChatMessageType.MESSAGE)
                .sender(sender)
                .build();
        when(chatMessageDao.save(any(ChatMessage.class))).thenReturn(savedChatMessage);
        ChatMessage returnedChatMessage = chatMessageService.saveChatMessage(messageSendDto, sender);
        //then
        assertThat(returnedChatMessage).isEqualTo(savedChatMessage);
    }



    @Test
    @DisplayName("ChatMesageResponseDto")
    void findAllChatMessage() {
        //given
        ChatRoom chatRoom = ChatRoom.builder().chatRoomId(UUID.randomUUID()).build();
        when(chatRoomDao.findById(chatRoom.getChatRoomId())).thenReturn(Optional.of(chatRoom));

        List<ChatMessage> chatMessageList = new ArrayList<>();

        chatMessageList.add(ChatMessage.builder()
                .sender(User.builder().userId(UUID.randomUUID()).nickname("User1").build())
                .content("testContent")
                .chatMessageType(ChatMessageType.MESSAGE)
                .sendDate(LocalDateTime.now()).build());
        chatMessageList.add(ChatMessage.builder()
                .sender(User.builder().userId(UUID.randomUUID()).nickname("User2").build())
                .content("testContent2")
                .chatMessageType(ChatMessageType.MESSAGE)
                .sendDate(LocalDateTime.now().minusDays(1)).build());
        Page<ChatMessage> chatMessagePage = new PageImpl<>(chatMessageList);
        //가장 최근에 보낸 메세지만 가져오기(page 사용)
        Pageable pageable = PageRequest.of(0, 2);
        //when
        when(chatMessageDao.findByChatRoomOrderBySendDateDesc(chatRoom, pageable)).thenReturn(chatMessagePage);
        List<ChatMessageResponseDto> response = chatMessageService.findAllChatMessage(
                chatRoom.getChatRoomId().toString(), pageable);
        //then
        assertThat(2).isEqualTo(response.size());
        assertThat("User1").isEqualTo(response.get(0).getSender());
        assertThat("testContent").isEqualTo(response.get(0).getContent());
        assertThat(ChatMessageType.MESSAGE.getType()).isEqualTo(response.get(0).getMessageType());
    }

}