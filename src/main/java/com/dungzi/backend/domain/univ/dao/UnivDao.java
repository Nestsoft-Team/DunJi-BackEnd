package com.dungzi.backend.domain.univ.dao;

import com.dungzi.backend.domain.univ.domain.Univ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnivDao extends JpaRepository<Univ, String> {
}
