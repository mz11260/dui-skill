package com.zm.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

/**
 * skill spring boot 自动配置类
 */
@Slf4j
@Configuration
public class SkillAutoConfigure {

    /**
     * 请求处理程序发现组件
     * @return SkillRequestHandlerDiscoverer
     */
    @Bean
    @ConditionalOnMissingBean
    public SkillRequestHandlerDiscoverer handlerDiscoverer() {
        return new SkillRequestHandlerDiscoverer();
    }

}
