package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.RoomInfo;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomInfoDao extends JpaRepository<RoomInfo, UUID> {
    Optional<RoomInfo> findByRoomId(UUID roomId);
}
