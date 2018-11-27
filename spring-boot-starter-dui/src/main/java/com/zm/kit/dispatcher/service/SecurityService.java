package com.zm.kit.dispatcher.service;


/**
 * 签名认证服务<p>
 * 可以在请求时在http request header中添加签名
 */
public interface SecurityService {
    boolean security(String headerSignature);
}
