package com.zm.filter;

import com.alibaba.fastjson.JSON;
import com.zm.protocol.ResponseBuilder;
import com.zm.protocol.response.SkillResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public String allExceptionHandler(Exception e) {
        logger.error("服务异常", e);
        SkillResponse response = ResponseBuilder.error("我好像遇到了点问题");
        return JSON.toJSONString(response);
    }
}
