package com.springboot.examplework.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UserLoginReq {

    @NotBlank
    @Schema(description = "帳號", example = "admin_test")
    private String account;

    @NotBlank
    @Schema(description = "密碼", example = "passwordtest")
    private String password;

    public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
