package com.multiple.zone.controller;

import org.multiple.zone.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import multiple.zone.commons.base.core.ResultData;

/**
 * @author lichao
 * @Date   2017年9月8日11:18:53
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@RequestMapping("/pager")
	@ResponseBody
	public ResultData<User> pager(){
		User user = new User();
		user.setId(1);
		user.setName("lc");
		user.setPassword("123");
		return ResultData.newResultData(user);
	}
}
