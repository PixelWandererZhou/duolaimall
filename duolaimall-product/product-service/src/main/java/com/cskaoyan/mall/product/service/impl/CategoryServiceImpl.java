package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.product.converter.dto.CategoryConverter;
import com.cskaoyan.mall.product.converter.dto.TrademarkConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.CategoryTrademark;
import com.cskaoyan.mall.product.model.SecondLevelCategory;
import com.cskaoyan.mall.product.model.ThirdLevelCategory;
import com.cskaoyan.mall.product.model.Trademark;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import com.cskaoyan.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    FirstLevelCategoryMapper firstLevelCategoryMapper;
    @Autowired
    SecondLevelCategoryMapper secondLevelCategoryMapper;
    @Autowired
    ThirdLevelCategoryMapper thirdLevelCategoryMapper;
    @Autowired
    TrademarkMapper trademarkMapper;
    @Autowired
    CategoryTrademarkMapper categoryTrademarkMapper;

    @Autowired
    TrademarkConverter trademarkConverter;
    @Autowired
    CategoryConverter categoryConverter;
    @Override
    public List<FirstLevelCategoryDTO> getFirstLevelCategory() {
        return categoryConverter.firstLevelCategoryPOs2DTOs(firstLevelCategoryMapper.selectList(null));
    }

    @Override
    public List<SecondLevelCategoryDTO> getSecondLevelCategory(Long firstLevelCategoryId) {
        Map<String,Object> map = new HashMap<>();
        map.put("first_level_category_id",firstLevelCategoryId);
        List<SecondLevelCategory> secondLevelCategories = secondLevelCategoryMapper.selectByMap(map);
        return categoryConverter.secondLevelCategoryPOs2DTOs(secondLevelCategories);
    }

    @Override
    public List<ThirdLevelCategoryDTO> getThirdLevelCategory(Long secondLevelCategoryId) {
        Map<String,Object> map = new HashMap<>();
        map.put("second_level_category_id",secondLevelCategoryId);
        List<ThirdLevelCategory> thirdLevelCategories = thirdLevelCategoryMapper.selectByMap(map);
        return categoryConverter.thirdLevelCategoryPOs2DTOs(thirdLevelCategories);
    }

    @Override
    public List<TrademarkDTO> findTrademarkList(Long category3Id) {
        Map<String,Object> map = new HashMap<>();
        map.put("third_level_category_id",category3Id);
        List<CategoryTrademark> categoryTrademarks = categoryTrademarkMapper.selectByMap(map);
        // get all trademark ids to array
        Long[] trademarkIds = categoryTrademarks.stream().map(CategoryTrademark::getTrademarkId).toArray(Long[]::new);
        //select all trademark by ids
        List<Trademark> trademarks = trademarkMapper.selectBatchIds(Arrays.asList(trademarkIds));
        return trademarkConverter.trademarkPOs2DTOs(trademarks);
    }

    @Override
    public void save(CategoryTrademarkParam categoryTrademarkParam) {
        //iterator trademarkIdList
        for (int i = 0; i < categoryTrademarkParam.getTrademarkIdList().size(); i++) {
            CategoryTrademark categoryTrademark = new CategoryTrademark();
            categoryTrademark.setThirdLevelCategoryId(categoryTrademarkParam.getCategory3Id());
            categoryTrademark.setTrademarkId(categoryTrademarkParam.getTrademarkIdList().get(i));
            categoryTrademarkMapper.insert(categoryTrademark);
        }
    }

    @Override
    public List<TrademarkDTO> findUnLinkedTrademarkList(Long thirdLevelCategoryId) {
        // get all linked trademark ids
        Map<String,Object> map = new HashMap<>();
        map.put("third_level_category_id",thirdLevelCategoryId);
        List<CategoryTrademark> categoryTrademarks = categoryTrademarkMapper.selectByMap(map);
        Long[] linkedTrademarkIds = categoryTrademarks.stream().map(CategoryTrademark::getTrademarkId).toArray(Long[]::new);
        // get all trademark ids
        List<Trademark> trademarks = trademarkMapper.selectList(null);
        Long[] trademarkIds = trademarks.stream().map(Trademark::getId).toArray(Long[]::new);
        // get unlinked trademark ids
        Long[] unlinkedTrademarkIds = Arrays.stream(trademarkIds).filter(trademarkId -> !Arrays.asList(linkedTrademarkIds).contains(trademarkId)).toArray(Long[]::new);
        // select unlinked trademark by ids
        List<Trademark> unlinkedTrademarks = trademarkMapper.selectBatchIds(Arrays.asList(unlinkedTrademarkIds));
        return trademarkConverter.trademarkPOs2DTOs(unlinkedTrademarks);
    }

    @Override
    public void remove(Long thirdLevelCategoryId, Long trademarkId) {
        Map<String,Object> map = new HashMap<>();
        map.put("third_level_category_id",thirdLevelCategoryId);
        map.put("trademark_id",trademarkId);
        categoryTrademarkMapper.deleteByMap(map);
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
