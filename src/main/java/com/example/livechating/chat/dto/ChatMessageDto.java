package com.example.livechating.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

    private Long roomId;
    private String message;
    private String senderEmail;

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
