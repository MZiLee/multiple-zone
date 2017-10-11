package multiple.zone.dao;

import multiple.zone.commons.base.dao.BaseDao;
import multiple.zone.entity.User;

/**
 * @Description
 * @Author lichao
 * @Date 2017年9月8日16:49:39
 */
public interface UserDao extends BaseDao<User> {
	
	/**
     * 通过用户名查询单个用户，用户名保持唯一
     *
     * @param username
     * @return
     */
    User queryByUsername(String username);
	
}
