package com.springboot.examplework.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.examplework.auth.constaint.UserTable;
import com.springboot.examplework.auth.dto.*;
import com.springboot.examplework.auth.service.LoginService;
import com.springboot.examplework.auth.utils.UserUtils;
import com.springboot.examplework.core.dto.AjaxDTO;
import com.springboot.examplework.core.service.impl.InMemoryTokenList;
import com.springboot.examplework.security.service.JwtAuthService;
import com.springboot.examplework.user.dto.UserDTO;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "授權 APIs")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private JwtAuthService jwtAuthService;
    @Autowired
    private InMemoryTokenList inMemoryTokenList;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<AjaxDTO> handleResponseStatusException(ResponseStatusException ex) {
        AjaxDTO dto = new AjaxDTO();
        dto.setMessage(ex.getReason());
        dto.setStatusFail();
        return ResponseEntity.status(ex.getStatusCode()).body(dto);
    }

    @PostMapping("/login")
    @Operation(
        summary = "登入",
        description = "這個 API 操作可以登入並且拿到JWT Token。",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功請求資源",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AjaxDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "請求格式錯誤"),
            @ApiResponse(responseCode = "401", description = "認證失敗"),
            @ApiResponse(responseCode = "403", description = "認證成功，但沒有權限訪問"),
            @ApiResponse(responseCode = "404", description = "找不到資源"),
            @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
        }
    )
    public ResponseEntity<AjaxDTO> globalLogin(@RequestBody @Valid UserLoginReq userLoginRequest) {
    	AjaxDTO dto = new AjaxDTO();
        UserDTO userDTO = loginService.globalLogin(userLoginRequest);
        if(userDTO == null) {
            dto.setData(userDTO);
            dto.setMessage("找不到該使用者!");
            dto.setStatusFail();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
        }

        String token = jwtAuthService.auth(userLoginRequest.getAccount(), userLoginRequest.getPassword(), UserTable.PLATFORM_USER.getValue());
        String refreshToken = jwtAuthService.refreshAuth(userLoginRequest.getAccount(), userLoginRequest.getPassword(), UserTable.PLATFORM_USER.getValue());
        inMemoryTokenList.addToRefreshTokenMap(token, refreshToken);
        Map<String, String> tokenMap = Map.of("token", token, "refreshToken", refreshToken);
        dto.setData(tokenMap);
        dto.setStatusOK();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("logout")
    @Operation(
        summary = "登出使用者", 
        description = "這個 API 操作可以用來登出使用者",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "更新資源成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AjaxDTO.class)
                )
            ),
            @ApiResponse(responseCode = "403", description = "沒有權限訪問此API"),
            @ApiResponse(responseCode = "404", description = "找不到資源")
        }
    )
    public ResponseEntity<AjaxDTO> logout(HttpServletRequest request) {
        String token = userUtils.extractTokenFromRequest(request);
        inMemoryTokenList.addToBlacklist(token); // 加入黑名單

        AjaxDTO dto = new AjaxDTO();
        dto.setMessage("登出成功");
        dto.setStatusOK();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @PostMapping("isValidToken")
    @Operation(
        summary = "確認AccessToken是否有效"
    )
    public ResponseEntity<AjaxDTO> isValidToken(@RequestBody @Valid @NotBlank String accessToken) {
        boolean isValid = jwtAuthService.isValidToken(accessToken);
        AjaxDTO dto = new AjaxDTO();
        dto.setData(isValid);
        dto.setStatusOK();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("refreshToken")
    @Operation(
        summary = "重新取得AccessToken",
        description = "根據RefreshToken取得新的AccessToken"
    )
    public ResponseEntity<AjaxDTO> refreshToken(@RequestBody @Valid @NotBlank String refreshToken) {
        boolean isValid = jwtAuthService.isValidRefreshToken(refreshToken);
        if(!isValid) {
            AjaxDTO dto = new AjaxDTO();
            dto.setMessage("refreshToken 過期");
            dto.setStatusFail();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
        }

        String newToken = jwtAuthService.refreshToken(refreshToken);
        inMemoryTokenList.addBlacklistToRefreshTokenMap(refreshToken);
        inMemoryTokenList.addToRefreshTokenMap(newToken, refreshToken);
        Map<String, String> tokenMap = Map.of("token", newToken);

        AjaxDTO dto = new AjaxDTO();
        dto.setData(tokenMap);
        dto.setStatusOK();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
