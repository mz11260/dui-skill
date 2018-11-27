package com.zm.protocol.response.nodes.widgets;

import com.zm.protocol.response.nodes.widgets.enumer.WidgetType;
import com.zm.protocol.response.nodes.widgets.parent.Content;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Administrator on 2018/11/22.
 * 卡片类型控件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CardWidget extends Content {

    /** 控件类型 */
    private WidgetType type = WidgetType.content;

    /** 控件名称 */
    private String name;

}
