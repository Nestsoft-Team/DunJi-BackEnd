package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.domain.RoomInfo;
import com.dungzi.backend.domain.room.domain.RoomOption;
import com.dungzi.backend.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
@Slf4j
public class RoomRequestDto {

    @Data
    @Builder
    public static class RegisterDto {
       // private String roomId;
        private String userId;
        private Room room;
        private String univId;
        private String title;
        private String content;
        private int image;
        private double longitude;
        private double latitude;
        private String address;
        private String addressDetail;
        private String sigungu;
        private String dong;
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
        private boolean electricity;
        private boolean gas;
        private boolean water;
        private boolean internet;
        private boolean tv;
        //private int heartNum;
        private String status;

        public Room toRoomEntity(User user) {
//            UUID uuid = null;
//            if(roomId != null){
//                uuid = UUID.fromString(roomId);
//            }

            return Room.builder()
//                    .roomId(uuid)
                    .user(user)
                    .title(title)
                    .content(content)
                    .image(image)
                    .heartNum(0)
                    .build();
        }

        public RoomAddress toAddressEntity() {
         //   log.info("[RoomAddress] roomId : {}", roomId);
            log.info("[RoomAddress] room : {}", room.getRoomId());
            return RoomAddress.builder()
           //         .roomId(roomId)
                    .room(room)
                    .longitude(longitude)
                    .latitude(latitude)
                    .address(address)
                    .addressDetail(addressDetail)
                    .sigungu(sigungu)
                    .dong(dong)
                    .status(status)
                    .build();
        }

        public RoomInfo toInfoEntity() {

            return RoomInfo.builder()
             //       .roomId(roomId)
                    .room(room)
//                    .startedAt(requestDto.getStartedAt())
//                    .finishedAt(requestDto.getFinishedAt())
//                    .tenancyAgreement(requestDto.isTenancyAgreement())
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

        public RoomOption toOptionEntity(String option, String utility) {
            return RoomOption.builder()
              //      .roomId(roomId)
                    .room(room)
                    .options(option)
                    .utility(utility)
                    .startedAt(startedAt)
                    .finishedAt(finishedAt)
                    .tenancyAgreement(tenancyAgreement)
                    //.advantage(advantage)
                    .build();
        }

    }




//    public Room toRoomEntity(RoomDto roomDto) {
//
//        UserDto userDto = null;
//        userDto.setUserId(roomDto.getUserId().toString());
//        User user = userDto.toEntity();
//
//        return Room.builder()
//                .roomId(roomDto.getRoomId())
//                .user(user)
////                .univId(requestDto.getUnivId())
//                .title(roomDto.getTitle())
//                .content(roomDto.getContent())
//                .image(roomDto.getImage())
////                .regDate(requestDto.getRegDate)
//                .delDate(roomDto.getDelDate())
//                .dealDate(roomDto.getDealDate())
//                .heartNum(roomDto.getHeartNum())
//                .build();
//    }

}
