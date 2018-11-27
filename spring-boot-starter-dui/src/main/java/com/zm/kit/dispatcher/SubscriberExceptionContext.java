package com.zm.kit.dispatcher;


import com.zm.protocol.request.SkillRequest;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * 订阅者方法调用异常处理上下文
 *
 * Context for an exception thrown by a subscriber.
 */
public class SubscriberExceptionContext {
    private final RequestBus eventBus;
    private final SkillRequest event;
    private final Object subscriber;
    private final Method subscriberMethod;

    /**
     * @param eventBus         The {@link RequestBus} that handled the event and the subscriber. Useful for
     *                         broadcasting a a new event based on the error.
     * @param event            The event object that caused the subscriber to throw.
     * @param subscriber       The source subscriber context.
     * @param subscriberMethod the subscribed method.
     */
    SubscriberExceptionContext(
            RequestBus eventBus, SkillRequest event, Object subscriber, Method subscriberMethod) {
        this.eventBus = checkNotNull(eventBus);
        this.event = checkNotNull(event);
        this.subscriber = checkNotNull(subscriber);
        this.subscriberMethod = checkNotNull(subscriberMethod);
    }

    /**
     * @return The {@link RequestBus} that handled the event and the subscriber. Useful for broadcasting
     * a a new event based on the error.
     */
    public RequestBus getEventBus() {
        return eventBus;
    }

    /**
     * @return The event object that caused the subscriber to throw.
     */
    public SkillRequest getEvent() {
        return event;
    }

    /**
     * @return The object context that the subscriber was called on.
     */
    public Object getSubscriber() {
        return subscriber;
    }

    /**
     * @return The subscribed method that threw the exception.
     */
    public Method getSubscriberMethod() {
        return subscriberMethod;
    }
}
