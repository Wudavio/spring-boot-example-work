package com.springboot.examplework.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.examplework.security.utils.MyUserDetails;
import com.springboot.examplework.user.dto.UserDTO;

@Component
public class UserUtils {
    
    public UserDTO getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails myUser = (MyUserDetails) authentication.getPrincipal();
            UserDTO userDTO = new UserDTO().convertMyuserToDTO(myUser);
            return userDTO;
        } catch (Exception e) {
            // response 補上message
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token失效, 找不到使用者, 請重新登入", e);
        }
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
