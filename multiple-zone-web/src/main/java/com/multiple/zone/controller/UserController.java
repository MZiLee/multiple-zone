package com.multiple.zone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import multiple.zone.commons.base.core.ResultData;
import multiple.zone.entity.User;
import multiple.zone.service.UserService;

/**
 * @author lichao
 * @Date   2017年9月8日11:18:53
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@Autowired
	private UserService userService;
	
	/**
	 *	分页查询用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/pager")
	@ResponseBody
	public ResultData<List<User>> pager(User user){
		
		return userService.queryByList(user);
	}
	
	/**
	 * 新增用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public ResultData<User> add(User user){
		return userService.add(user);
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ResultData<User> update(User user){
		return userService.updateBySelective(user);
	}
}
