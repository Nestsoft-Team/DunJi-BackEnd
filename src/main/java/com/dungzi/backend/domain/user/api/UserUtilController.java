package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserAuthResponseDto;
import com.dungzi.backend.domain.user.dto.UserUtilResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import com.dungzi.backend.global.common.error.AuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserUtilController {
    private final AuthService authService;
    private final UnivService univService;
    private final UnivAuthService univAuthService;

    @Operation(summary = "사용자 프로필 api", description = "사용자 기본 정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "사용자 확인 불가")
            }
    )
    @GetMapping("/profile")
    public CommonResponse getUserProfile() {
        log.info("[API] users/profile");
        User user = authService.getUserFromSecurity();
        UnivAuth univAuth = univAuthService.getUnivAuthByUser(user);
        return CommonResponse.toResponse(CommonCode.OK, UserUtilResponseDto.GetUserProfile.toDto(user, univAuth));
    }

    @Operation(summary = "대학교 이메일 인증 api", description = "대학교 이메일 인증 정보 저장")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "request body 값 관련 오류"),
                    @ApiResponse(responseCode = "401", description = "사용자 확인 불가")
            }
    )
    @PutMapping("/univs")
    public CommonResponse updateUserEmailAuth(@RequestBody @Valid UserRequestDto.UpdateEmailAuth requestDto) {
        log.info("[API] users/univs");
        User user = authService.getUserFromSecurity();

        Univ univ = univService.getUniv(requestDto.getUnivId());
        univService.checkUnivDomain(requestDto.getUnivEmail(), univ);

        UnivAuth univAuth = univAuthService.updateUserEmailAuth(user, univ, requestDto.getUnivEmail(), requestDto.getIsEmailChecked());
        return CommonResponse.toResponse(CommonCode.OK, UserUtilResponseDto.UpdateEmailAuth.toDto(user, univAuth));
    }
}
