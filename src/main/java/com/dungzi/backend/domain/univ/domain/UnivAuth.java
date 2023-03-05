package com.dungzi.backend.domain.univ.domain;

import com.dungzi.backend.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UnivAuth {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false, length = 36)
    @Type(type = "uuid-char")
    private UUID univAuthId;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private User user;

    @JoinColumn(name = "univ_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Univ univ;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean isChecked;

    public void updateUnivAuth(Univ univ, Boolean isChecked) {
        this.univ = univ;
        this.isChecked = isChecked;
    }
}
