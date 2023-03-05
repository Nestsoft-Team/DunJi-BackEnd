package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.RoomInfo;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class RoomInfoDto {

    private UUID roomId;
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

    public RoomInfo toEntity(UUID roomId) {

        return RoomInfo.builder()
          //      .roomId(roomId)
//                .startedAt(startedAt)
//                .finishedAt(finishedAt)
//                .tenancyAgreement(tenancyAgreement)
                .roomSize(roomSize)
                .totalFloor(totalFloor)
                .floor(floor)
                .structure(structure)
                .roomType(roomType)
                .dealType(dealType)
                .deposit(deposit)
                .price(price)
                .priceUnit(priceUnit)
                .managementCost(managementCost)
                .fullOption(fullOption)
                .elevators(elevators)
                .parking(parking)
                .pets(pets)
                .womenOnly(womenOnly)
                .loan(loan)
                .build();
    }
}
