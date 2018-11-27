package com.zm.protocol;

import com.zm.protocol.exception.ProtocolException;
import com.zm.protocol.response.SkillResponse;
import com.zm.protocol.response.nodes.Response;
import com.zm.protocol.response.nodes.Session;
import com.zm.protocol.response.nodes.widgets.CardWidget;
import com.zm.protocol.response.nodes.widgets.ListOrMediaWidget;
import com.zm.protocol.response.nodes.widgets.TextWidget;
import com.zm.protocol.response.nodes.widgets.WebWidget;
import com.zm.protocol.response.nodes.widgets.enumer.WidgetType;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * skill请求响应构建者<p>
 * 包含一些实用方法<p>
 */
@Slf4j
public class ResponseBuilder {

    /*=====================================================================================================*/
    /*=====================================================================================================*/

    public static final SkillResponse error(String tts) {
        return ResponseBuilder.build().speakText(tts).afterFinish().createError();
    }

    private SkillResponse createError() {
        response.getResponse().setSpeak(speak);
        return response;
    }

    public static final SkillResponse playSpeak(String tts) throws ProtocolException {
        try {
            return ResponseBuilder.build().speakText(tts).create();
        } catch (ProtocolException e) {
            log.error("builder response error", e);
            throw e;
        }
    }

    /**
     * 退出应用
     *
     * @return SkillResponse
     */
    public static final SkillResponse exit() throws ProtocolException {
        try {
            return ResponseBuilder.build().afterFinish().create();
        } catch (ProtocolException e) {
            log.error("builder response error", e);
            throw e;
        }
    }

    /**
     * 退出应用
     *
     * @return SkillResponse
     */
    public static final SkillResponse exit(String tts) throws ProtocolException {
        try {
            return ResponseBuilder.build().speakText(tts).afterFinish().create();
        } catch (ProtocolException e) {
            log.error("builder response error", e);
            throw e;
        }
    }

    public static final SkillResponse playSpeakAndMedia(Response.Speak speak, ListOrMediaWidget media) throws ProtocolException {
        try {
            media.setType(WidgetType.media);
            return ResponseBuilder.build().speak(speak).setMediaWidget(media).create();
        } catch (ProtocolException e) {
            log.error("builder response error", e);
            throw e;
        }
    }

    public static final SkillResponse playSpeakAndSendTextWidget(Response.Speak speak, TextWidget textWidget) throws ProtocolException {
        try {
            textWidget.setType(WidgetType.text);
            return ResponseBuilder.build().speak(speak).setTextWidget(textWidget).create();
        } catch (ProtocolException e) {
            log.error("builder response error", e);
            throw e;
        }
    }

    public static final SkillResponse playSpeakAndSendCardWidget(Response.Speak speak, CardWidget card) throws ProtocolException {
        try {
            card.setType(WidgetType.content);
            return ResponseBuilder.build().speak(speak).setCardWidget(card).create();
        } catch (ProtocolException e) {
            log.error("builder response error", e);
            throw e;
        }
    }

    /*=====================================================================================================*/
    /*=====================================================================================================*/

    /**
     * 客户端的返回结果
     */
    private SkillResponse response;
    /**
     * 执行完毕后	对话是否结束
     */
    private boolean shouldEndSession = false;
    /**
     * 是否让出机会给其它skill, 如果是true, Skill Dispatcher将尝试重新派发该请求, 如果派发成功则结束当前skill的session。
     * 如果派发失败则继续按当前skill的响应回复用户。例如 U: 我要打电话 S: 打给谁 U: 导航去北京 S: 为您找到如下几个地点 U: ...
     */
    private boolean yield = false;
    /**
     * 置信度
     */
    private float confidence = 0.9F;

    /** 会话id */
    private String sessionId;

    /**
     * session中的上下文信息
     */
    private LinkedHashMap<String, Object> attributes;
    /**
     * session中下发的下一轮意图
     */
    private List<String> nextIntents;
    /**
     * 语音交互指令
     */
    private Response.Speak speak;
    /**
     * 卡片内容控件，控件之间互斥，即同时只能设置一种控件
     */
    private CardWidget card;
    /**
     * 文本控件，控件之间互斥，即同时只能设置一种控件
     */
    private TextWidget text;
    /**
     * 网页控件，控件之间互斥，即同时只能设置一种控件
     */
    private WebWidget web;
    /**
     * list控件，控件之间互斥，即同时只能设置一种控件
     */
    private ListOrMediaWidget list;
    /**
     * 媒体内容播放控件，控件之间互斥，即同时只能设置一种控件
     */
    private ListOrMediaWidget media;

    /**
     * 需要执行的本地命令
     */
    private Response.Execute execute;


    /**
     * 返回ResponseBuilder构建者对象
     * @return ResponseBuilder
     */
    public static ResponseBuilder build() {
        return new ResponseBuilder();
    }

    public ResponseBuilder() {
        response = buildEmptyWidgetResponse(null,null,null);
    }

    public ResponseBuilder setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    /**
     * 设置 是否让出机会给其它技能
     */
    public ResponseBuilder setYield(boolean yield) {
        this.yield = yield;
        return this;
    }

    /**
     * 设置 置信度
     */
    public ResponseBuilder setConfidence(float confidence) {
        this.confidence = confidence;
        return this;
    }

