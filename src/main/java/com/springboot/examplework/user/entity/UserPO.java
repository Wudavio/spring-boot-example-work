package com.springboot.examplework.user.entity;

import java.io.Serializable;
import java.util.Date;

import com.springboot.examplework.user.dto.UserReq;

import jakarta.persistence.*;

@Entity
@Table(name = "platform_users")
public class UserPO implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "account", nullable = false)
	private String account;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "real_name", nullable = false)
	private String realName;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "birthday", nullable = true)
	private Date birthday;

	@Column(name = "telephone", nullable = false)
	private String telephone;

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public UserPO convertReqToPO(UserReq req) {
		UserPO po = new UserPO();
		po.setAccount(req.getAccount());
		po.setPassword(req.getPassword());
		po.setRealName(req.getRealName());
		po.setEmail(req.getEmail());
		po.setGender(req.getGender());
		po.setBirthday(req.getBirthday());
		po.setTelephone(req.getTelephone());
		po.setPhone(req.getPhone());
		return po;
	}
}
