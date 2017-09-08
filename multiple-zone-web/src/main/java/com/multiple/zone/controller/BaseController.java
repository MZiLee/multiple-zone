package com.multiple.zone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import multiple.zone.commons.base.core.ErrorCode;
import multiple.zone.commons.base.core.MultipleBusinessException;
import multiple.zone.commons.base.core.MultipleLoggerError;
import multiple.zone.commons.base.core.MultipleRuntimeException;
import multiple.zone.commons.base.core.ResultData;

/**
 * User: lichao
 * Date: 2017年9月8日15:46:10
 */

public abstract class BaseController {


    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 统一处理 MultipleBusinessException 异常 ， 其余异常需要自己处理
     *
     * @param e ：MultipleBusinessException
     */
    @ExceptionHandler(MultipleBusinessException.class)
    @ResponseBody
    public ResultData<Object> MultipleBusinessException(MultipleBusinessException e) {
        if (e instanceof MultipleBusinessException) {
            String code = e.getCode();
            String message = e.getMessage();
            logger.error("MultipleBusinessException code : {} , message : {}", code, message);
            return ResultData.newResultData(code, message);
        }
        MultipleLoggerError.error(e);
        return ResultData.newResultData(ErrorCode.ADD_FAILOR, ErrorCode.ADD_FAILOR_MSG);
    }

    @ExceptionHandler(MultipleRuntimeException.class)
    @ResponseBody
    public ResultData<Object> gomeRuntimeException(MultipleRuntimeException e) {
        MultipleLoggerError.error(e);
        if (e instanceof MultipleRuntimeException) {
            String code = e.getCode();
            String message = e.getMessage();
            logger.error("MultipleBusinessException code : {} , message : {}", code, message);
            return ResultData.newResultData(code, message);
        }
        return ResultData.newResultData(ErrorCode.ADD_FAILOR, ErrorCode.ADD_FAILOR_MSG);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultData<Object> exception(Exception e) {
        MultipleLoggerError.error(e);
        if (e instanceof Exception) {
            String code = ErrorCode.FAILOR;
            String message = e.getMessage();
            logger.error("MultipleBusinessException code : {} , message : {}", code, message);
            return ResultData.newResultData(code, ErrorCode.FAILOR_MSG);
        }
        return ResultData.newResultData(ErrorCode.ADD_FAILOR, ErrorCode.ADD_FAILOR_MSG);
    }

}

