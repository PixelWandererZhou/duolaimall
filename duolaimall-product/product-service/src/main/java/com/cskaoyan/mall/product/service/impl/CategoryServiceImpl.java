package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.product.converter.dto.CategoryConverter;
import com.cskaoyan.mall.product.converter.dto.TrademarkConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.*;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import com.cskaoyan.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    CategoryHierarchyMapper categoryHierarchyMapper;

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
        List<CategoryHierarchy> categoryHierarchies = categoryHierarchyMapper.selectCategoryHierarchy(thirdLevelCategoryId);
        if(categoryHierarchies.size()==1){
            return categoryConverter.categoryViewPO2DTO(categoryHierarchies.get(0));
        }
        return null;
    }

    @Override
    public List<FirstLevelCategoryNodeDTO> getCategoryTreeList() {
        //查询所有的一级目录
        List<FirstLevelCategoryDTO> firstLevelCategory = getFirstLevelCategory();
        //查询所有的二级目录
        List<SecondLevelCategoryDTO> secondLevelCategory = categoryConverter.secondLevelCategoryPOs2DTOs(secondLevelCategoryMapper.selectList(null));
        //查询所有的三级目录
        List<ThirdLevelCategoryDTO> thirdLevelCategory = categoryConverter.thirdLevelCategoryPOs2DTOs(thirdLevelCategoryMapper.selectList(null));
        //将一级目录转换成一级目录节点
        List<FirstLevelCategoryNodeDTO> firstLevelCategoryNodeDTOs = categoryConverter.firstLevelCategoryDTOs2NodeDTOs(firstLevelCategory);
        //遍历一级目录节点，筛选出该一级目录下的所有二级目录并转换为二级目录节点
        for(FirstLevelCategoryNodeDTO firstLevelCategoryNodeDTO : firstLevelCategoryNodeDTOs){
            //筛选出该一级目录下的所有二级目录
            List<SecondLevelCategoryDTO> secondLevelCategoryDTOS = secondLevelCategory.stream().filter(secondLevelCategoryDTO -> secondLevelCategoryDTO.getFirstLevelCategoryId().equals(firstLevelCategoryNodeDTO.getCategoryId())).collect(Collectors.toList());
            //将二级目录转换成二级目录节点
            List<SecondLevelCategoryNodeDTO> secondLevelCategoryNodeDTOs = categoryConverter.secondLevelCategoryDTOs2NodeDTOs(secondLevelCategoryDTOS);
            //遍历二级目录节点，筛选出该二级目录下的所有三级目录并转换为三级目录节点
            for(SecondLevelCategoryNodeDTO secondLevelCategoryNodeDTO : secondLevelCategoryNodeDTOs){
                //筛选出该二级目录下的所有三级目录
                List<ThirdLevelCategoryDTO> thirdLevelCategoryDTOS = thirdLevelCategory.stream().filter(thirdLevelCategoryDTO -> thirdLevelCategoryDTO.getSecondLevelCategoryId().equals(secondLevelCategoryNodeDTO.getCategoryId())).collect(Collectors.toList());
                //将三级目录转换成三级目录节点
                List<ThirdLevelCategoryNodeDTO> thirdLevelCategoryNodeDTOs = categoryConverter.thirdLevelCategoryDTOs2NodeDTOs(thirdLevelCategoryDTOS);
                //将三级目录节点添加到二级目录节点中
                secondLevelCategoryNodeDTO.setCategoryChild(thirdLevelCategoryNodeDTOs);
            }
            //将二级目录节点添加到一级目录节点中
            firstLevelCategoryNodeDTO.setCategoryChild(secondLevelCategoryNodeDTOs);
        }
        return firstLevelCategoryNodeDTOs;
    }
}
