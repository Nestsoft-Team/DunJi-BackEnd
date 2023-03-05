package com.dungzi.backend.domain.room.application;

import com.dungzi.backend.domain.room.dto.enumType.OptionsEnum;
import javax.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.persistence.Converter;
import java.util.Arrays;
import java.util.EnumSet;

@Slf4j
@Converter
// Entity에서 사용할 X Type으로 EnumSet<Category>
// DB에서 읽어오거나 저장할 때 사용할 Y Type으로 String 선언
public class SetOptionConverter implements AttributeConverter<EnumSet<OptionsEnum>, String> {

    // DB에서 사용할 Type으로 Entity Type을 변환하는 로직 구현
    @Override
    public String convertToDatabaseColumn(EnumSet<OptionsEnum> attribute) {
        // DB에서 사용할 String Type 생성을 위해 StringBuilder 선언
        StringBuilder sb = new StringBuilder();
        // ["KOREA", "ALONE"] 과 같은 EnumSet Collection Data를
        // 각각 foreach를 돌며 "KOREA," 형식으로 StringBuilder에 append
        attribute.stream().forEach(e -> sb.append(e.name()+","));
        // 최종 결과 String으로 변환
        String result = sb.toString();
        // "KOREA,ALONE," 형식 일 경우 마지막 ',' 제거
        if(result.charAt(result.length() - 1) == ',') result = result.substring(0, result.length() - 1);
        return result;
    }

    // Entity에서 사용할 Type으로 DB Type을 변환하는 로직 구현
    @Override
    public EnumSet<OptionsEnum> convertToEntityAttribute(String dbData) {
        // DB에서 읽어온 값이 null이거나 공백이거나 CATEGORY.KOREA(name="한식") 형태로 읽어올 경우 제외
        if(dbData == null || dbData == "" || dbData.contains(".")) return EnumSet.noneOf(OptionsEnum.class);
        // 최초 빈 Collection 생성
        EnumSet<OptionsEnum> attribute = EnumSet.noneOf(OptionsEnum.class);
        // DB에서 읽어온 "KOREA,ALONE" 형태의 데이터 ','로 split
        String[] dbDataArray = StringUtils.trimAllWhitespace(dbData).toUpperCase().split(",");
        // 빈 Collection으로 생성한 EnumSet에 split한 data를 Category(Enum) .valueOf로 생성
        // 해당 구문에서 Enum에 선언되지 않은 값 존재 시 Exception 발생 가능
        Arrays.stream(dbDataArray).forEach(e -> attribute.add(OptionsEnum.valueOf(e)));
        return attribute;
    }
}