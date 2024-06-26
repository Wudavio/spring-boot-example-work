package com.springboot.examplework.user.service;

import java.util.Map;

import com.springboot.examplework.user.dto.UserPutReq;
import com.springboot.examplework.user.dto.UserReq;

public interface UserService {
	Map<String, Object> createPlatformUsers(UserReq userReq);
	Map<String, Object> updatePlatformUsers(UserPutReq userPutReq);
	Map<String, Object> deletePlatformUsers();
}
