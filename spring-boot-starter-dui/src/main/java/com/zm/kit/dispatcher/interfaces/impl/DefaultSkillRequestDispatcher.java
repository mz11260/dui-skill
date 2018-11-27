package com.zm.kit.dispatcher.interfaces.impl;

import com.zm.kit.dispatcher.interfaces.SkillRequestDispatcher;
import com.zm.protocol.ResponseBuilder;
import com.zm.protocol.exception.ProtocolException;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.response.SkillResponse;

/**
 * Created by Administrator on 2018/11/23.
 */
public class DefaultSkillRequestDispatcher implements SkillRequestDispatcher {
    @Override
    public SkillResponse startRequest(SkillRequest request) throws ProtocolException {
        return ResponseBuilder.playSpeak("欢迎使用本技能，试试和我对话吧");
    }

    @Override
    public SkillResponse endRequest(SkillRequest request) throws ProtocolException {
        return ResponseBuilder.playSpeak("再见");
    }

    @Override
    public SkillResponse unknownRequest(SkillRequest request) throws ProtocolException {
        return ResponseBuilder.playSpeak("我好像不明白你在说什么");
    }
}
