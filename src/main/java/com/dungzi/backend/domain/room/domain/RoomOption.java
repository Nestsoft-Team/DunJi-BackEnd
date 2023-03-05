package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoomOption extends BaseTimeEntity {

    @Id
    @Column(name = "room_id", nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "room_id")
    private Room room;

    //@Convert(converter = SetOptionConverter.class)
//    private EnumSet<Options> options;
    private String options;

    private String utility;

    //@Convert(converter = SetAdvantageConverter.class)
    //private String advantage;

    private String startedAt;
    private String finishedAt;
    private boolean tenancyAgreement;
}