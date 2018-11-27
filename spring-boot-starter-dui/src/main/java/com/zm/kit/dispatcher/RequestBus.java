package com.zm.kit.dispatcher;

import com.google.common.base.MoreObjects;
import com.google.common.util.concurrent.MoreExecutors;
import com.zm.kit.constants.Constants;
import com.zm.kit.constants.RequestType;
import com.zm.kit.dispatcher.interfaces.SubscriberExceptionHandler;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.request.nodes.Request;
import com.zm.protocol.response.SkillResponse;
import com.zm.starter.SkillRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 请求分发总线
 */
@Slf4j
public class RequestBus {

    private final String identifier;
    private final ExecutorService executor;
    private final SubscriberExceptionHandler exceptionHandler;
    private final SkillDispatcher dispatcher;
    private SkillRequest request;

    private SkillRequestListener listener;

    public String identifier() {
        return identifier;
    }
    public ExecutorService executor() {
        return executor;
    }
    public SubscriberExceptionHandler exceptionHandler() {
        return exceptionHandler;
    }
    public SkillRequest getRequest() {
        return request;
    }
    public void setRequest(SkillRequest request) {
        this.request = request;
    }

    /**
     * 分发入口
     * @param request skill request
     * @return skill response
     */
    public SkillResponse service(SkillRequest request) {
        RequestType type = request.getRequest().getType();
        try {
            switch (type) {
                case END:
                    return listener.receiveEndRequest(request);
                case START:
                    return listener.receiveStartRequest(request);
                case CONTINUE:
                    SkillSubscriber eventSubscriber = subscribers.getSubscriber(getSubscriberKey(request));
                    if (eventSubscriber != null) {
                        this.setRequest(request);
                        return dispatcher.dispatch(eventSubscriber);
                    }
                    // 无法识别的意图
                    return listener.receiveUnknown(request);
                default:
                    return listener.receiveUnknown(request);
            }
        } catch (Exception e) {
            return exceptionHandler.handleException(e);
        }
    }

    /**
     * 根据意图获取请求订阅方法key
     * @param request skill request
     * @return the subscriber key
     */
    private String getSubscriberKey(SkillRequest request) {
        StringBuilder key = new StringBuilder().append(request.getRequest().getTask()).append(Constants.SUBSCRIBER_SEPARATOR);
        List<Request.Slot> slots = request.getRequest().getInputs().get(0).getSlots();
        for (Request.Slot slot : slots) {
            if (slot.getName().equals("intent")) {
                key.append(slot.getValue());
                break;
            }
        }
        return key.toString();
    }


    /**
     * 技能请求总线构造方法
     * 根据beanName初始化属性
     * @param applicationContext context
     * @param identifier 总线名称/技能名称, 即注解{@link SkillRequestHandler}标记的名称
     */
    public RequestBus(ApplicationContext applicationContext, String identifier) {

        this.identifier = checkNotNull(identifier);
        this.executor = MoreExecutors.newDirectExecutorService();
        this.dispatcher = SkillDispatcher.perThreadDispatchQueue();

        this.exceptionHandler = (SubscriberExceptionHandler) applicationContext.getBean(identifier + "SubscriberExceptionHandler");

        this.listener = new SkillRequestListener(applicationContext, identifier);
    }

    /**
     * Creates a new RequestBus named "default".
     */
    public RequestBus(ApplicationContext applicationContext) {
        this("default", MoreExecutors.newDirectExecutorService(), SkillDispatcher.perThreadDispatchQueue(), applicationContext);
    }

    /**
     * Creates a new RequestBus with the given {@code identifier}.
     *
     * @param identifier a brief name for this bus, for logging purposes. Should be a valid Java
     *                   identifier.
     */
    public RequestBus(String identifier, ExecutorService executor, SkillDispatcher dispatcher, ApplicationContext applicationContext) {
        this.identifier = checkNotNull(identifier);
        this.executor = checkNotNull(executor);
        this.dispatcher = checkNotNull(dispatcher);
        this.exceptionHandler = applicationContext.getBean(SubscriberExceptionHandler.class);

        this.listener = new SkillRequestListener(applicationContext);
    }

    /**
     * 注册组件实例对象
     */
    private final SkillSubscriberRegistry subscribers = new SkillSubscriberRegistry(this);


    /**
     * Handles the given exception thrown by a subscriber with the given context.
     */
    public SkillResponse handleSubscriberException(Throwable e) {
        checkNotNull(e);
        //checkNotNull(context);
        try {
            return exceptionHandler.handleException(e);
        } catch (Throwable e2) {
            log.info(String.format(Locale.ROOT, "Exception %s thrown while handling exception: %s", e2, e), e2);
            // if the handler threw an exception... well, just log it
            return null;
        }
    }

    /**
     * Registers all subscriber methods on {@code object} to receive events.
     *
     * @param object object whose subscriber methods should be registered.
     */
    public void register(Object object) {
        subscribers.register(object);
    }

    /**
     * Unregisters all subscriber methods on a registered {@code object}.
     *
     * @param object object whose subscriber methods should be unregistered.
     * @throws IllegalArgumentException if the object was not previously registered.
     */
    public void unregister(Object object) {
        subscribers.unregister(object);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(identifier).toString();
    }


}
