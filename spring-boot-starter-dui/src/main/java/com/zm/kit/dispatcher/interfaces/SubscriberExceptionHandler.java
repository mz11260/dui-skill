package com.zm.kit.dispatcher.interfaces;

import com.zm.protocol.response.SkillResponse;

/**
 * Created by Administrator on 2018/11/23.
 *
 * Handler for exceptions thrown by event subscribers.
 *
 */
public interface SubscriberExceptionHandler {

    /**
     * Handles exceptions thrown by subscribers.
     */
    SkillResponse handleException(Throwable exception);

}
