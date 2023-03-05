package com.dungzi.backend.domain.room.dto.enumType;

public class type {

    public enum dealType {
        shortTransfer("단기양도"),
        transfer("양도"),
        rental("임대");

        private String type;
        dealType(String type) {
            this.type = type;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return type;
        }
    }

    public enum roomType {
        oneRoom("원룸"),
        twoRoom("투룸"),
        threeRoom("쓰리룸");

        private String type;
        roomType(String type) {
            this.type = type;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return type;
        }
    }

    public enum structureType {
        openType("오픈형"),
        separateType("분리형"),
        multipleType("복층형");

        private String type;
        structureType(String type) {
            this.type = type;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return type;
        }
    }

    public enum roomStatus {
        deleted("삭제됨"),
        active("거래가능"),
        transacted("거래완료"),
        reported("신고됨");


        private String type;
        roomStatus(String type) {
            this.type = type;
        }

        public String getKey() {
            return name();
        }

        public String getValue() {
            return type;
        }
    }

}
