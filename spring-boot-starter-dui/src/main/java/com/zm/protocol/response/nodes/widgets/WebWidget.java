package com.zm.protocol.response.nodes.widgets;

import com.zm.protocol.response.nodes.widgets.enumer.WidgetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018/11/22.
 * 内嵌网页控件
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WebWidget {

    /** 控件类型 */
    private WidgetType type = WidgetType.web;

    /** 控件名称 */
    private String name;

    /** 内嵌网页url资源地址 */
    private String url;

    /** 用户自定义参数，透传给用户。开发者也可以在定制平台引用：$exra.key1$ */
    private LinkedHashMap<String, String> extra;
}
