package com.dungzi.backend.domain.room.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.UUID;

public class RoomResponseDto {

    @Builder
    @Data
    public static class RoomRegist {
        private UUID roomId;
    }

    @Builder
    @Data
    public static class RoomDetail {
        private UUID roomId;
        private UUID userId;
        private String univId;
        private String title;
        private String content;
        private int image;
        private double longitude;
        private double latitude;
        private String address;
        private String addressDetail;
        private String startedAt;
        private String finishedAt;
        private boolean tenancyAgreement;
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
        private boolean airConditioner;
        private boolean refrigerator;
        private boolean washingMachine;
        private boolean gasStove;
        private boolean induction;
        private boolean microwave;
        private boolean desk;
        private boolean bookcase;
        private boolean bed;
        private boolean closet;
        private boolean sink;
        private boolean shoeCabinet;
        private boolean elevators;
        private boolean parking;
        private boolean pets;
        private boolean womenOnly;
        private boolean loan;
        private String utility;

    }

    @Builder
    @Data
    public static class RoomList {
        private String roomId;
        private double longtitude;
        private double latitude;
        private String address;
        private int price;
        private String priceUnit;
        private int floor;
        private double roomSize;
        private int deposit;
        private String roomType;
        private String dealType;
    }
}
