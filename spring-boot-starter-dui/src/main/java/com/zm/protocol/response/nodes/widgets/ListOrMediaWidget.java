package com.zm.protocol.response.nodes.widgets;

import com.zm.protocol.response.nodes.widgets.enumer.WidgetType;
import com.zm.protocol.response.nodes.widgets.parent.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 * 列表或媒体类型卡片
 *
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ListOrMediaWidget {
    /** 控件类型 */
    private WidgetType type;

    /** 控件名称 */
    private String name;

    /** data字段内数据列表个数 */
    private Integer count;

    /** 查询返回的数据列表，每个子项是个内容卡片 */
    private List<Content> content;
}
