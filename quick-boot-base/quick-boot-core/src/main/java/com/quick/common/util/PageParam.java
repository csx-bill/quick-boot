package com.quick.common.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础分页
 */
@Getter
@Setter
@SuppressWarnings("ALL")
@Accessors(chain = true)
public class PageParam<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "查询参数")
    private T model;

    @Schema(description = "当前页码")
    private long page;

    @Schema(description = "每页显示数量")
    private long count;

    @Schema(description = "排序的字段")
    private String orderBy;

    @Schema(description = "asc/desc")
    private String orderDir;


    @Schema(description = "排序的字段")
    private List<OrderItem> orders;

    public Page<T> buildPage(){
        Page<T> page = new Page<>();
        page.setCurrent(this.getPage()==0?1:this.getPage());
        page.setSize(this.getCount()==0?10:this.getCount());
        List<OrderItem> orders = new ArrayList<>();
        if(!StringUtils.isEmpty(this.orderBy)){
            OrderItem orderItem = new OrderItem();
            // 驼峰转下划线
            orderItem.setColumn(StrUtil.toUnderlineCase(this.orderBy));
            orderItem.setAsc(this.orderDir.equals("desc")?false:true);
            orders.add(orderItem);
        }
        page.setOrders(orders);
        return page;
    }
}
