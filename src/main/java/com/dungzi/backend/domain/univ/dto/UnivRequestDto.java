package com.dungzi.backend.domain.univ.dto;

import com.dungzi.backend.domain.univ.domain.Univ;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class UnivRequestDto {
    @Data
    public static class UnivInfo {
        @NotBlank
        private String univName;

        @Email
        @NotBlank
        private String emailDomain;

        public Univ toEntity() {
            return Univ.builder()
                    .univName(this.univName)
                    .emailDomain(this.emailDomain)
                    .build();
        }
    }
}
