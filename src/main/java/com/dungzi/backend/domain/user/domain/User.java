package com.dungzi.backend.domain.user.domain;

import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.user.dto.UserDto;
import com.dungzi.backend.domain.user.dto.UserResponseDto;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users") // TODO : 언제부터 반영?
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column( nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID userId;

    @Column(nullable = false)
    private String nickname; //kakao 필수 동의 항목

    @Column(nullable = false)
    private String email; //kakao 필수 동의 항목

    //    @Column(nullable = false)
    private String ci;

    private String profileImg; //kakao 선택 동의 항목
    private String phoneNum;
    private String userType;
    private String gender;

    //    @Temporal(TemporalType.TIMESTAMP)
    private Date delDate;
    private boolean isActivated;

//    @Column(nullable = false)
//    private String token;

//    @Column(nullable = false)
    private String name;

    public void updateRoles(List<String> roles) {
        this.roles = roles;
    }

    public void updateSignUpInfo(Optional<String> nicknameOp) {
        //이메일 변경
//        Optional<String> nicknameOp = Optional.ofNullable(requestDto.getNickname());
        nicknameOp.ifPresent(nickname -> this.nickname = nickname);
    }

    //////////-- set user roles(Authentication) : implements UserDetails --//////////
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getUserId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
