package com.zm.kit.dispatcher;

import com.zm.kit.annotation.AllowConcurrentEvents;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.response.SkillResponse;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class SkillSubscriber {

    /**
     * Creates a {@code SkillSubscriber} for {@code method} on {@code listener}.
     */
    public static SkillSubscriber create(RequestBus bus, Object listener, Method method) {
        return isDeclaredThreadSafe(method)? new SkillSubscriber(bus, listener, method): new SynchronizedSubscriber(bus, listener, method);
    }

    /**
     * 请求处理总线
     *
     * The request bus this subscriber belongs to.
     */
    private RequestBus bus;

    /**
     * 订阅者方法的目标对象
     */
    public final Object target;

    /**
     * 标注了{@code @SkillSubscribe}的方法
     */
    private final Method method;

    /**
     * Executor to use for dispatching events to this subscriber.
     */
    private final ExecutorService executor;


    private SkillSubscriber(RequestBus bus, Object target, Method method) {
        this.bus = bus;
        this.target = checkNotNull(target);
        this.method = method;
        method.setAccessible(true);
        this.executor = bus.executor();
    }

    /**
     * Dispatches {@code event} to this subscriber using the proper executor.
     */
    final SkillResponse dispatchEvent() {
        final SkillRequest request = bus.getRequest();
        Future future = executor.submit(new Callable() {
            @Override
            public Object call() {
                try {
                    return invokeSubscriberMethod(request);
                } catch (InvocationTargetException e) {
                    return bus.handleSubscriberException(e.getCause());
                }
            }
        });
        try {
            return (SkillResponse) future.get(10000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("get Future result error: ", e);
        }
        return null;
    }

    /**
     * 从请求实例中实例化异常处理上下文对象
     * Gets the context for the given event.
     */
    private SubscriberExceptionContext context(SkillRequest event) {
        return new SubscriberExceptionContext(bus, event, target, method);
    }

    /**
     * Invokes the subscriber method. This method can be overridden to make the invocation
     * synchronized.
     */
    SkillResponse invokeSubscriberMethod(SkillRequest request) throws InvocationTargetException {
        try {
            return (SkillResponse) method.invoke(target, checkNotNull(request));
        } catch (IllegalArgumentException e) {
            throw new Error("Method rejected target/argument: " + request, e);
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + request, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }

    @Override
    public final int hashCode() {
        return (31 + method.hashCode()) * 31 + System.identityHashCode(target);
    }

    @Override
    public final boolean equals(@Nullable Object obj) {
        if (obj instanceof SkillSubscriber) {
            SkillSubscriber that = (SkillSubscriber) obj;
            // Use == so that different equal instances will still receive events.
            // We only guard against the case that the same object is registered
            // multiple times
            return target == that.target && method.equals(that.method);
        }
        return false;
    }

    /**
     * Checks whether {@code method} is thread-safe, as indicated by the presence of the
     * {@link AllowConcurrentEvents} annotation.
     */
    private static boolean isDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }

    /**
     * SkillSubscriber that synchronizes invocations of a method to ensure that only one thread may enter
     * the method at a time.
     */
    static final class SynchronizedSubscriber extends SkillSubscriber {
        private SynchronizedSubscriber(RequestBus bus, Object target, Method method) {
            super(bus, target, method);
        }
        @Override
        SkillResponse invokeSubscriberMethod(SkillRequest request) throws InvocationTargetException {
            synchronized (this) {
                return super.invokeSubscriberMethod(request);
            }
        }
    }
}
