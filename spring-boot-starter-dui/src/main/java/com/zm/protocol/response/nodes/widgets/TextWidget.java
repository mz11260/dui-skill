package com.zm.protocol.response.nodes.widgets;

import com.zm.protocol.response.nodes.widgets.enumer.WidgetType;
import com.zm.protocol.response.nodes.widgets.parent.Content;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 * 文本类型控件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TextWidget extends Content {

    /** 控件类型 */
    private WidgetType type = WidgetType.text;

    /** 控件名称 */
    private String name;

    /** 推荐说法列表 */
    private List<String> recommendations;
}
