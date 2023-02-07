package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.domain.room.dto.RoomAddressDto;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoomAddress extends BaseTimeEntity {
    @Id
    @Column(name = "roomId", nullable = false)
    private UUID roomId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "roomId")
    private Room room;

//    @OneToOne
//    @JoinColumn(name = "univId")
//    private Univ univ;

    private double longitude;
    private double latitude;
    private String address;
    private String addressDetail;
    private String sigungu;
    private String dong;

    public RoomAddressDto toRoomAddressDto() {

        return RoomAddressDto.builder()
                .roomId(this.getRoomId())
                .longitude(this.getLongitude())
                .latitude(this.getLatitude())
                .address(this.getAddress())
                .address(this.getAddressDetail())
                .sigungu(this.getSigungu())
                .dong(this.getDong())
                .build();
    }
}
