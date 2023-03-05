package com.dungzi.backend.domain.univ.dao;

import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UnivAuthDao extends JpaRepository<UnivAuth, UUID> {
    Optional<UnivAuth> findByUser(User user);
}
