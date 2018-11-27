package com.zm.protocol.response.nodes.widgets.parent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018/11/22.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Content {

    /** 标题 */
    private String title;

    /** 子标题 */
    private String subTitle;

    /** 标签 */
    private String label;

    /** 图片资源地址 */
    private String imageUrl;

    /** 资源地址 */
    private String linkUrl;

    /** 用户自定义参数，透传给用户 */
    private LinkedHashMap<String, String> extra;
}
