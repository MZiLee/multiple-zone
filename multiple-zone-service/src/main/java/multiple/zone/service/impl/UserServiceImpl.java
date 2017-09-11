package multiple.zone.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import multiple.zone.commons.base.service.BaseServiceSupport;
import multiple.zone.dao.UserDao;
import multiple.zone.entity.User;
import multiple.zone.service.UserService;

/**
 * 账号
 * @Description
 * @Author lichao
 * @Date 2017/09/11
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceSupport<User> implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
    private UserDao userDao;
    
    @Override
	public UserDao getDao() {
		return userDao;
	}
}
