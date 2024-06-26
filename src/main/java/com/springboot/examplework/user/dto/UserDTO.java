package com.springboot.examplework.user.dto;

import com.springboot.examplework.auth.constaint.UserTable;
import com.springboot.examplework.security.utils.MyUserDetails;
import com.springboot.examplework.user.entity.UserPO;

public class UserDTO {
	/** id(系統自動產生) */
	private long userId;
	
	/** 帳號 */
	private String account;

	/** 密碼 */
	private String password;

	/** 使用者名稱 */
	private String userName;

	private UserTable userTable;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public UserTable getUserTable(){
		return userTable;
	}

	public void setUserTable(UserTable userTable){
		this.userTable = userTable;
	}

	public UserDTO convertMyuserToDTO(MyUserDetails myUserDetails) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(myUserDetails.getUserId());
		userDTO.setAccount(myUserDetails.getAccount());
		userDTO.setUserName(myUserDetails.getUsername());
		userDTO.setPassword(myUserDetails.getPassword());
		userDTO.setUserTable(myUserDetails.getUserTable());
		return userDTO;
	}

	public UserDTO convertPOToDTO(UserPO userPO) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userPO.getId());
		userDTO.setAccount(userPO.getAccount());
		userDTO.setUserName(userPO.getRealName());
		userDTO.setPassword(userPO.getPassword());
		userDTO.setUserTable(UserTable.PLATFORM_USER);
		return userDTO;
	}

	public String toString() {
		return "GlobalUserDTO{" +
				"userId=" + userId +
				", account='" + account + '\'' +
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				'}';
	}
}
