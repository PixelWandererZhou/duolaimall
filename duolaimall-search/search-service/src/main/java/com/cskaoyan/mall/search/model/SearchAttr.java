package com.cskaoyan.mall.search.model;

import lombok.Data;
import org.dromara.easyes.annotation.IndexName;

@Data
public class SearchAttr {
    // 平台属性Id
    private Long attrId;
    // 平台属性值名称
    private String attrValue;
    // 平台属性名
    private String attrName;
}
