package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.application.UserUtilService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserUtilResponseDto;
import com.dungzi.backend.global.common.response.code.CommonCode;
import com.dungzi.backend.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users") // cookie 에서 사용자 정보를 확인하는 api 들
public class UserUtilController {
    private final AuthService authService;
    private final UserUtilService userUtilService;
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
    public ResponseEntity<CommonResponse> getUserProfile() {
        log.info("[API] users/profile");
        User user = authService.getUserFromSecurity();
        Optional<UnivAuth> univAuthOp = univAuthService.getUnivAuthByUser(user);
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK, UserUtilResponseDto.GetUserProfile.toDto(user, univAuthOp)));
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
    public ResponseEntity<CommonResponse> updateUserEmailAuth(@RequestBody @Valid UserRequestDto.UpdateEmailAuth requestDto) {
        log.info("[API] users/univs");
        User user = authService.getUserFromSecurity();

        Univ univ = univService.getUniv(requestDto.getUnivId());
        univService.checkUnivDomain(requestDto.getUnivEmail(), univ);

        UnivAuth univAuth = univAuthService.updateUserEmailAuth(user, univ, requestDto.getUnivEmail(), requestDto.getIsEmailChecked());
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK, UserUtilResponseDto.UpdateEmailAuth.toDto(user, univAuth)));
    }

    @Operation(summary = "닉네임 변경 api", description = "사용자 닉네임 변경")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "변경 성공"),
                    @ApiResponse(responseCode = "401", description = "사용자 확인 불가")
            }
    )
    @Transactional
    @PatchMapping("/nickname")
    public ResponseEntity<CommonResponse> updateNickname(@RequestBody @Valid UserRequestDto.NicknameOnly requestDto) {
        log.info("[API] users/nickname");
        authService.validateNickname(requestDto.getNickname());
        User user = authService.getUserFromSecurity();
        userUtilService.updateNickname(user, requestDto.getNickname());
        return ResponseEntity.ok(CommonResponse.toResponse(CommonCode.OK, null));
    }

}
