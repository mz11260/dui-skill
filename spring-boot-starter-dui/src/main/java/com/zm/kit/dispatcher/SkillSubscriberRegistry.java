package com.zm.kit.dispatcher;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.zm.kit.annotation.SkillSubscribe;
import com.zm.kit.constants.Constants;
import com.zm.protocol.request.SkillRequest;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 * <p>请求处理订阅者注册组件</p>
 * <p>将所有intent和event相关的请求注册到订阅者组件中</p>
 * <p>再通过订阅者匹配到相应的方法执行调用</p>
 *
 * <p>Registry of subscribers to a single skill dispatcher.</p>
 */
public class SkillSubscriberRegistry {

    /**
     * All registered subscribers, indexed by event type.
     *
     * <p>The {@link CopyOnWriteArraySet} values make it easy and relatively lightweight to get an
     * immutable snapshot of all current subscribers to an event without any locking.
     */
    private final ConcurrentMap<String, CopyOnWriteArraySet<SkillSubscriber>> subscriber_set = Maps.newConcurrentMap();

    /**
     * 每个key对应一个subscriber订阅者，一个订阅者对应只有一个事件执行方法
     *
     */
    private final ConcurrentMap<String, SkillSubscriber> subscribers = Maps.newConcurrentMap();

    /**
     * The skill request bus this registry belongs to.
     */
    private final RequestBus bus;
    SkillSubscriberRegistry(RequestBus bus) {
        this.bus = bus;
    }

    /**
     * 注册订阅者方法
     * Registers all subscriber methods on the given listener object.
     */
    public void register_set(Object listener) {
        Multimap<String, SkillSubscriber> listenerMethods = findAllSubscribers(listener);
        for (Map.Entry<String, Collection<SkillSubscriber>> entry : listenerMethods.asMap().entrySet()) {
            String eventType = entry.getKey();
            Collection<SkillSubscriber> eventMethodsInListener = entry.getValue();
            CopyOnWriteArraySet<SkillSubscriber> eventSubscribers = subscriber_set.get(eventType);
            if (eventSubscribers == null) {
                CopyOnWriteArraySet<SkillSubscriber> newSet = new CopyOnWriteArraySet<>();
                eventSubscribers = MoreObjects.firstNonNull(subscriber_set.putIfAbsent(eventType, newSet), newSet);
            }
            eventSubscribers.addAll(eventMethodsInListener);
        }
    }

    /**
     * 注册订阅者方法
     * Registers all subscriber methods on the given listener object.
     */
    public void register(Object listener) {
        Multimap<String, SkillSubscriber> listenerMethods = findAllSubscribers(listener);
        for (Map.Entry<String, Collection<SkillSubscriber>> entry : listenerMethods.asMap().entrySet()) {
            String eventType = entry.getKey();
            Collection<SkillSubscriber> eventMethodsInListener = entry.getValue();
            if (eventMethodsInListener.size() > 1) {
                throw new RuntimeException(String.format("请求处理类 %s 中，意图名称[%s]重复", listener.getClass().getName(), eventType));
            }
            SkillSubscriber eventSubscriber = subscribers.get(eventType);
            if (eventSubscriber == null && eventMethodsInListener.iterator().hasNext()) {
                eventSubscriber = eventMethodsInListener.iterator().next();
                subscribers.putIfAbsent(eventType, eventSubscriber);
            }
        }
    }

    /**
     * Unregisters all subscribers on the given listener object.
     */
    void unregister_set(Object listener) {
        Multimap<String, SkillSubscriber> listenerMethods = findAllSubscribers(listener);

        for (Map.Entry<String, Collection<SkillSubscriber>> entry : listenerMethods.asMap().entrySet()) {
            String eventType = entry.getKey();
            Collection<SkillSubscriber> listenerMethodsForType = entry.getValue();

            CopyOnWriteArraySet<SkillSubscriber> currentSubscribers = subscriber_set.get(eventType);
            if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType)) {
                // if removeAll returns true, all we really know is that at least one subscriber was
                // removed... however, barring something very strange we can assume that if at least one
                // subscriber was removed, all subscribers on listener for that event type were... after
                // all, the definition of subscribers on a particular class is totally static
                throw new IllegalArgumentException(
                        "missing event subscriber for an annotated method. Is " + listener + " registered?");
            }

