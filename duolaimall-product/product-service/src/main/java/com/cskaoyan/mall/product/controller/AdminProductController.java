package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.FirstLevelCategoryDTO;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.FileService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    FileService fileService;
    @RequestMapping("getCategory1")
    public Result<List<FirstLevelCategoryDTO>> getCategory1(){
        return Result.ok(categoryService.getFirstLevelCategory());
    }
    @PostMapping("fileUpload")
    public  Result fileUpload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return Result.ok(fileService.fileUpload(file));
    }
}
