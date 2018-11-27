package com.zm.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zm.common.RedisUtil;
import com.zm.filter.CrossOriginFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 一些通用配置
 */
@Configuration
public class Configs {

    /**
     * redisUtil配置
     * @param redisTemplate redisTemplate
     * @return redisUtil
     */
    @Bean
    public RedisUtil redisUtilConfig (RedisTemplate<String, String> redisTemplate) {
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

    /**
     * 过滤器配置
     */
    @Configuration
    @ConditionalOnClass(CrossOriginFilter.class)
    public static class FilterConfig {
        @Bean
        public FilterRegistrationBean filterRegistrationBean() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(this.skillFilter());
            //registration.addUrlPatterns(URI.SPEECHLET);
            registration.addUrlPatterns("/*");
            registration.setName("skillFilter");
            return registration;
        }

        public Filter skillFilter() {
            return new CrossOriginFilter();
        }
    }

    /**
     * ali fastJson json解析配置
     * 启用该配置将覆盖springMVC默认的jackson解析
     *
     */
    @Configuration
    @ConditionalOnClass(FastJsonHttpMessageConverter.class)
    public static class FastJsonConvertConfig {

        private final ApplicationContext applicationContext;
        public FastJsonConvertConfig(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        private static boolean romePresent =
                ClassUtils.isPresent("com.rometools.rome.feed.WireFeed",
                        Configs.class.getClassLoader());

        private static final boolean jaxb2Present =
                ClassUtils.isPresent("javax.xml.bind.Binder",
                        Configs.class.getClassLoader());

        private static final boolean jackson2XmlPresent =
                ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper",
                        Configs.class.getClassLoader());
        /**
         * 添加一组默认的HttpMessageConverter
         * @param messageConverters 需要添加默认消息转换器的集合
         */
        protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
            messageConverters.add(new ByteArrayHttpMessageConverter());
            messageConverters.add(new ResourceHttpMessageConverter());
            messageConverters.add(new SourceHttpMessageConverter<>());
            messageConverters.add(new AllEncompassingFormHttpMessageConverter());
            if (romePresent) {
                messageConverters.add(new AtomFeedHttpMessageConverter());
                messageConverters.add(new RssChannelHttpMessageConverter());
            }
            if (jackson2XmlPresent) {
                messageConverters.add(new MappingJackson2XmlHttpMessageConverter(
                        Jackson2ObjectMapperBuilder.xml().applicationContext(this.applicationContext).build()));
            } else if (jaxb2Present) {
                messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
            }
        }

        /**
         * 注入fastJsonHttpMessageConvert
         */
        @Bean
        public <T> HttpMessageConverters fastJsonHttpMessageConverters(){

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

            // 定义转换消息对象 FastJsonHttpMessageConverter
            FastJsonHttpMessageConverter fastJsonConvert = new FastJsonHttpMessageConverter();

            // 创建fastJson配置信息
            FastJsonConfig config = new FastJsonConfig();
            config.setCharset(Charset.forName("UTF-8"));
            config.setDateFormat("yyyy-MM-dd HH:mm:ss");
            // 美化输出
            //config.setSerializerFeatures(SerializerFeature.PrettyFormat);

            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

            // Convert中添加fastJson配置
            fastJsonConvert.setSupportedMediaTypes(mediaTypes);
            fastJsonConvert.setFastJsonConfig(config);

            StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("utf-8"));
            stringConverter.setWriteAcceptCharset(false);


            messageConverters.add(stringConverter);
            messageConverters.add(fastJsonConvert);

            // add default converters
            this.addDefaultHttpMessageConverters(messageConverters);

            // default converters has been added, so set to false
            return new HttpMessageConverters(false, messageConverters);
        }
    }

    /**
     * 跨域配置
     * 启用后所有链接都支持跨域访问
     *
     * 2018-07-30 废弃， 改为使用拦截器
     */
    // @Configuration
    // @ConditionalOnClass(CorsFilter.class)
    @Deprecated
    public static class CrossOriginConfig {
        /**
         * 注入CORS过滤器
         */
        // @Bean
        public CorsFilter crossOriginConfig() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", buildConfig());
            return new CorsFilter(source);
        }

        /**
         * 返回CORS配置
         * @return CorsConfiguration
         */
        private CorsConfiguration buildConfig() {
            // 允许跨域
            //@CrossOrigin(maxAge = 1800, origins = "*", allowCredentials = "true", allowedHeaders = {"*"},
            //        methods = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.POST})
            CorsConfiguration config = new CorsConfiguration();

            config.addAllowedOrigin("*"); // 允许跨域的站点（*表示全部）
            //config.addAllowedHeader("*"); // 允许的HTTP请求头
            config.addAllowedHeader("Access-Control-Allow-Headers");
            config.addAllowedHeader("Origin");
            config.addAllowedHeader("Accept");
            config.addAllowedHeader("X-Requested-With");
            config.addAllowedHeader("Content-Type");
            config.addAllowedHeader("Access-Control-Request-Method");
            config.addAllowedHeader("Access-Control-Request-Headers");
            config.addAllowedHeader("appcode");
            config.addAllowedHeader("appversion");
            config.addAllowedHeader("languagetype");
            config.addAllowedHeader("devicetype");
            config.addAllowedHeader("devicemodel");
            config.addAllowedHeader("sys");
            config.addAllowedHeader("sysversion");
            config.addAllowedHeader("deviceidentifier");
            config.addAllowedHeader("service");
            config.addAllowedHeader("sign");
            config.addAllowedHeader("noncestr");
            config.addAllowedHeader("timestamp");
            config.addAllowedHeader("action");
            config.addAllowedHeader("page");
            config.addAllowedHeader("pagesize");
            config.addAllowedHeader("token");



            /*List<String> list = new ArrayList<>();
            list.add("GET");
            list.add("HEAD");
            list.add("OPTIONS");
            list.add("POST"); //GET,HEAD,OPTIONS,POST,PUT
            list.add("PUT");*/
            //config.setAllowedMethods(list);// 允许的请求方式

            config.addAllowedMethod("GET");
            config.addAllowedMethod("HEAD");
            config.addAllowedMethod("OPTIONS");
            config.addAllowedMethod("POST");
            config.addAllowedMethod("PUT");
            config.setAllowCredentials(true); // 允许发送Cookie
            /*
             * 用来指定‘OPTIONS预检请求’的有效期，单位为秒
             * 表示允许这个‘OPTIONS预检请求’的参数缓存的秒数，在此期间，不用发出另一条预检请求。
             */
            //config.setMaxAge(3600L);
            return config;
        }
    }

}
