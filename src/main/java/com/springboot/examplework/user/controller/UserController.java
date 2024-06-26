package com.springboot.examplework.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.examplework.auth.utils.UserUtils;
import com.springboot.examplework.core.dto.AjaxDTO;
import com.springboot.examplework.user.dto.UserPutReq;
import com.springboot.examplework.user.dto.UserReq;
import com.springboot.examplework.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "User APIs")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserUtils userUtils;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<AjaxDTO> handleResponseStatusException(ResponseStatusException ex) {
        AjaxDTO dto = new AjaxDTO();
        dto.setMessage(ex.getReason());
        dto.setStatusFail();
        return ResponseEntity.status(ex.getStatusCode()).body(dto);
    }

    @Operation(
        summary = "新增平台使用者",
        description = "這個 API 操作可以用來新增使用者。",
        responses = {
            @ApiResponse(
                responseCode = "201", 
                description = "成功創建資源", 
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AjaxDTO.class)
                )
            ),
            @ApiResponse(responseCode = "400", description = "請求格式錯誤"),
            @ApiResponse(responseCode = "403", description = "沒有權限訪問此API"),
            @ApiResponse(responseCode = "404", description = "找不到資源")
        }
    )
	@PostMapping(consumes = "application/json")
	public ResponseEntity<AjaxDTO> createPlatformUsers(@RequestBody @Valid UserReq userReq) {
    	AjaxDTO dto = new AjaxDTO();
		Map<String, Object> isSuccess = userService.createPlatformUsers(userReq);
		dto.setMessage((String) isSuccess.get("message"));
		if (!(Boolean) isSuccess.get("success")) {
			dto.setStatusFail();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		} else {
			dto.setStatusOK();
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		}
    }

    @Operation(
        summary = "更新平台使用者",
        description = "這個 API 操作可以用來更新會員資料",
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
            @ApiResponse(responseCode = "401", description = "JWT Token 驗證失敗"),
            @ApiResponse(responseCode = "403", description = "沒有權限訪問此API"),
            @ApiResponse(responseCode = "404", description = "找不到資源")
        }
    )
    @PutMapping(consumes = "application/json")
	public ResponseEntity<AjaxDTO> updatePlatformUsers(@RequestBody @Valid UserPutReq userReq) {
    	AjaxDTO dto = new AjaxDTO();
		Map<String, Object> isSuccess = userService.updatePlatformUsers(userReq);

		dto.setMessage((String) isSuccess.get("message"));
		if (!(Boolean) isSuccess.get("success")) {
			dto.setStatusFail();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		} else {
			dto.setStatusOK();
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
    }

    @Operation(
        summary = "刪除平台使用者",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", 
                            description = "已刪除資源", 
                            content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = AjaxDTO.class)
                            )),
            @ApiResponse(responseCode = "404", 
                            description = "找不到資源")
        }
    )
    @DeleteMapping
    public ResponseEntity<AjaxDTO> deletePlatformUsers() {
	    AjaxDTO dto = new AjaxDTO();
		Map<String, Object> isSuccess = userService.deletePlatformUsers();

		dto.setMessage((String) isSuccess.get("message"));
		if (!(Boolean) isSuccess.get("success")) {
			dto.setStatusFail();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		} else {
			dto.setStatusOK();
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		}
	}

    @GetMapping
    @Operation(
        summary = "取得登入者資訊",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<AjaxDTO> test() {
        AjaxDTO dto = new AjaxDTO();
        dto.setData(userUtils.getUser());
        dto.setStatusOK();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
