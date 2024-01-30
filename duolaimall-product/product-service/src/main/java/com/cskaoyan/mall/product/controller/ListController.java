package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.client.SearchApiClient;
import com.cskaoyan.mall.search.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")
public class ListController {
    @Autowired
    SearchApiClient searchApiClient;
    @GetMapping
    Result list(SearchParam searchParam) {
        return Result.ok(searchApiClient.list(searchParam));
    }
}
