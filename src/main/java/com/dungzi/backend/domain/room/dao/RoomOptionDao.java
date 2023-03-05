package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.RoomOption;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomOptionDao extends JpaRepository<RoomOption, UUID> {
    Optional<RoomOption> findByRoomId(UUID roomId);
}
