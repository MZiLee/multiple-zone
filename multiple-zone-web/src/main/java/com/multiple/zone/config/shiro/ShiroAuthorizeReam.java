package com.multiple.zone.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.multiple.zone.utils.SpringUtil;

import multiple.zone.commons.base.core.ResultData;
import multiple.zone.entity.User;
import multiple.zone.service.UserService;

/**
* @Description shiro
* @Author lichao
* @Date 2017年10月11日14:32:55
* @Copyright(c) gome inc Gome Co.,LTD
*/
public class ShiroAuthorizeReam extends AuthorizingRealm  {

    private final static Logger logger = LoggerFactory.getLogger(ShiroAuthorizeReam.class);
//    @Autowired
//    private UserService userService;

    /**
     * 权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.fromRealm(getName()).iterator().next();
        if (user!=null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole("admin");
            info.addStringPermission("admin:*");
            return info;
        }
        return null;
    }

//    @Override
//    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        try {
//            this.userService = (UserService) SpringUtil.getBean("userService");
//        } catch (Exception e) {
//            System.out.println("Error loading: " + e.getMessage());
//            throw new Error("Critical system error", e);
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//
//    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        UserService userService= (UserService) SpringUtil.getBean("userService");
        ResultData<User> resultData = userService.queryByUsername(token.getUsername());
        User user = resultData.getData();
        if (user == null) {//如果帐号不存在，输出
            throw new UnknownAccountException();
        }
//        if (user.getStatus().equals(AccountStatus.DELETED)) {//如果帐号被删除，输出异常
//            throw new DeletedAccountException();
//        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), null, getName());
        return info;
    }

    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
  //      String credentials = "Abcd1234";
        String credentials = "";
        int hashIterations = 1;
        Object obj = new SimpleHash(hashAlgorithmName, credentials, null, hashIterations);
        System.out.println(obj);
    }
    Object encryptPassword(String password){
        String hashAlgorithmName = "MD5";
        String credentials = password;
        int hashIterations = 1;
        Object obj = new SimpleHash(hashAlgorithmName, credentials, null, hashIterations);
        return obj;
    }
}
