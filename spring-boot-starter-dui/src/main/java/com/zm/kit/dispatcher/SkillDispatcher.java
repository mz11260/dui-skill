package com.zm.kit.dispatcher;

import com.google.common.collect.Queues;
import com.zm.protocol.response.SkillResponse;

import java.util.Iterator;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Handler for dispatching events to subscribers, providing different event ordering guarantees that
 * make sense for different situations.
 *
 * <p><b>Note:</b> The dispatcher is orthogonal to the subscriber's {@code Executor}. The dispatcher
 * controls the order in which events are dispatched, while the executor controls how (i.e. on which
 * thread) the subscriber is actually called when an event is dispatched to it.
 *
 * @author Colin Decker
 */
abstract class SkillDispatcher {
    /**
     * Returns a dispatcher that queues events that are posted reentrantly on a thread that is already
     * dispatching an event, guaranteeing that all events posted on a single thread are dispatched to
     * all subscribers in the order they are posted.
     *
     * <p>When all subscribers are dispatched to using a <i>direct</i> executor (which dispatches on
     * the same thread that posts the event), this yields a breadth-first dispatch order on each
     * thread. That is, all subscribers to a single event A will be called before any subscribers to
     * any events B and C that are posted to the event bus by the subscribers to A.
     */
    static SkillDispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    /**
     * Returns a dispatcher that dispatches events to subscribers immediately as they're posted
     * without using an intermediate queue to change the dispatch order. This is effectively a
     * depth-first dispatch order, vs. breadth-first when using a queue.
     */
    @SuppressWarnings("unused")
    static SkillDispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }

    /**
     * Dispatches the given {@code event} to the given {@code subscribers}.
     */
    //abstract void dispatch(Iterator<SkillSubscriber> subscribers);
    abstract SkillResponse dispatch(SkillSubscriber subscriber);

    /**
     * Implementation of a {@link #perThreadDispatchQueue()} dispatcher.
     */
    private static final class PerThreadQueuedDispatcher extends SkillDispatcher {

        // This dispatcher matches the original dispatch behavior of RequestBus.

        /**
         * Per-thread queue of events to dispatch.
         */
        private final ThreadLocal<Queue<Event>> queue =
                new ThreadLocal<Queue<Event>>() {
                    @Override
                    protected Queue<Event> initialValue() {
                        return Queues.newArrayDeque();
                    }
                };

        /*private final ThreadLocal<Queue<EventSet>> queueSet =
                new ThreadLocal<Queue<EventSet>>() {
                    @Override
                    protected Queue<EventSet> initialValue() {
                        return Queues.newArrayDeque();
                    }
                };*/

        /**
         * 线程调度状态，用于避免请求事件的重复调用
         * Per-thread dispatch state, used to avoid reentrant event dispatching.
         */
        private final ThreadLocal<Boolean> dispatching =
                new ThreadLocal<Boolean>() {
                    @Override
                    protected Boolean initialValue() {
                        return false;
                    }
                };

        /*@Override
        void dispatch(Iterator<SkillSubscriber> subscribers) {
            checkNotNull(subscribers);
            Queue<EventSet> queueForThread = queueSet.get();
            queueForThread.offer(new EventSet(subscribers));
            if (!dispatching.get()) {
                dispatching.set(true);
                try {
                    EventSet nextEvent;
                    while ((nextEvent = queueForThread.poll()) != null) {
                        while (nextEvent.subscribers.hasNext()) {
                            nextEvent.subscribers.next().dispatchEvent();
                        }
                    }
                } finally {
                    dispatching.remove();
                    queue.remove();
                }
            }
        }*/

        @Override
        SkillResponse dispatch(SkillSubscriber subscriber) {
            checkNotNull(subscriber);
            Queue<Event> queueForThread = queue.get();
            queueForThread.offer(new Event(subscriber));
            if (!dispatching.get()) {
                dispatching.set(true);
                try {
                    Event nextEvent;
                    if ((nextEvent = queueForThread.poll()) != null) {
                        return nextEvent.subscriber.dispatchEvent();
                    }
                } finally {
                    dispatching.remove();
                    queue.remove();
                }
            }
            return null;
        }

        private static final class Event {
            private final SkillSubscriber subscriber;

            private Event(SkillSubscriber subscriber) {
                this.subscriber = subscriber;
            }

        }

        private static final class EventSet {
            private final Iterator<SkillSubscriber> subscribers;

            private EventSet(Iterator<SkillSubscriber> subscribers) {
                this.subscribers = subscribers;
            }

        }
    }

    /**
     * Implementation of {@link #immediate()}.
     */
    private static final class ImmediateDispatcher extends SkillDispatcher {

        private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();

        @Override
        SkillResponse dispatch(SkillSubscriber subscriber) {
            return subscriber.dispatchEvent();
        }

        /*@Override
        void dispatch(Iterator<SkillSubscriber> subscribers) {
            while (subscribers.hasNext()) {
                subscribers.next().dispatchEvent();
            }
        }*/
    }
}
