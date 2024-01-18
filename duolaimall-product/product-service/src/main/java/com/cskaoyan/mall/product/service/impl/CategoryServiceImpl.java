package com.cskaoyan.mall.product.service.impl;

import com.cskaoyan.mall.product.converter.dto.CategoryConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.FirstLevelCategoryMapper;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import com.cskaoyan.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    FirstLevelCategoryMapper firstLevelCategoryMapper;

    @Autowired
    CategoryConverter categoryConverter;
    @Override
    public List<FirstLevelCategoryDTO> getFirstLevelCategory() {
        return categoryConverter.firstLevelCategoryPOs2DTOs(firstLevelCategoryMapper.selectList(null));
    }

    @Override
    public List<SecondLevelCategoryDTO> getSecondLevelCategory(Long firstLevelCategoryId) {
        return null;
    }

    @Override
    public List<ThirdLevelCategoryDTO> getThirdLevelCategory(Long secondLevelCategoryId) {
        return null;
    }

    @Override
    public List<TrademarkDTO> findTrademarkList(Long category3Id) {
        return null;
    }

    @Override
    public void save(CategoryTrademarkParam categoryTrademarkParam) {

    }

    @Override
    public List<TrademarkDTO> findUnLinkedTrademarkList(Long thirdLevelCategoryId) {
        return null;
    }

    @Override
    public void remove(Long thirdLevelCategoryId, Long trademarkId) {

    }

    @Override
    public CategoryHierarchyDTO getCategoryViewByCategoryId(Long thirdLevelCategoryId) {
        return null;
    }

    @Override
    public List<FirstLevelCategoryNodeDTO> getCategoryTreeList() {
        return null;
    }
}
