package com.springboot.examplework.user.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class UserPutReq {
	@NotNull
	@Schema(description = "真實姓名", example = "王大明")
	private String realName;
	@Schema(description = "email", example = "testABC@gmail.com")
	private String email;
    @NotNull
	@Schema(description = "性別 1:男 0: 女", example = "1")
	private String gender;
	@Schema(description = "生日", example = "2000-01-01")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	@Schema(description = "市話", example = "07-6543210")
	private String telephone;
	@Schema(description = "手機", example = "0912345678")
	private String phone;

    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
