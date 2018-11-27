package com.zm.skill.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zm.common.openapi.BaiduMusicApi;
import com.zm.protocol.ResponseBuilder;
import com.zm.protocol.exception.ProtocolException;
import com.zm.protocol.response.SkillResponse;
import com.zm.protocol.response.nodes.Response;
import com.zm.protocol.response.nodes.widgets.ListOrMediaWidget;
import com.zm.protocol.response.nodes.widgets.enumer.WidgetType;
import com.zm.protocol.response.nodes.widgets.parent.Content;
import com.zm.skill.dto.Song;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/11/26.
 */
@Service
@Slf4j
public class DemoSkillService {

    public Song play(String session, String keyword) throws IOException {

        JSONObject object = BaiduMusicApi.search(keyword);

        if (object.getJSONArray("song") == null) {
            return null;
        }

        JSONArray array = object.getJSONArray("song");
        if(!array.isEmpty()) {
            Random r = new Random();
            int index = r.nextInt(array.size()) - 1;
            if (array.size() == 1) {
                index = 0;
            }

            DataCache.cached.put(session + DataCache.INDEX_KEY, index);
            DataCache.cached.put(session + DataCache.LIST_KEY, array);

            JSONObject json = (JSONObject) array.get(index);
            String songId = json.getString("songid");

            String name = json.getString("songname");
            String artistname = json.getString("artistname");
            String url = BaiduMusicApi.getPlayUrl(songId);
            return new Song(name, url, artistname);
        }
        return null;
    }

    public Song next(String session) throws IOException {

        int index = (int) DataCache.cached.getIfPresent(session + DataCache.INDEX_KEY);
        JSONArray array = (JSONArray) DataCache.cached.getIfPresent(session + DataCache.LIST_KEY);
        if (array != null && !array.isEmpty()) {
            if (index == (array.size() - 1)) {
                index = 0;
            } else {
                index = index + 1;
            }

            DataCache.cached.put(session + DataCache.INDEX_KEY, index);
            DataCache.cached.put(session + DataCache.LIST_KEY, array);

            JSONObject json = (JSONObject) array.get(index);
            String songId = json.getString("songid");


            String name = json.getString("songname");
            String artistname = json.getString("artistname");
            String url = BaiduMusicApi.getPlayUrl(songId);
            return new Song(name, url, artistname);
        }
        return null;
    }

    public Song previous(String session) throws IOException {

        int index = (int) DataCache.cached.getIfPresent(session + DataCache.INDEX_KEY);

        JSONArray array = (JSONArray) DataCache.cached.getIfPresent(session + DataCache.LIST_KEY);

        if (array != null && !array.isEmpty()) {
            if (index == 0) {
                index = array.size() - 1;
            } else {
                index = index - 1;
            }

            DataCache.cached.put(session + DataCache.INDEX_KEY, index);
            DataCache.cached.put(session + DataCache.LIST_KEY, array);

            JSONObject json = (JSONObject) array.get(index);
            String songId = json.getString("songid");

            String name = json.getString("songname");
            String artistname = json.getString("artistname");
            String url = BaiduMusicApi.getPlayUrl(songId);
            return new Song(name, url, artistname);
        }
        return null;
    }

    public Song recommend() {
        try {
            JSONObject object = BaiduMusicApi.getRecommendList();

            if (object.getJSONArray("song_list") == null) {
                return null;
            }

            JSONArray array = object.getJSONArray("song_list");
            if(!array.isEmpty()) {
                Random r = new Random();
                int index = r.nextInt(array.size()) - 1;
                if (array.size() == 1) {
                    index = 0;
                }
                JSONObject json = (JSONObject) array.get(index);
                String songId = json.getString("song_id");

                String name = json.getString("title");
                String artistname = json.getString("artist_name");
                String url = BaiduMusicApi.getPlayUrl(songId);
                return new Song(name, url, artistname);
            }
        } catch (IOException e) {
            log.error("get recommend list error: ", e);
        }
        return null;
    }

    public SkillResponse getSkillResponse(Song song, String tts) throws ProtocolException {
        Response.Speak speak = new Response.Speak();
        speak.setType(Response.SpeakType.text);

        if (StringUtils.isNotBlank(tts)) {
            speak.setText(tts);
        } else {
            speak.setText(String.format("即将播放歌曲，%s", song.getName()));
        }


        ListOrMediaWidget widget = new ListOrMediaWidget();
        widget.setType(WidgetType.media);
        widget.setCount(1);
        widget.setName("音乐播放");

        Content content = new Content();
        content.setLinkUrl(song.getUrl());
        content.setLabel(song.getName());
        content.setTitle(song.getArtistname() + "-" + song.getName());
        content.setSubTitle(song.getName());

        List<Content> list = new ArrayList<>();
        list.add(content);
        widget.setContent(list);

        return ResponseBuilder.playSpeakAndMedia(speak, widget);
    }

}
