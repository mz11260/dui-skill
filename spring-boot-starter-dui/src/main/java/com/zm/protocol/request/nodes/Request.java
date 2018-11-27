package com.zm.protocol.request.nodes;

import com.zm.kit.constants.RequestType;
import com.zm.protocol.request.SkillRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>当前请求的真正内容</p>
 * <p>上级节点{@link SkillRequest}</p>
 * <p>reqType - 表明请求的类型： INTENT 和 EVENT 分别对应 IntentRequest 和 EventRequest</p>
 * <p>reqId - 每次请求都会对应一个唯一ID用以区分每一次的请求</p>
 * <p>content - IntentRequest 和 EventRequest 对应的具体内容</p>
 */
@Data
@EqualsAndHashCode
public class Request {

    /** 当前请求类型 */
    private RequestType type;

    /** 当前请求的唯一ID */
    private String requestId;

    /** 当前任务名称 */
    private String task;

    /** 合并后的slots */
    private List<Slot> slots;

    /** 音频信息, 限内部使用, 只有配置了"透传音频"时才有此键 */
    private Audio audio;

    /** 用户语义解析记录列表，按照从久到近排序 */
    private List<Input> inputs;


    /**
     * 语义槽
     */
    @Data
    @EqualsAndHashCode
    public static class Slot {

        /** 语义槽名称, 其中intent是一种特殊的slot */
        private String name;

        /** 语义槽取值 */
        private String value;

        /** 原始value */
        private String rawvalue;

        /** 原始value的拼音 */
        private String rawpinyin;

        /** value在文本中的位置 */
        private List<Object> pos;

    }
    /**
     * 音频信息<p>
     * 限内部使用, 只有配置了"透传音频"时才有此键
     */
    @Data
    public static class Audio {
        /** 官网没解释 */
        private String audioType;

        /** 官网没解释 */
        private Long sampleRate;

        /** 官网没解释 */
        private Integer channel;

        /** 官网没解释 */
        private Integer sampleBytes;
    }


    /**
     * 用户语义解析记录
     */
    @Data
    public static class Input {

        /** 官网没解释 */
        private String input;

        /** 限内部使用, 只有配置了"透传音频"时才有此键 */
        private Audio audio;

        /** 任务名称 */
        private String task;

        /** 时间戳 */
        private Long timestamp;

        /** 合并后的slot */
        private List<Slot> slots;
    }

}
