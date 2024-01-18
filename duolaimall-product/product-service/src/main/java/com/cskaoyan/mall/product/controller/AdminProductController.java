package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.FileService;
import com.cskaoyan.mall.product.service.PlatformAttributeService;
import io.minio.errors.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PlatformAttributeService platformAttributeService;
    @Autowired
    FileService fileService;
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

    @PostMapping("fileUpload")
    public  Result fileUpload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return Result.ok(fileService.fileUpload(file));
    }
}
