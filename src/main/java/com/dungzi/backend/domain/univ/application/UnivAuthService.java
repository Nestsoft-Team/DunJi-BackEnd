package com.dungzi.backend.domain.univ.application;

import com.dungzi.backend.domain.univ.dao.UnivAuthDao;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.error.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UnivAuthService {
    private final UnivAuthDao univAuthDao;


    public Optional<UnivAuth> getUnivAuthByUser(User user) {
        log.info("[SERVICE] getUnivAuthByUser");
        return univAuthDao.findByUser(user);
    }

    @Transactional
    public UnivAuth updateUserEmailAuth(User user, Univ univ, String email, Boolean isEmailChecked) {
        log.info("[SERVICE] updateUserEmailAuth");
//        User user = getUserFromSecurity();

        Optional<UnivAuth> univAuthOp = univAuthDao.findByUser(user);

        // .isPresent()가 true이면 get()해서 updateUnivAuth로 수정 저장, false이면 createUnivAuth로 생성 저장
        return univAuthOp.map(univAuth -> updateUnivAuth(univAuth, user, univ, email, isEmailChecked))
                .orElseGet(() -> createUnivAuth(user, univ, email, isEmailChecked));
    }

    private UnivAuth updateUnivAuth(UnivAuth univAuth, User user, Univ univ, String email, Boolean isEmailChecked) {
        univAuth.updateUnivAuth(univ, isEmailChecked);
        return univAuthDao.save(univAuth);
    }

    public UnivAuth createUnivAuth(User user, Univ univ, String email, Boolean isEmailChecked){
        return univAuthDao.save(UnivAuth.builder()
                .user(user)
                .univ(univ)
                .email(email)
                .isChecked(isEmailChecked)
                .build());
    }

}
