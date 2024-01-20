package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.FirstLevelCategoryNodeDTO;
import com.cskaoyan.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class IndexController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("index")
    public Result<List<FirstLevelCategoryNodeDTO>> index(){
        return Result.ok(categoryService.getCategoryTreeList());
    }

}