    /**
     * 设置上下文信息<p>
     * client的下次请求attributes会被原样带回<p>
     * @param attributes attributes
     * @return responseBuilder
     */
    public ResponseBuilder setAttributes(LinkedHashMap<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * 设置下一轮意图
     * @param nextIntents 意图列表
     * @return ResponseBuilder
     */
    public ResponseBuilder setNextIntents(List<String> nextIntents) {
        this.nextIntents = nextIntents;
        return this;
    }

    /**
     * 设置本地执行命令
     */
    public ResponseBuilder setExecute(Response.Execute execute) {
        this.execute = execute;
        return this;
    }

    /**
     * 文本类型语音播报指令
     * @return ResponseBuilder
     */
    public ResponseBuilder speak(Response.Speak speak) {
        this.speak = speak;
        return this;
    }

    /**
     * 文本类型语音播报指令
     * @return ResponseBuilder
     */
    public ResponseBuilder speakText(String text) {
        if (speak == null) {
            speak = new Response.Speak();
        }
        speak.setType(Response.SpeakType.text);
        speak.setText(text);
        return this;
    }
    /**
     * 音频类型语音播报指令
     * @return ResponseBuilder
     */
    public ResponseBuilder speakAudio(String audioUrl) {
        if (speak == null) {
            speak = new Response.Speak();
        }
        speak.setType(Response.SpeakType.audio);
        speak.setAudioUrl(audioUrl);
        return this;
    }
    /**
     * 语音合成类型语音播报指令
     * @return ResponseBuilder
     */
    public ResponseBuilder speakSsml(String ssml) {
        if (speak == null) {
            speak = new Response.Speak();
        }
        speak.setType(Response.SpeakType.ssml);
        speak.setSsml(ssml);
        return this;
    }

    /**
     * 设置文本控件
     *
     * @return ResponseBuilder
     */
    public ResponseBuilder setTextWidget(TextWidget textWidget) {
        this.text = textWidget;
        return this;
    }

    /**
     * 设置卡片控件
     *
     * @return ResponseBuilder
     */
    public ResponseBuilder setCardWidget(CardWidget card) {
        this.card = card;
        return this;
    }


    /**
     * 设置web控件
     *
     * @return ResponseBuilder
     */
    public ResponseBuilder setWebWidget(WebWidget web) {
        this.web = web;
        return this;
    }

    /**
     * 设置list控件
     *
     * @return ResponseBuilder
     */
    public ResponseBuilder setListWidget(ListOrMediaWidget list) {
        this.list = list;
        return this;
    }

    /**
     * 设置媒体控件
     *
     * @return ResponseBuilder
     */
    public ResponseBuilder setMediaWidget(ListOrMediaWidget media) {
        this.media = media;
        return this;
    }

    /**
     * 指令执行完成后退出应用
     *
     * @return ResponseBuilder
     */
    public ResponseBuilder afterFinish() {
        shouldEndSession = true;
        return this;
    }


    /**
     * 构建请求响应
     * @return SkillResponse
     * @throws ProtocolException ProtocolException
     */
    public SkillResponse create() throws ProtocolException {
        // 设置上下文信息到session中
        if (attributes != null) {
            response.getSession().setAttributes(attributes);
        }
        if (nextIntents != null) {
            response.getSession().setNextIntents(nextIntents);
        }
        if (!CheckUtil.checkSpeak(speak)) {
            throw new ProtocolException("check speak error");
        }
        response.getResponse().setSpeak(speak);

        if (execute != null) {
            response.getResponse().setExecute(execute);
        }


        if (card != null) {
            response.getResponse().setWidget(card);
        }
        if (text != null) {
            response.getResponse().setWidget(text);
        }
        if (media != null) {
            response.getResponse().setWidget(media);
        }
        if (web != null) {
            response.getResponse().setWidget(web);
        }
        if (list != null) {
            response.getResponse().setWidget(list);
        }

        response.setShouldEndSession(this.shouldEndSession);
        response.setConfidence(this.confidence);
        response.setYield(this.yield);

        return response;
    }

    /**
     * 构建默认SkillResponse<p>
     * 即没有控件，其它返回值为默认值<p>
     * @param attributes 上下文
     * @param nextIntents 下一轮意图
     * @return skill response
     */
    private SkillResponse buildEmptyWidgetResponse(LinkedHashMap<String, Object> attributes,
                                                   List<String> nextIntents,
                                                   Response.Speak speak) {

        SkillResponse skillResponse = new SkillResponse();
        Session session = new Session();

        // 下一轮意图
        if (nextIntents != null) {
            session.setNextIntents(nextIntents);
        }
        // 上下文信息
        if (attributes != null && !attributes.isEmpty()) {
            session.setAttributes(attributes);
        }
        skillResponse.setSession(session);

        // 空的response
        Response response = new Response();
        if (speak != null) {
            response.setSpeak(speak);
        }
        skillResponse.setResponse(response);

        /*skillResponse.setShouldEndSession(this.shouldEndSession);
        skillResponse.setConfidence(this.confidence);
        skillResponse.setYield(this.yield);*/

        return skillResponse;
    }

    public static class CheckUtil {

        /**
         * 语音校验
         */
        public static boolean checkSpeak (Response.Speak speak) throws ProtocolException {
            if (speak == null) {
                throw new ProtocolException("speak Can not be null.");
            }
            if (speak.getType() == null) {
                throw new ProtocolException("speak type error.");
            }
            return true;
        }
    }
}
