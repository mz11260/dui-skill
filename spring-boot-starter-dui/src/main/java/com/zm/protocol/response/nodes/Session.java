package com.zm.protocol.response.nodes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>当前应用的 session，用于保存上下文信息</p>
 * <p>CloudApp 可以在 attributes 里填充自己需要的上下文信息用于后面的请求</p>
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Session {

    /** 希望用户下一句话的意图 */
    private List<String> nextIntents;
    /** 上下文信息 */
    private LinkedHashMap<String, Object> attributes;
}
