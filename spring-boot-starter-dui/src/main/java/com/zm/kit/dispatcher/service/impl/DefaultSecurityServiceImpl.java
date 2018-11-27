package com.zm.kit.dispatcher.service.impl;

import com.zm.kit.dispatcher.service.SecurityService;
import com.zm.protocol.request.SkillRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 签名认证服务 默认实现
 */
@Slf4j
public class DefaultSecurityServiceImpl implements SecurityService {

    @Override
    public boolean security(String headerSignature) {
        log.info("security");
        return true;
    }
}
