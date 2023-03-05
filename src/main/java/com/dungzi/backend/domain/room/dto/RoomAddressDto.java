package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.RoomAddress;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoomAddressDto {

    private UUID roomId;
    private double longitude;
    private double latitude;
    private String address;
    private String addressDetail;
    private String sigungu;
    private String dong;

    public RoomAddress toEntity(UUID roomId) {

        return RoomAddress.builder()
                //.roomId(roomId)
                .longitude(longitude)
                .latitude(latitude)
                .address(address)
                .address(addressDetail)
                .sigungu(sigungu)
                .dong(dong)
                .build();
    }
}
