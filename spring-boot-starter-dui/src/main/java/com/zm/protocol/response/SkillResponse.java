package com.zm.protocol.response;

import com.zm.protocol.response.nodes.Response;
import com.zm.protocol.response.nodes.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>Response 是 CloudApp 向客户端的返回结果</p>
 * <p>完整协议示例：</p>
 * <p>
 * {
 *     "version": "1.0",
 *     "session": {
 *         "nextIntents": ["下一轮意图1","下一轮意图2"],
 *         "attributes": {...}    // 保留备用
 *     },
 *     "response": {
 *         "speak": {
 *             "type": "text",
 *             "text": "北京晴, 26到32度",
 *             "ssml": "SSML markup text string to speak"
 *         },
 *         "widget": {
 *             "type": "content",
 *             "name": "the widget name",
 *             "title": "the title",
 *
 *             "subTitle": "sub-title",
 *             "label": "label",
 *             "imageUrl":"URL of the image to be shown",
 *             "linkUrl":"URL of the attribute to be associated with the card"，
 *             "extra": {
 *                 "key1": "val1",
 *                 "key2": "val2"
 *             },
 *             "recommendations": ["推荐说法1", "推荐说法2"]
 *         },
 *         "execute": {                                // 执行本地命令或本地查询, 可以有返回值, 也可以没返回值
 *             "url": "nativecmd://settings/openwifi", // 命令URL, nativecmd没有返回值, nativeapi有返回值
 *             "args": {                               // 参数
 *                 "arg1": "val1",
 *                 "arg2": "val2"
 *             }
 *         }
 *     },
 *     "yield": false,
 *     "shouldEndSession": false,
 *     "confidence": 0.9
 *   }
 * }
 * </p>
 */
@Data
@EqualsAndHashCode
public class SkillResponse {

    /** Response协议的版本，必须由 CloudApp 填充。当前协议版本是 1.0.0 */
    private String version = "1.0.0";

    /** 当前技能的session */
    private Session session;

    /** 给 CloudAppClient 的Response内容 */
    private Response response;

    /**
     * 是否让出机会给其它skill, 如果是true, Skill Dispatcher将尝试重新派发该请求, 如果派发成功则结束当前skill的session。
     * 如果派发失败则继续按当前skill的响应回复用户。例如 U: 我要打电话 S: 打给谁 U: 导航去北京 S: 为您找到如下几个地点 U: ...
     */
    private boolean yield;

    /** 对话是否结束，false：继续对话，true：结束对话 */
    private boolean shouldEndSession;

    /** 置信度 */
    private float confidence;
}
