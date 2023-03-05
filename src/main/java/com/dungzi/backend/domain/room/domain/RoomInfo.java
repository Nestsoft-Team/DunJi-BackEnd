package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoomInfo extends BaseTimeEntity {

    @Id
    @Column(name = "room_id", nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "room_id")
    private Room room;

//    private status;

//    private String startedAt;
//    private String finishedAt;
//    private boolean tenancyAgreement;
    private double roomSize;
    private int totalFloor;
    private int floor;
    private String structure;
    private String roomType;
    private String dealType;
    private int deposit;
    private int price;
    private String priceUnit;
    private int managementCost;
    private boolean fullOption;
    private boolean elevators;
    private boolean parking;
    private boolean pets;
    private boolean womenOnly;
    private boolean loan;

}
