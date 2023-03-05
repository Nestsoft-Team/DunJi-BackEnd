package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RoomDao extends JpaRepository<Room, UUID> {
    Optional<Room> findByRoomId(UUID roomId);

//    @Query("SELECT NEW com.example.dto.UserAddress")
//    Optional<Room> findRoomDetailByRoomId()
}
