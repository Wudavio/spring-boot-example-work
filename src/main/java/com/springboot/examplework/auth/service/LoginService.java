package com.springboot.examplework.auth.service;

import com.springboot.examplework.auth.dto.UserLoginReq;
import com.springboot.examplework.user.dto.UserDTO;

public interface LoginService {
    UserDTO globalLogin(UserLoginReq userLoginRequest);
    UserDTO getUserAccount(String account);
}
