package com.multiple.zone.config.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

;

/**
 * @Description
 * @Author lichao
 * @Date 2017年10月11日14:32:13
 */
public class SessionExpiredFilter extends UserFilter {

    private static Logger logger = LoggerFactory.getLogger(SessionExpiredFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        if (this.isLoginRequest(request, response)) {
//            return true;
//        } else {
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setHeader("Content-type", "application/json;charset=UTF-8");
//            httpResponse.setCharacterEncoding("UTF-8");
//            ResultData resultData = ResultData.newResultData(ErrorCode.FAILOR, ErrorCode.FAILOR_MSG);
//            PrintWriter printWriter = httpResponse.getWriter();
//            printWriter.write(JsonUtil.toJson(resultData));
//            return false;
//        }
        return true;
    }

}
