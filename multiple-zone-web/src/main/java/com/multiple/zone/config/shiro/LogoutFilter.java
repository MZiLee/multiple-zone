package com.multiple.zone.config.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by lichao on 2017年10月11日14:30:43.
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        return super.preHandle(request, response);
    }
}
