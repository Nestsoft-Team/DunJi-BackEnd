package com.dungzi.backend.domain.room.dto.enumType;

public enum AdvantageEnum {
    elevators("엘리베이터"),
    parking("주차공간"),
    pets("반려동물"),
    womenOnly("여성전용"),
    loan("전세대출");

    //public List<Advantage> advantageList = new ArrayList<>();

    public String advantage;

    AdvantageEnum(String value) {
        this.advantage = value;
    }

//    List<Advantage> advantage = new ArrayList<Advantage>();
//
//    public List<Advantage> toAdvantageEnum(List<String> values) {
//        for(String value : values) {
//            advantage.add(Advantage.valueOf(value));
//        }
//        return advantage;
//    }
//
//    public String getAdvantageList() {
//        return advantageList.toString();
//    }
}
