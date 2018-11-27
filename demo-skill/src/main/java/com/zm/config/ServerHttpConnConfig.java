package com.zm.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * tomcat http 连接配置
 */
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
@ConditionalOnProperty(name = "server.http.port") // 有http端口，配置生效
@Slf4j
public class ServerHttpConnConfig {
    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory(HttpProperties properties){
        try {
            TomcatEmbeddedServletContainerFactory tomcatConfig = new TomcatEmbeddedServletContainerFactory();
            tomcatConfig.addAdditionalTomcatConnectors(this.newHttpConnector(properties));
            return tomcatConfig;
        } catch (Exception e) {
            log.error("configuration http connector error: ", e);
            return null;
        }
    }
    private Connector newHttpConnector(HttpProperties properties) {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(properties.getPort());
        connector.setSecure(false);

        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        // 设置最大线程数
        protocol.setMaxThreads(100);
        // 设置初始线程数  最小空闲线程数
        protocol.setMinSpareThreads(20);
        // 设置超时
        protocol.setConnectionTimeout(5000);

        return connector;
    }

    /*
     *
     * @Retention(RetentionPolicy.RUNTIME)
     * @Target({ElementType.TYPE, ElementType.METHOD})
     * @Documented
     * @Conditional({OnPropertyCondition.class})
     * public @interface ConditionalOnProperty {
     *     String[] value() default {};             // property名称，与name不可同时使用
     *
     *     String prefix() default "";              // property名称的前缀，可有可无
     *
     *     String[] name() default {};              // property完整名称或部分名称（可与prefix组合使用，组成完整的property名称），与value不可同时使用
     *
     *     String havingValue() default "";         // 可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置
     *
     *     boolean matchIfMissing() default false;  // 缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错
     *
     *     boolean relaxedNames() default true;     // 是否可以松散匹配
     * }
     *
     */
}
