package com.zm.starter;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.List;

/**
 * 请求处理程序发现组件<p>
 * 找到标记了@{@link SkillRequestHandler}注解的bean<p>
 */
@Slf4j
public class SkillRequestHandlerDiscoverer implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private BeanDefinitionRegistry beanDefinitionRegistry;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.beanDefinitionRegistry = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }

    /**
     * 找到所有标记了SkillRequestHandler注解的请求处理程序
     * @return skill request service list
     */
    public Collection<Object> findSkillRequestHandler() {
        String[] beanNames = this.applicationContext.getBeanNamesForAnnotation(SkillRequestHandler.class);
        List<Object> beans = Lists.newArrayList();
        for (String beanName : beanNames) {
            beans.add(this.applicationContext.getBean(beanName));
        }
        return beans;
    }

    /**
     * 根据技能名称找到所有标记了SkillRequestHandler注解的请求处理程序
     * @return skill request service list
     */
    public Collection<Object> findSkillRequestHandlerByName(String name) {
        String[] beanNames = this.applicationContext.getBeanNamesForAnnotation(SkillRequestHandler.class);
        List<Object> beans = Lists.newArrayList();
        for (String beanName : beanNames) {
            Object object = this.applicationContext.getBean(beanName);
            SkillRequestHandler skillRequestHandler = object.getClass().getAnnotation(SkillRequestHandler.class);
            if (name.equals(skillRequestHandler.name())) {
                log.info(object.getClass().getPackage().getName());
                beans.add(object);
            }
        }
        return beans;
    }

    /**
     * get spring application context
     * @return applicationContext
     */
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    /**
     * 获取bean
     * @param clazz 要匹配的类型，可以是接口或父类
     * @return an instance of the single bean matching the required type
     */
    public <T> T  getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            log.warn("根据类型[{}]获取Bean失败：{}", clazz.getName(), e.getMessage());
            return null;
        }
    }


    /**
     * 注册bean到spring容器
     * @param name bean name
     * @param clazz bean class
     */
    public void registerBean(String name, Class<?> clazz) {
        //创建beanBuilder
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        //注册bean
        beanDefinitionRegistry.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());


    }
    /**
     * Remove the BeanDefinition for the given name.
     * @param beanName the name of the bean instance to register
     */
    public void unregisterBean(String beanName){
        beanDefinitionRegistry.removeBeanDefinition(beanName);
    }
}
