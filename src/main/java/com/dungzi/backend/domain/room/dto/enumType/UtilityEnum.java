package com.dungzi.backend.domain.room.dto.enumType;

public enum UtilityEnum {

    electricity("전기세"),
    gas("가스비"),
    water("수도세"),
    internet("인터넷"),
    tv("TV");

    public String utility;

    public String getKey() {
        return name();
    }

    public String getValue() {
        return utility;
    }

    UtilityEnum(String value) {
        this.utility = value;
    }

}
