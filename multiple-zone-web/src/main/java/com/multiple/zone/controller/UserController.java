package com.multiple.zone.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;

import multiple.zone.commons.base.core.ErrorCode;
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
	
	@Autowired
	private DefaultKaptcha defaultKaptcha;
	
	private RedisTemplate<String,?> redisTemplate;
	/**
	 *	用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	@ResponseBody
	public ResultData<Object> login(String username,String password,Boolean rememberMe,String code){
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		if(rememberMe != null){
			token.setRememberMe(rememberMe);
		}else{
			token.setRememberMe(false);
		}
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
			if(currentUser.isAuthenticated()){
				return ResultData.newSuccessResultData();
			}
		} catch (UnknownAccountException uae) {
            logger.error("user[" + username + "]UnknownAccountException");
            return ResultData.newResultData(ErrorCode.FAILOR,"该账号不存在，请重新输入");
        }catch (LockedAccountException lae) {
            logger.error("user[" + username + "]LockedAccountException");
            return ResultData.newResultData(ErrorCode.FAILOR,"账户锁定");
        }catch (DisabledAccountException uae) {
            logger.error("user[" + username + "]DisabledAccountException");
            return ResultData.newResultData(ErrorCode.FAILOR,"账户被禁用");
        } catch (IncorrectCredentialsException ice) {
            logger.error("user[" +username + "]IncorrectCredentialsException");
            return ResultData.newResultData(ErrorCode.FAILOR,"密码错误，请重新输入");
        }  catch (ExcessiveAttemptsException eae) {
            logger.error("user[" + username + "]ExcessiveAttemptsException");
            return ResultData.newResultData(ErrorCode.FAILOR,"重试次数过多，请稍后重试");
        } catch (AuthenticationException ae) {
            logger.error("user[" + username + "]AuthenticationException");
            return ResultData.newResultData(ErrorCode.FAILOR,"认证失败");
        }
		return ResultData.newResultData(ErrorCode.FAILOR,"登录失败！");
	}
	
	/**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "/logout",produces="application/json;charset=UTF-8")
    @ResponseBody
    public ResultData<Object> logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {  
            try{  
                subject.logout(); 
                logger.debug("用户退出系统");
                return ResultData.newSuccessResultData();
            }catch(Exception ex){  
                logger.error("用户退出系统异常");
                return ResultData.newResultData(ErrorCode.FAILOR,ErrorCode.FAILOR_MSG);
            }  
        }  
        logger.debug("用户退出系统");
        return ResultData.newSuccessResultData();
       
    }
	
    /**
     * 退出登录
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/getCode")
    @ResponseBody
	public void getCode(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException{
    	byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } 
	}
	
	
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