            // don't try to remove the set if it's empty; that can't be done safely without a lock
            // anyway, if the set is empty it'll just be wrapping an array of length 0
        }
    }

    /**
     * Unregisters all subscribers on the given listener object.
     */
    void unregister(Object listener) {
        Multimap<String, SkillSubscriber> listenerMethods = findAllSubscribers(listener);

        for (Map.Entry<String, Collection<SkillSubscriber>> entry : listenerMethods.asMap().entrySet()) {
            String eventType = entry.getKey();
            Collection<SkillSubscriber> listenerMethodsForType = entry.getValue();

            SkillSubscriber currentSubscribers = subscribers.get(eventType);
            if (currentSubscribers == null || !subscribers.remove(eventType, currentSubscribers)) {
                throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + listener + " registered?");
            }
        }
    }

    /**
     * Gets an iterator representing an immutable snapshot of all subscribers to the given event at
     * the time this method is called.
     */
    public Iterator<SkillSubscriber> getSubscribers(String event) {
        return subscriber_set.get(event).iterator();
    }

    /**
     * 获取请求订阅者
     * @param event key
     * @return SkillSubscriber
     */
    public SkillSubscriber getSubscriber(String event) {
        return subscribers.get(event);
    }

    /**
     * A thread-safe cache that contains the mapping from each class to all methods in that class and
     * all super-classes, that are annotated with {@code @SkillSubscribe}. The cache is shared across
     * all instances of this class; this greatly improves performance if multiple RequestBus
     * instances are created and objects of the same class are registered on all of them.
     */
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache =
            CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, ImmutableList<Method>>() {
                @Override
                public ImmutableList<Method> load(Class<?> concreteClass) throws Exception {
                    return getAnnotatedMethodsNotCached(concreteClass);
                }
            });


    private Multimap<String, SkillSubscriber> findAllSubscribers(Object listener) {
        Multimap<String, SkillSubscriber> methodsInListener = HashMultimap.create();
        Class<?> clazz = listener.getClass();
        for (Method method : getAnnotatedMethods(clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException("the subscriber method must have only one parameter");
            }
            if (parameterTypes[0] != SkillRequest.class) {
                throw new RuntimeException("the subscriber method's parameter must be SkillRequest.class");
            }

            SkillSubscribe subscribe = method.getAnnotation(SkillSubscribe.class);

            methodsInListener.put(subscribe.task() + Constants.SUBSCRIBER_SEPARATOR + subscribe.name(),
                    SkillSubscriber.create(bus, listener, method));
        }
        return methodsInListener;
    }

    private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
        return subscriberMethodsCache.getUnchecked(clazz);
    }

    /**
     * 获取给定类中的所有标注了{@code @IntentMethod}和{@code @EventMethod}且没有缓存到
     *
     * @param clazz class
     * @return list
     */
    private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
        Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
        Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
        for (Class<?> supertype : supertypes) {
            for (Method method : supertype.getDeclaredMethods()) {
                if ((method.isAnnotationPresent(SkillSubscribe.class) && !method.isSynthetic())) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    checkArgument(parameterTypes.length == 1,
                            "Method %s has @SkillSubscribe annotation but has %s parameters.SkillSubscribe methods must have exactly 1 parameter.",
                            method, parameterTypes.length);
                    MethodIdentifier ident = new MethodIdentifier(method);
                    if (!identifiers.containsKey(ident)) {
                        identifiers.put(ident, method);
                    }
                }
            }
        }
        return ImmutableList.copyOf(identifiers.values());
    }

    /**
     * 方法标识对象
     */
    private static final class MethodIdentifier {

        private final String name;
        private final List<Class<?>> parameterTypes;

        MethodIdentifier(Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name, parameterTypes);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (o instanceof MethodIdentifier) {
                MethodIdentifier ident = (MethodIdentifier) o;
                return name.equals(ident.name) && parameterTypes.equals(ident.parameterTypes);
            }
            return false;
        }
    }
}
