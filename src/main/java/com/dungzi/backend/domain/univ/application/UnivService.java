package com.dungzi.backend.domain.univ.application;

import com.dungzi.backend.domain.univ.dao.UnivDao;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.error.UnivErrorCode;
import com.dungzi.backend.global.common.error.UnivException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UnivService {

    private final UnivDao univDao;

    public void checkUnivDomain(String email, String univId) {
        Univ univ = univDao.findById(univId)
                .orElseThrow(() -> new UnivException(UnivErrorCode.UNIV_NOT_FOUND));
        checkUnivDomain(email, univ); //TODO : 예외 핸들링하기!!
    }

    public void checkUnivDomain(String email, Univ univ) {
        String domain = email.split("@")[1];
        univ.checkDomain(domain);
    }

    public Univ getUniv(String univId) {
        log.info("[SERVICE] getUniv");
        return univDao.findById(univId)
                .orElseThrow(() -> new UnivException(CommonCode.NOT_FOUND)); //TODO : 예외처리 handler로 핸들링하기!!!
    }

    public List<Univ> getUnivAll() {
        log.info("[SERVICE] getUnivAll");
        return univDao.findAll();
    }

    public Univ saveUniv(Univ univ) {
        return univDao.save(univ);
    }
}
