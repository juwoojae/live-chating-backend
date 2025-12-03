package com.example.livechating.chat.service;


import com.example.livechating.chat.domain.ChatMessage;
import com.example.livechating.chat.domain.ChatParticipant;
import com.example.livechating.chat.domain.ChatRoom;
import com.example.livechating.chat.domain.ReadStatus;
import com.example.livechating.chat.dto.ChatMessageReqDto;
import com.example.livechating.chat.repository.ChatMessageRepository;
import com.example.livechating.chat.repository.ChatParticipantRepositorty;
import com.example.livechating.chat.repository.ChatRoomRepository;
import com.example.livechating.chat.repository.ReadStatusRepository;
import com.example.livechating.member.domain.Member;
import com.example.livechating.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatParticipantRepositorty  chatParticipantRepositorty;

    private final ChatMessageRepository chatMessageRepository;

    private final ReadStatusRepository readStatusRepository;

    private final MemberRepository memberRepository;

    public void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto) {
        //      채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new EntityNotFoundException("해당 room 이 없습니다."));
        //      보낸 사람 조회
        Member sender = memberRepository.findByEmail(chatMessageReqDto.getSenderEmail()).orElseThrow(
                () -> new EntityNotFoundException("해당 member 이 없습니다."));
        //      메세지 저장
        ChatMessage chatmessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(sender)  //누가 보낸 메세지인지
                .content(chatMessageReqDto.getMessage())
                .build();

        //      사용자별로 읽음 여부 저장
        List<ChatParticipant> chatParticipants = chatParticipantRepositorty.findByChatRoom(chatRoom);
        for(ChatParticipant c : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .member(c.getMember())
                    .chatMessage(chatmessage)
                    .isRead(c.getMember().equals(sender))   //보낸 사람에 한해서는 무조건 true
                    .build();
            readStatusRepository.save(readStatus);
        }
    }
}
