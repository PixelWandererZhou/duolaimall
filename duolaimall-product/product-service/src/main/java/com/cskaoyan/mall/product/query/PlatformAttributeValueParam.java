package com.cskaoyan.mall.product.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformAttributeValueParam {

    //"平台属性值id"
    private Long id;

    //"属性值名称"
    private String valueName;

    //"属性id"
    private Long attrId;
}
