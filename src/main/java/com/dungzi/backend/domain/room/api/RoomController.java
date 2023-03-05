package com.dungzi.backend.domain.room.api;

import com.dungzi.backend.domain.room.application.RoomService;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.room.dto.RoomResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RoomController {
    private final AuthService authService;
    private final RoomService roomService;

    @PostMapping(value = "/rooms")
    public CommonResponse registerRoom(RoomRequestDto.RegisterDto body, List<MultipartFile> files) throws Exception {

        log.info("[API] Register Room");

        User user = authService.getUserFromSecurity();

        RoomResponseDto.RoomRegist response  = roomService.saveAction(body, files, user);

        log.info("[API] Room ID:{}, Registrant:{}", response.getRoomId(), user.getUserId());

        return CommonResponse.toResponse(CommonCode.OK, response);

    }

    @GetMapping(value = "/rooms")
    public CommonResponse roomDetail(@RequestParam("roomId") String roomId) {

        log.info("[API] Room Detail");
        log.info("매물 ID:{}", roomId);
        //RoomResponseDto.RoomDetail response = roomService.findRooms();
        return CommonResponse.toResponse(CommonCode.OK, "response");
    }

}