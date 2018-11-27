package com.zm.protocol.response.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

/**
 * <p>返回给 CloudAppClient 的 Response 内容</p>
 * <p>包括了 card 和 action 两个部分</p>
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Response<T> {

    /** 对话交互中语音播报的内容 */
    private Speak speak;

    /** 执行本地命令或本地查询, nativecmd没有返回值, nativeapi有返回值(nativeapi保留备用) */
    private Execute execute;

    /**
     * 对话交互中要显示的内容, DUI控件包含: 文本(text)、内容卡片(content)、列表(list)、多媒体(media)、内嵌网页(web)、自定义。
     */
    private T widget;

    /**
     * 对话交互中语音播报的内容
     */
    @Data
    public static class Speak {

        /**
         * 对话输出类型，音频或者语音合成；"text": 纯文本；"audio": 音频资源; "ssml"：合成SSML标记
         */
        private SpeakType type;

        /** 合成文本，speak.type是"text"时必须有 */
        private String text;

        /** 对话语音输出音频，speek.type为“audio”时必须有 */
        private String audioUrl;

        /** 用于合成语音的ssml标记文本 */
        private String ssml;
    }

    /**
     * 执行本地命令或本地查询, 可以有返回值, 也可以没返回值
     */
    @Data
    public static class Execute {

        /**
         *  命令URL, 两种类型nativecmd和nativeapi<p>
         *  nativecmd没有返回值, nativeapi有返回值
         */
        private String url;

        /** 命令参数 */
        private LinkedHashMap<String, String> args;
    }

    /**
     * 对话输出类型枚举
     */
    public enum SpeakType {
        /** 纯文本 */
        text,

        /** 音频资源 */
        audio,

        /** 合成SSML标记 */
        ssml
    }

}
