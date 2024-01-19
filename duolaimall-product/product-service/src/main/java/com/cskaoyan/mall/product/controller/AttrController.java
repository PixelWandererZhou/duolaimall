package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.query.PlatformAttributeParam;
import com.cskaoyan.mall.product.query.PlatformAttributeValueParam;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.PlatformAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product")
public class AttrController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PlatformAttributeService platformAttributeService;
    @RequestMapping("getCategory1")
    public Result<List<FirstLevelCategoryDTO>> getCategory1(){
        return Result.ok(categoryService.getFirstLevelCategory());
    }
    @GetMapping("getCategory2/{firstLevelCategoryId}")
    public Result<List<SecondLevelCategoryDTO>> getCategory2(@PathVariable Long firstLevelCategoryId){
        return Result.ok(categoryService.getSecondLevelCategory(firstLevelCategoryId));
    }
    @GetMapping("getCategory3/{secondLevelCategoryId}")
    public Result<List<ThirdLevelCategoryDTO>> getCategory3(@PathVariable Long secondLevelCategoryId){
        return Result.ok(categoryService.getThirdLevelCategory(secondLevelCategoryId));
    }
    @GetMapping("attrInfoList/{firstLevelCategoryId}/{secondLevelCategoryId}/{thirdLevelCategoryId}")
    public Result<List<PlatformAttributeInfoDTO>> attrInfoList(@PathVariable Long firstLevelCategoryId,@PathVariable Long secondLevelCategoryId,@PathVariable Long thirdLevelCategoryId){
        return Result.ok(platformAttributeService.getPlatformAttrInfoList(firstLevelCategoryId,secondLevelCategoryId,thirdLevelCategoryId));
    }

    @GetMapping("getAttrValueList/{attrId}")
    public Result<List<PlatformAttributeValueDTO>> getAttrValueList(@PathVariable Long attrId){
        return Result.ok(platformAttributeService.getPlatformAttrInfo(attrId));
    }

    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody Map map) {
        List attrValueList = (List) map.get("attrValueList");
        if(attrValueList.isEmpty()){
            PlatformAttributeParam platformAttributeParam = new PlatformAttributeParam();
            Long id = Long.valueOf((Integer) map.get("id"));
            platformAttributeParam.setId(id);
            platformAttributeParam.setAttrName((String) map.get("attrName"));
            platformAttributeService.savePlatformAttrInfo(platformAttributeParam);
        }
        else {
            List<PlatformAttributeValueParam> platformAttributeValueParamList = new ArrayList<>();
            for (int i = 0; i < attrValueList.size(); i++) {
                Map o = (Map) attrValueList.get(i);
                if (!o.containsKey("id")) {
                    //{valueName=123124, edit=false} -> {id=null, valueName=123124, attrId=106}
                    Long attrId = Long.valueOf((Integer) map.get("id"));
                    platformAttributeValueParamList.add(new PlatformAttributeValueParam(null, (String) o.get("valueName"),attrId ));
                }
                else {
                    Long id = Long.valueOf((Integer) o.get("id"));
                    Long attrId = Long.valueOf((Integer) o.get("attrId"));
                    PlatformAttributeValueParam platformAttributeValueParam = new PlatformAttributeValueParam(id, (String) o.get("valueName"), attrId);
                    platformAttributeValueParamList.add(platformAttributeValueParam);
                }
            }
            PlatformAttributeParam platformAttributeParam = new PlatformAttributeParam();
            Long id = Long.valueOf((Integer) map.get("id"));
            platformAttributeParam.setId(id);
            platformAttributeParam.setAttrName((String) map.get("attrName"));
            platformAttributeParam.setAttrValueList(platformAttributeValueParamList);
            platformAttributeService.savePlatformAttrInfo(platformAttributeParam);

        }
        return Result.ok();
    }


}
