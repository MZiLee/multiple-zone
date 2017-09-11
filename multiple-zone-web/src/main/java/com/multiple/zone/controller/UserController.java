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
	
	@RequestMapping("/pager")
	@ResponseBody
	public ResultData<List<User>> pager(User user){
		
		return userService.queryByList(user);
	}
}
