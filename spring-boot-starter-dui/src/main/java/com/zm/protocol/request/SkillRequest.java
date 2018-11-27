package com.zm.protocol.request;

import com.zm.protocol.request.nodes.Context;
import com.zm.protocol.request.nodes.Request;
import com.zm.protocol.request.nodes.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CloudApp协议，Request对象<p>
 * 请求完整信息<p>
 * <p>{
 *     "version": "2.0.0",
 *     "session": {
 *         "sessionId": "D75D1C9BECE045E9AC4A87DA86303DD6",
 *         "newSession": true,
 *         "attributes": {
 *             "key1": {"type":"","value":""}
 *         }
 *     },
 *     "context": {
 *         "application": {
 *             "skillId": "skill 本身的 Id",
 *             "media": {
 *                 "state": "PLAYING/PAUSED/IDLE",
 *                 "itemId": "Skill 响应的 MediaId",
 *                 "token": "Skill 响应的 MediaToken",
 *                 "progress": "当前的播放进度单位毫秒",
 *                 "duration": "当前 Media 的总长度单位毫秒"
 *             },
 *             "voice": {
 *                 "state": "PLAYING/PAUSED/IDLE",
 *                 "itemId": "Skill 响应的 VoiceId"
 *             }
 *         },
 *         "device": {
 *             "basic": {
 *                 "vendor": "注册生产商 ID",
 *                 "deviceType": "该生产商设定的设备型号",
 *                 "deviceId": "该型号下的设备 ID",
 *                 "masterId": "设备主人 ID",
 *                 "voicetrigger": "设备当前的激活词",
 *                 "locale": "zh-cn",
 *                 "timestamp": 1478009510909
 *             },
 *             "screen": {
 *                 "x": "640",
 *                 "y": "480"
 *             },
 *             "media": {
 *                 "state": "PLAYING / PAUSED / IDLE"
 *             },
 *             "voice": {
 *                 "state": "PLAYING / PAUSED / IDLE"
 *             },
 *             "location": {
 *                 "latitude": "30.213322455923485",
 *                 "longitude": "120.01190010997654",
 *                 "country": "国家",
 *                 "state": "州/省份",
 *                 "city": "城市",
 *                 "area": "区县",
 *                 "district": "地区，行政",
 *                 "street": "街道",
 *                 "timeZone": "时区"
 *             }
 *         },
 *         "user": {
 *             "userId": "当前用户的ID"
 *         }
 *     },
 *     "request": {
 *         "reqType": "INTENT / EVENT",
 *         "reqId": "当前请求的 Id",
 *         "content": {
 *             "intent": "play_random",
 *             "sentence": "用户语句",
 *             "slots": {
 *                 "key1":{"type":"","value":""},
 *                 "key2":{"type":"","value":""}
 *             }
 *         }
 *     }
 * }
 * </p>
 */
@Data
@EqualsAndHashCode
public class SkillRequest<T> {

    /**
     * Response协议的版本，必须由 CloudApp 填充<p>
     * 当前协议版本是 1.0.0<p>
     */
    private String version = "1.0.0";

    /**
     * 会话的信息<p>
     * @see Session
     */
    private Session session;

    /**
     * 当前的设备信息，用户信息和应用状态<p>
     * 用以帮助CloudApp更好的去管理逻辑，状态以及对应的返回结果<p>
     * @see Context
     */
    private Context context;

    /**
     * 当前请求的真正内容<p>
     * @see Request
     */
    private Request request;

}
