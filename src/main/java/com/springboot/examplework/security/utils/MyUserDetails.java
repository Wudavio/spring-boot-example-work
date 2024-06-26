package com.springboot.examplework.security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springboot.examplework.auth.constaint.UserTable;
import com.springboot.examplework.user.dto.UserDTO;

import java.util.Collection;
import java.util.Collections;

public class MyUserDetails implements UserDetails {
    private final UserDTO userDTO;

    public MyUserDetails(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回一個空的權限列表
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getUserName();
    }

	public String getAccount() {
		return userDTO.getAccount();
	}
    
    public long getUserId() {
        return userDTO.getUserId();
    }

    public UserTable getUserTable() {
        return userDTO.getUserTable();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}