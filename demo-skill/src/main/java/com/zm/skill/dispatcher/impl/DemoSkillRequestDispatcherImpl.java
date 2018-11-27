package com.zm.skill.dispatcher.impl;

import com.zm.kit.dispatcher.interfaces.SkillRequestDispatcher;
import com.zm.protocol.ResponseBuilder;
import com.zm.protocol.exception.ProtocolException;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.response.SkillResponse;
import com.zm.skill.dto.Song;
import com.zm.skill.service.DataCache;
import com.zm.skill.service.DemoSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 示例技能请求分发<p>
 * 可以通过该接口实现一些逻辑，例如打开技能的欢迎语，关闭技能时清理缓存数据等等<p>
 * Created by Administrator on 2018/11/26.
 */
@Component("demoSkillRequestDispatcher")
public class DemoSkillRequestDispatcherImpl implements SkillRequestDispatcher {

    private DemoSkillService demoSkillService;

    @Autowired
    public void setDemoSkillService(DemoSkillService demoSkillService) {
        this.demoSkillService = demoSkillService;
    }

    @Override
    public SkillResponse startRequest(SkillRequest request) throws ProtocolException {

        Song song = demoSkillService.recommend();

        if (song == null) {
            return ResponseBuilder.playSpeak("欢迎使用本技能，试试和我对话吧");
        }

        return demoSkillService.getSkillResponse(song, String.format("欢迎回来，为您推荐热门歌曲，%s的%s", song.getArtistname(), song.getName()));


    }

    @Override
    public SkillResponse endRequest(SkillRequest request) throws ProtocolException {

        DataCache.cached.invalidate(request.getSession().getSessionId() + DataCache.INDEX_KEY);
        DataCache.cached.invalidate(request.getSession().getSessionId() + DataCache.LIST_KEY);

        return ResponseBuilder.exit("再见");
    }

    @Override
    public SkillResponse unknownRequest(SkillRequest request) throws ProtocolException {
        return ResponseBuilder.playSpeak("我好像不明白你在说什么");
    }
}
