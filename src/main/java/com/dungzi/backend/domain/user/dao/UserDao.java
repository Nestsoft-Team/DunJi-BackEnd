package com.dungzi.backend.domain.user.dao;

import com.dungzi.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, UUID> {
    Optional<User> findByUserId(UUID userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}