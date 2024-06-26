package com.springboot.examplework.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


import com.springboot.examplework.auth.constaint.UserTable;
import com.springboot.examplework.auth.dto.*;
import com.springboot.examplework.auth.service.LoginService;
import com.springboot.examplework.user.dao.UserDAO;
import com.springboot.examplework.user.dto.UserDTO;
import com.springboot.examplework.user.entity.UserPO;

@Component
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserDAO userDAO; //平台的使用者(老師, 學生, 家長)

    @Override
    public UserDTO globalLogin(UserLoginReq userLoginRequest) {
        UserPO userPO = userDAO.findByAccount(userLoginRequest.getAccount());
        // 檢查 user 是否存在
        if (userPO == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "此帳號尚未註冊");
        }

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

        // 比較密碼
        if(!bcryptPasswordEncoder.matches(userLoginRequest.getPassword(), userPO.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "帳號密碼不正確");
        }

        // 如果被刪除
        if(userPO.getDeletedAt() != null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "此帳號已被停用");
        }

        return new UserDTO().convertPOToDTO(userPO);
    }

    public UserDTO getUserAccount(String account) {
        String newAccount = account.split(":_:")[0];
        String tableName = account.split(":_:")[1];

        UserDTO userDTO = null;
        if(tableName.equals(UserTable.PLATFORM_USER.getValue())){
            UserPO userPO = userDAO.findByAccount(newAccount);
            userDTO = new UserDTO().convertPOToDTO(userPO);
        }
        return userDTO;
    }
}
