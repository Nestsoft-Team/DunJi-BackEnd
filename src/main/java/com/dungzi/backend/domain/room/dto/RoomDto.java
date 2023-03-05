package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class RoomDto {

    private UUID roomId;
    private String userId;
    private String univId;
    private String title;
    private String content;
    private int image;
    private ZonedDateTime regDate;
    private Date delDate;
    private Date dealDate;
    private int heartNum;

    public Room toEntity(int image) {
        UUID uuid = null;
        if(roomId != null) {
            uuid = roomId;
        }

        UserDto userDto = null;
        //userDto.setUserId(roomDto.getUserId());
        User user = userDto.toEntity();

        return Room.builder()
                .roomId(uuid)
                .user(user)
//                .univId
                .title(title)
                .content(content)
                .image(image)
                //.regDate(regDate)
                .delDate(delDate)
                .dealDate(dealDate)
                .heartNum(heartNum)
                .build();
    }
}
