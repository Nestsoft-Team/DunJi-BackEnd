package com.dungzi.backend.domain.univ.api;

import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.dto.UnivRequestDto;
import com.dungzi.backend.domain.univ.dto.UnivResponseDto;
import com.dungzi.backend.global.common.response.code.CommonCode;
import com.dungzi.backend.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/univs")
public class UnivController {
    private final UnivService univService;

    @Operation(summary = "대학교 목록 조회 api", description = "DB에 저장된 대학교 목록 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 성공")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getUnivAll() {
        log.info("[API] univs/all");
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK,
                UnivResponseDto.UnivInfo.toUnivInfoList(univService.getUnivAll())));
    }


    // TODO : 임시로 만들고 현재 사용하지 않는 API. 추후 관리자 페이지에 대학교 추가 기능이 필요하면 사용
    @PostMapping("")
    public ResponseEntity<CommonResponse> saveUniv(@RequestBody @Valid UnivRequestDto.UnivInfo requestDto) {
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK, univService.saveUniv(requestDto.toEntity())));
    }
}
