package com.zm.common.openapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zm.skill.dto.Song;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Random;

/**
 *
 * Created by Administrator on 2018/3/28.
 */
public class BaiduMusicApi {

    /**百度音乐搜索接口*/
    public final static String SEARCH = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.search.catalogSug%s";
    /**百度音乐播放接口*/
    public final static String PLAY = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.song.play&songid=%s";

    /** 获取推荐列表 type = 1-新歌榜,2-热歌榜,11-摇滚榜,12-爵士,16-流行,21-欧美金曲榜,22-经典老歌榜,23-情歌对唱榜,24-影视金曲榜,25-网络歌曲榜 */
    public final static String LIST = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.billboard.billList&type=%s&size=5&offset=0";

    private final static String[] RECOMMEND_TYPES = new String[]{"1", "2", "11", "12", "16", "21", "22", "23", "24", "25"};

    /**
     * 歌曲检索
     * @param keyword 关键词
     * @return jsonObject
     * @throws IOException IOException
     */
    public static JSONObject search(String keyword) throws IOException {
        String json = JsoupUtils.getResponseJson(String.format(SEARCH, StringUtils.isNotEmpty(keyword)? ("&query=" + keyword) : ""));
        return JSON.parseObject(json);
    }

    /**
     * 获取百度音乐推荐列表
     * @return 推荐列表
     * @throws IOException IOException
     */
    public static JSONObject getRecommendList() throws IOException {
        String json = JsoupUtils.getResponseJson(String.format(LIST, RECOMMEND_TYPES[new Random().nextInt(RECOMMEND_TYPES.length) - 1]));
        return JSON.parseObject(json);
    }

    /**
     * 获取歌曲播放链接
     * @param songId 歌曲id
     * @return String
     */
    public static String getPlayUrl(String songId) throws IOException {
        JSONObject jsonObject = JSON.parseObject(JsoupUtils.getResponseJson(String.format(PLAY, songId)));
        JSONObject bitrate = jsonObject.getJSONObject("bitrate");
        return bitrate.getString("file_link");
    }

    public static void main(String[] args) throws IOException {
        /*JSONObject object = BaiduMusicApi.search("八里香");
        if (object.get("error_code") != null) {
            System.out.println("没找到歌曲");
        }
        JSONArray array = object.getJSONArray("song");
        System.out.println(array.toString());
        if(!array.isEmpty()) {
            Random r = new Random();
            JSONObject song = (JSONObject) array.get(r.nextInt(array.size()));

            System.out.println("ARRAY =>> " +  song.toJSONString());
            String songId = song.getString("songid");
            String songUrl = BaiduMusicApi.getPlayUrl(songId);
            System.out.println(songId + "======" + songUrl);
        }*/

        JSONObject object = BaiduMusicApi.getRecommendList();

        if (object.getJSONArray("song_list") == null) {
            System.out.println("没找到歌曲");
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
            System.out.println(new Song(name, artistname, url).toString());
        }

    }


}
