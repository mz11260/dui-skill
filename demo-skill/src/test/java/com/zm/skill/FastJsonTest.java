package com.zm.skill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zm.protocol.ResponseBuilder;
import com.zm.protocol.response.SkillResponse;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FastJsonTest {

    public static void main(String[] args) throws Exception {
        SkillResponse res = ResponseBuilder.playSpeak("测试");

        System.out.println(JSON.toJSONString(res));


        System.out.println(JSON.toJSONString(res, 1041));

        int features = 0;
        features |= SerializerFeature.QuoteFieldNames.getMask();
        features |= SerializerFeature.SkipTransientField.getMask();
        features |= SerializerFeature.WriteEnumUsingName.getMask();
        System.out.println(features);
        features |= SerializerFeature.SortField.getMask();

        {
            String featuresProperty = IOUtils.getStringProperty("fastjson.serializerFeatures.MapSortField");
            int mask = SerializerFeature.MapSortField.getMask();
            if ("true".equals(featuresProperty)) {
                features |= mask;
            } else if ("false".equals(featuresProperty)) {
                features &= ~mask;
            }
        }
        System.out.println(features);
    }

    @Test
    public void test2() {
        Map<Integer, List<String>> map = Maps.newLinkedHashMap();
        List<String> l = Lists.newLinkedList();
        l.add("rwere");
        l.add("zzzfa");
        l.add("aaa");
        l.add("aab");
        map.put(5454, l);
        l = Lists.newLinkedList();
        l.add("zrwereTTT");
        l.add("bzzzfaFFF");
        l.add("aaaaF");
        l.add("baabGH");
        map.put(13, l);
        l = Lists.newLinkedList();
        l.add("ffrwereTTT");
        l.add("zzzzfaFFF");
        l.add("haaaF");
        l.add("aaaabGH");
        map.put(423, l);
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void test3() {
        String s = "{5454:[\"rwere\",\"zzzfa\",\"aaa\",\"aab\"],13:[\"zrwereTTT\",\"bzzzfaFFF\",\"aaaaF\",\"baabGH\"],423:[\"ffrwereTTT\",\"zzzzfaFFF\",\"haaaF\",\"aaaabGH\"]}";
        HashMap map = JSON.parseObject(s, new TypeReference<HashMap>(){});
        System.out.println(map.toString());
    }
    @Test
    public void test4() {
        Date now = new Date();
        String[] arr = "00:00".split(":");
        int hours = Integer.parseInt(arr[0]);
        int minutes = Integer.parseInt(arr[1]);
        Date start = DateUtils.setMinutes(DateUtils.setHours(now, hours), minutes);
        Date end = DateUtils.setMinutes(DateUtils.setHours(now, hours), minutes + 1);
        System.out.println(DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
    }

}
