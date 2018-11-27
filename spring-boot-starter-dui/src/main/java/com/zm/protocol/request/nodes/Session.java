package com.zm.protocol.request.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zm.protocol.request.SkillRequest;
import lombok.Data;

import java.util.LinkedHashMap;

/**
 * <p>会话信息</p>
 * <p>上级节点{@link SkillRequest}</p>
 */
@Data
public class Session {
    /**
     * 每次会话的唯一ID，由系统填充
     */
    private String sessionId;

    /**
     * 向CloudApp表明此次会话是新的会话还是已经存在的会话
     */
    @JSONField(name = "new")
    @JsonProperty("new")
    private boolean newSession;

    /**
     * 为CloudApp提供attributes字段留保存上下文信息的字段
     */
    private LinkedHashMap<String, Object> attributes;
}
