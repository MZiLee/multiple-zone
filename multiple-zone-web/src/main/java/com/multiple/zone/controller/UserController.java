package com.multiple.zone.controller;

import org.multiple.zone.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/pager")
	@ResponseBody
	public User pager(){
		User user = new User();
		user.setId(1);
		user.setName("lc");
		user.setPassword("123");
		return user;
	}
}
