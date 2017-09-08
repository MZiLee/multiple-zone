package org.multiple.zone.entity;

import java.util.Date;

import multiple.zone.commons.base.entity.BaseEntity;

public class User extends BaseEntity<User>{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -1911665535364553894L;

	/**
	 * 用户id
	 */
	private Integer id;
	
	/**
	 * 用户名
	 */
	private String name;
	
	/**
	 * 登录名
	 */
	private String loginName;
	
	/**
	 * 登录密码
	 */
	private String password;
	
	/**
	 * 用户头像
	 */
	private String userImg;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date modifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	
	
}
