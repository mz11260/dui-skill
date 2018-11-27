package com.zm.starter;

import com.zm.kit.dispatcher.RequestBus;
import com.zm.kit.dispatcher.service.SecurityService;
import com.zm.kit.dispatcher.service.impl.DefaultSecurityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 技能配置<p>
 * 1、通用事件处理接口和签名认证接口所有技能可以通用, 按类型实例化一个bean即可.<p>
 * 2、请求总线和其它4个关键接口每个技能都需要独立的bean, 所以需要按名称分别实例化bean.<p>
 * 3、bean名称规则为{@link SkillRequestHandler}注解属性name值 + 接口类名(不是实现类)或请求总线类名称(RequestBus).<p>
 * Created by Administrator on 2018/9/5.
 */
@Configuration
@ConditionalOnBean(SkillRequestHandlerDiscoverer.class)
@Slf4j
public class SkillConfigure {

    /**
     * 通用接口配置<p>
     * 所有技能可以通用的接口<P>
     * @see SecurityService
     */
    @Configuration
    static class CommonConfiguration {
        @Bean
        @ConditionalOnMissingBean(SecurityService.class)
        public SecurityService securityService() {
            log.warn("没有通用签名认证服务接口实现[{}]，启用默认实现。", SecurityService.class);
            return new DefaultSecurityServiceImpl();
        }
    }

    /**
     * 示例技能配置
     * <dl>
     *     <dt>{@link ConditionalOnBean}条件说明</dt>
     *     <dd>仅当BeanFactor中已经包含指定的bean时改配置才生效</dd>
     *     <dt>{@link ConditionalOnMissingBean}条件说明</dt>
     *     <dd>仅当BeanFactory中不包含指定的bean时条件配置才生效，
     *     这两个注解只能匹配到目前为止application context已经处理的bean定义</dd>
     * </dl>
     */
    @Configuration
    @ConditionalOnMissingBean(name = "demoRequestBus")
    static class DemoRequestBusConfiguration {

        private final SkillRequestHandlerDiscoverer discoverer;
        /**
         * 初始化4个关键接口
         */
        public DemoRequestBusConfiguration(SkillRequestHandlerDiscoverer discoverer) {
            this.discoverer = discoverer;
        }

        @Bean(name = "demoRequestBus")
        public RequestBus requestBus() {
            log.info("示例技能, 初始化请求总线...");
            RequestBus bus = new RequestBus(discoverer.getApplicationContext(), "demo");
            registerHandlers(bus, discoverer, "demo");
            return bus;
        }
    }


    /**
     * 注册请求处理组件到分发总线
     * @param bus 分发总线
     * @param discoverer 请求处理发现组件
     */
    private static void registerHandlers(RequestBus bus, SkillRequestHandlerDiscoverer discoverer, String name) {
        for (Object object : discoverer.findSkillRequestHandlerByName(name)) {
            bus.register(object);
        }
    }
}
