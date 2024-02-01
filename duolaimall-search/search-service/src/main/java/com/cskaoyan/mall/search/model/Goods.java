package com.cskaoyan.mall.search.model;

import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

import java.util.List;

// Index = goods , Type = info  es 7.8.0 逐渐淡化type！  修改！
@Data
@IndexName(value = "goods",childClass = SearchAttr.class)
public class Goods {
    // 商品Id skuId
    @IndexId(type = IdType.CUSTOMIZE)
    private Long id;

    private String defaultImg;

    //  es 中能分词的字段，这个字段数据类型必须是 text！keyword 不分词！
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String title;

    private Double price;

    private Long tmId;

    private String tmName;

    private String tmLogoUrl;

    private Long firstLevelCategoryId;

    private String firstLevelCategoryName;

    private Long secondLevelCategoryId;

    private String secondLevelCategoryName;

    private Long thirdLevelCategoryId;

    private String thirdLevelCategoryName;

    //  商品的热度！ 我们将商品被用户点查看的次数越多，则说明热度就越高！
    private Long hotScore = 0L;

    // 平台属性集合对象
    // Nested 支持嵌套查询
    private List<SearchAttr> attrs;

}
