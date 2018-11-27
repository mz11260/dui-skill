package com.zm.skill.dispatcher.impl;

import com.zm.kit.dispatcher.interfaces.SubscriberExceptionHandler;
import com.zm.protocol.ResponseBuilder;
import com.zm.protocol.response.SkillResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/11/26.
 */
@Component("demoSubscriberExceptionHandler")
@Slf4j
public class DemoExceptionHandler implements SubscriberExceptionHandler {
    @Override
    public SkillResponse handleException(Throwable exception) {
        log.error("Skill Exception: ", exception);
        return ResponseBuilder.error("我好像遇到了点问题");
    }
}
