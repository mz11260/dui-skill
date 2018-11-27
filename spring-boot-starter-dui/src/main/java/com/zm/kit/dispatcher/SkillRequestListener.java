package com.zm.kit.dispatcher;


import com.zm.kit.dispatcher.interfaces.SkillRequestDispatcher;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.response.SkillResponse;
import org.springframework.context.ApplicationContext;

/**
 * <p>技能请求监听</p>
 * <p>监听所有{@code RequestBus}分发的请求</p>
 *
 * skill request listener
 */
public class SkillRequestListener {

    /**
     * 请求分发接口
     */
    private final SkillRequestDispatcher skillRequestDispatcher;


    /**
     * 请求监听构造方法
     * 根据类型初始化请求接口
     * @param applicationContext context
     */
    SkillRequestListener(ApplicationContext applicationContext) {
        this.skillRequestDispatcher = applicationContext.getBean(SkillRequestDispatcher.class);
    }

    /**
     * 请求监听构造方法
     * 根据beanName初始化请求接口 beanName规则(技能名称 + 接口类名)
     * @param applicationContext context
     */
    SkillRequestListener(ApplicationContext applicationContext, String beanNamePrefix) {
        this.skillRequestDispatcher = (SkillRequestDispatcher) applicationContext.getBean(beanNamePrefix + "SkillRequestDispatcher");
    }


    /*===========================SKILL REQUEST===========================*/
    /**
     * 启动请求
     * @param request skill request
     * @return response
     */
    public SkillResponse receiveStartRequest(SkillRequest request) throws Exception {
        return skillRequestDispatcher.startRequest(request);
    }
    /**
     * 退出请求
     * @param request skill request
     * @return response
     */
    public SkillResponse receiveEndRequest(SkillRequest request) throws Exception {
        return skillRequestDispatcher.endRequest(request);
    }
    /**
     * 未知请求
     * @param request skill request
     */
    public SkillResponse receiveUnknown(SkillRequest request) throws Exception {
        return skillRequestDispatcher.unknownRequest(request);
    }
    /*===========================SKILL REQUEST===========================*/


}
