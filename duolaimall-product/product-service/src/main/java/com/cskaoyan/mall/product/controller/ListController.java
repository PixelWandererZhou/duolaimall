package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")
public class ListController {
//    http://124.220.39.95/list?thirdLevelCategoryId=61
    public Result list(Integer thirdLevelCategoryId){
        return null;
    }
}
