package multiple.zone.service;

import multiple.zone.commons.base.core.ResultData;
import multiple.zone.commons.base.service.BaseService;
import multiple.zone.entity.User;

/**
 * 账号
 * @Description
 * @Author lichao
 * @Date 2017/09/11
 */
public interface UserService extends  BaseService<User>{
	/**
     * 通过用户名查询单个用户，用户名保持唯一
     *
     * @param username
     * @return
     */
    public ResultData<User> queryByUsername(String username);
}
