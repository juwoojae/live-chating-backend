package com.example.livechating.chat.repository;

import com.example.livechating.chat.domain.ChatParticipant;
import com.example.livechating.chat.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
}
