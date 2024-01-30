package com.cskaoyan.mall.search.service.impl;

import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.search.client.ProductApiClient;
import com.cskaoyan.mall.search.dto.SearchResponseDTO;
import com.cskaoyan.mall.search.dto.SearchResponseTmDTO;
import com.cskaoyan.mall.search.model.Goods;
import com.cskaoyan.mall.search.model.SearchAttr;
import com.cskaoyan.mall.search.param.SearchParam;
import com.cskaoyan.mall.search.service.SearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ProductApiClient productFeignClient;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void upperGoods(Long skuId) {
        //调用product服务，查询skuId对应的商品信息
        SkuInfoDTO skuInfo = productFeignClient.getSkuInfo(skuId);
        //将商品信息保存到es中
        Goods goods = new Goods();
        goods.setId(skuInfo.getId());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setTmId(skuInfo.getTmId());
        //品牌
        TrademarkDTO trademark = productFeignClient.getTrademark(skuInfo.getTmId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());
        //三级分类
        CategoryHierarchyDTO categoryView = productFeignClient.getCategoryView(skuInfo.getThirdLevelCategoryId());
        goods.setFirstLevelCategoryId(categoryView.getFirstLevelCategoryId());
        goods.setFirstLevelCategoryName(categoryView.getFirstLevelCategoryName());
        goods.setSecondLevelCategoryId(categoryView.getSecondLevelCategoryId());
        goods.setSecondLevelCategoryName(categoryView.getSecondLevelCategoryName());
        goods.setThirdLevelCategoryId(categoryView.getThirdLevelCategoryId());
        goods.setThirdLevelCategoryName(categoryView.getThirdLevelCategoryName());
        //平台属性
        List<PlatformAttributeInfoDTO> platformAttrInfoList = productFeignClient.getAttrList(skuId);
        if (null != platformAttrInfoList) {
            List<SearchAttr> searchAttrList = platformAttrInfoList.stream().map(baseAttrInfo -> {
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(baseAttrInfo.getId());
                searchAttr.setAttrName(baseAttrInfo.getAttrName());
                //一个sku的一个平台属性，只对应一个属性值
                List<PlatformAttributeValueDTO> attrValueList = baseAttrInfo.getAttrValueList();
                searchAttr.setAttrValue(attrValueList.get(0).getValueName());
                return searchAttr;
            }).collect(Collectors.toList());
            goods.setAttrs(searchAttrList);
        }
        //保存到es中
        elasticsearchRestTemplate.save(goods);
    }

    @Override
    public void lowerGoods(Long skuId) {
        elasticsearchRestTemplate.delete(skuId.toString(), Goods.class);
    }

    @Override
    public void incrHotScore(Long skuId) {

    }

    @Override
    public SearchResponseDTO search(SearchParam searchParam) throws IOException {
        return null;
    }
}
