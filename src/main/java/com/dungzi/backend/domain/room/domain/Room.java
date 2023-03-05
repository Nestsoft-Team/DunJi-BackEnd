package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.domain.room.dto.RoomDto;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID roomId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "univId")
//    private Univ univ;

    @Column(nullable = false)
    private String title;

    private String content;
    private int image;
    //private ZonedDateTime regDate;
    private Date delDate;
    private Date dealDate;
    private int heartNum;

}
