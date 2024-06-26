package com.springboot.examplework.user.service.impl;

import java.util.Date;
import java.util.Map;

// Array List
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.examplework.auth.utils.UserUtils;
import com.springboot.examplework.core.traits.ResponseServiceTrait;
import com.springboot.examplework.user.dao.UserDAO;
import com.springboot.examplework.user.dto.UserPutReq;
import com.springboot.examplework.user.dto.UserReq;
import com.springboot.examplework.user.entity.UserPO;
import com.springboot.examplework.user.service.UserService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private ResponseServiceTrait response;
	@Autowired
	private UserDAO userDAO;
	@Autowired
    private UserUtils userUtils;

	/**
	 * 新增平台使用者
	 * 
	 * @param UserReq
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> createPlatformUsers(UserReq userReq) {
		Map<String, Object> isAccountCreated = this.checkAccount(userReq);
		if (!(Boolean) isAccountCreated.get("success")) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "此帳號已存在!");
		}

		UserPO userPO = new UserPO().convertReqToPO(userReq);

		String encodePassword = new BCryptPasswordEncoder().encode(userReq.getPassword());
		userPO.setPassword(encodePassword);
		userPO.setCreatedAt(new Date());
		userPO.setUpdatedAt(new Date());
		userDAO.save(userPO);

		return response.responseStatusData(true);
	}

	/**
	 * 確認帳號重複
	 * @param userReq
	 * @return Map<String, Object>
	 */
	public Map<String, Object> checkAccount(UserReq userReq) {
		Boolean isCheckAccount = userDAO.existsByAccount(userReq.getAccount());
		if (isCheckAccount) {
			return response.responseStatusData(false, "帳號已存在!", response.getErrorCode("common", "OPERATE_FAILED"));
		}
		return response.responseStatusData(true);
	}
	
	/**
	 * 更新平台使用者
	 * 
	 * @param UserReq
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> updatePlatformUsers(UserPutReq userPutReq) {
        UserPO userPO = userDAO.findById(userUtils.getUser().getUserId());
		if (userPO == null) {
			return response.responseStatusData(false, "找不到該使用者!", response.getErrorCode("common", "OPERATE_FAILED"));
		}

		userPO.setRealName(userPutReq.getRealName());
		userPO.setGender(userPutReq.getGender());
		userPO.setBirthday(userPutReq.getBirthday());
		userPO.setPhone(userPutReq.getPhone());
		userPO.setTelephone(userPutReq.getTelephone());
		userPO.setEmail(userPutReq.getEmail());
		userPO.setUpdatedAt(new Date());
		userDAO.save(userPO);
		
        return response.responseStatusData(true);
	}
	
	/**
	 * 刪除平台使用者
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> deletePlatformUsers() {
        UserPO userPO = userDAO.findById(userUtils.getUser().getUserId());
		if (userPO == null) {
			return response.responseStatusData(false, "找不到該使用者!", response.getErrorCode("common", "OPERATE_FAILED"));
		}
		userPO.setDeletedAt(new Date());
		userDAO.save(userPO);
		return response.responseStatusData(true);
	}
}
