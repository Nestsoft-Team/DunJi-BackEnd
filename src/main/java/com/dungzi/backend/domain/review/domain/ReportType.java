package com.dungzi.backend.domain.review.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    SPAM, //스팸
    VIOLENT, //욕설
    ADVERTISEMENT, //광고
    FALSE_INFO //허위정보
}


