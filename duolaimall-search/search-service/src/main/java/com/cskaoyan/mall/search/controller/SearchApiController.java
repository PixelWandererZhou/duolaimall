package com.cskaoyan.mall.search.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.search.dto.SearchResponseDTO;
import com.cskaoyan.mall.search.param.SearchParam;
import com.cskaoyan.mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/list")
public class SearchApiController {

    @Autowired
    private SearchService searchService;
    @GetMapping
    public Result list(SearchParam searchParam) throws IOException {
        SearchResponseDTO searchResponseDTO = searchService.search(searchParam);
        return Result.ok(searchResponseDTO);
    }


    /**
     * 上架商品
     * @param skuId
     * @return
     */
    @GetMapping("/inner/upperGoods/{skuId}")
    public Result upperGoods(@PathVariable("skuId") Long skuId) {
        searchService.upperGoods(skuId);
        return Result.ok();
    }

    /**
     * 下架商品
     * @param skuId
     * @return
     */
    @GetMapping("/inner/lowerGoods/{skuId}")
    public Result lowerGoods(@PathVariable("skuId") Long skuId) {
        searchService.lowerGoods(skuId);
        return Result.ok();
    }
    @GetMapping("inner/incrHotScore/{skuId}")
    public Result incrHotScore(@PathVariable("skuId") Long skuId) {
        searchService.incrHotScore(skuId);
        return Result.ok();
    }
}