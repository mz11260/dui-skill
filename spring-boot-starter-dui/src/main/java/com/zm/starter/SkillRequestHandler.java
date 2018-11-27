package com.zm.starter;

import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于 技能 服务请求处理程序上的注解，包括意图请求和事件请求<p>
 * 标记该注解会被spring扫描并初始化<p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface SkillRequestHandler {

    /** 技能名称 */
    String name();
}
