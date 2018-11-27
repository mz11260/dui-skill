package com.zm.skill.dispatcher;

import com.zm.kit.annotation.SkillSubscribe;
import com.zm.protocol.ResponseBuilder;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.request.nodes.Request;
import com.zm.protocol.response.SkillResponse;
import com.zm.skill.dto.Song;
import com.zm.skill.service.DataCache;
import com.zm.skill.service.DemoSkillService;
import com.zm.starter.SkillRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2018/11/26.
 */
@SkillRequestHandler(name = "demo")
public class DemoSkillIntentDispatcher {

    private DemoSkillService demoSkillService;

    @Autowired
    public void setDemoSkillService(DemoSkillService demoSkillService) {
        this.demoSkillService = demoSkillService;
    }

    /**
     * 播放意图
     * @param request skill request
     * @return skill response
     */
    @SkillSubscribe(task = "play", name = "playSong")
    public SkillResponse playSong(SkillRequest request) throws Exception {

        Song song = demoSkillService.play(request.getSession().getSessionId(), getKeyword(request));

        if (song == null) {
            return ResponseBuilder.playSpeak("没有找到歌曲" + getKeyword(request) + "，换一首试试吧");
        }

        return demoSkillService.getSkillResponse(song, null);
    }


    /**
     * 下一首意图
     * @param request skill request
     * @return skill response
     */
    @SkillSubscribe(task = "play", name = "next")
    public SkillResponse next(SkillRequest request) throws Exception {

        Song song = demoSkillService.next(request.getSession().getSessionId());

        if (song == null) {
            return ResponseBuilder.playSpeak("没有播放列表，你可以对我说出你想听的歌曲。");
        }

        return demoSkillService.getSkillResponse(song, null);
    }

    /**
     * 上一首意图
     * @param request skill request
     * @return skill response
     */
    @SkillSubscribe(task = "play", name = "previous")
    public SkillResponse previous(SkillRequest request) throws Exception {

        Song song = demoSkillService.previous(request.getSession().getSessionId());

        if (song == null) {
            return ResponseBuilder.playSpeak("没有播放列表，你可以对我说出你想听的歌曲。");
        }

        return demoSkillService.getSkillResponse(song, null);
    }

    /**
     * 退出技能意图
     * @param request skill request
     * @return skill response
     */
    @SkillSubscribe(task = "play", name = "exit")
    public SkillResponse exit(SkillRequest request) throws Exception {

        DataCache.cached.invalidate(request.getSession().getSessionId() + DataCache.INDEX_KEY);
        DataCache.cached.invalidate(request.getSession().getSessionId() + DataCache.LIST_KEY);

        return ResponseBuilder.exit("谢谢使用，再见！");
    }

    private String getKeyword(SkillRequest request) {
        String key = "";
        List<Request.Slot> slots = request.getRequest().getInputs().get(0).getSlots();
        for (Request.Slot slot : slots) {
            if (slot.getName().equals("NAME")) {
                key = slot.getValue();
                break;
            }
        }
        return key;
    }
}
