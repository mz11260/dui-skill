package com.zm.kit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 技能请求处理注解<p>
 * 一个技能可以有多个任务，一个任务可以对应一个或多个意图，但一个意图只能对应一个任务。<p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SkillSubscribe {

    /** 任务名称 */
    String task();

    /** 意图名称 */
    String name();

}
