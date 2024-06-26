package com.springboot.examplework.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.examplework.auth.constaint.UserTable;
import com.springboot.examplework.auth.service.LoginService;
// import com.springboot.examplework.auth.service.GlobalUserService;
import com.springboot.examplework.security.utils.MyUserDetails;
import com.springboot.examplework.user.dto.UserDTO;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginService loginService;

    @Override
    public MyUserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        UserDTO userDTO = loginService.getUserAccount(account);
        if(account.split(":_:").length > 1) {
            if (UserTable.PLATFORM_USER.getValue().equals(account.split(":_:")[1])) {
                userDTO.setUserTable(UserTable.PLATFORM_USER);
            } else {
                userDTO.setUserTable(UserTable.PLATFORM_USER);
            }
        }
        return new MyUserDetails(userDTO);
    }
}