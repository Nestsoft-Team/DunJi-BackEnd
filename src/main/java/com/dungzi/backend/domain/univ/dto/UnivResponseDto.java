package com.dungzi.backend.domain.univ.dto;

import com.dungzi.backend.domain.univ.domain.Univ;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnivResponseDto {

    @Builder
    @Data
    public static class UnivInfo {
        private String univId;
        private String univName;
        private String emailDomain;

        private static UnivInfo toDto(Univ univ) {
            return UnivInfo.builder()
                    .univId(univ.getUnivId())
                    .univName(univ.getUnivName())
                    .emailDomain(univ.getEmailDomain())
                    .build();
        }

        public static List<UnivInfo> toUnivInfoList(List<Univ> univList) {
            List<UnivInfo> univInfoList = new ArrayList<>();
            if(univList.size() != 0) {
                for (Univ univ : univList) {
                    univInfoList.add(toDto(univ));
                }
            }
            return univInfoList;
        }
    }
}
