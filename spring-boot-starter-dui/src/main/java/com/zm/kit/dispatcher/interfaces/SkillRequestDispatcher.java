package com.zm.kit.dispatcher.interfaces;

import com.zm.protocol.exception.ProtocolException;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.response.SkillResponse;

/**
 * 技能请求分发
 * Created by Administrator on 2018/11/23.
 */
public interface SkillRequestDispatcher {

    /**
     * 开始请求
     */
    SkillResponse startRequest(SkillRequest request) throws ProtocolException;

    /**
     * 结束请求
     */
    SkillResponse endRequest(SkillRequest request) throws ProtocolException;

    /**
     * 未知请求
     */
    SkillResponse unknownRequest(SkillRequest request) throws ProtocolException;
}
