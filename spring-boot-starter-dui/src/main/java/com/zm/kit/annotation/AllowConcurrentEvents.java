package com.zm.kit.annotation;

import com.google.common.annotations.Beta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解标记请求订阅者方法为线程安全方法，RequestBus可以多线程调用<p>
 * 注解不能单独使用，需要配合{@link SkillSubscribe}注解一起使用<p>
 * Marks an event subscriber method as being thread-safe. This annotation indicates that RequestBus
 * may invoke the event subscriber simultaneously from multiple threads.
 *
 * <p>This does not mark the method, and so should be used in combination with {@link SkillSubscribe}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Beta
public @interface AllowConcurrentEvents {
}