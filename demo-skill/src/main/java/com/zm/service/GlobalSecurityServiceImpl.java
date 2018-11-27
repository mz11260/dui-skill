package com.zm.service;

import com.zm.kit.dispatcher.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 签名认证服务
 */
@Service
@Slf4j
public class GlobalSecurityServiceImpl implements SecurityService {

    @Override
    public boolean security(String headerSignature) {
        return true;
    }
}
