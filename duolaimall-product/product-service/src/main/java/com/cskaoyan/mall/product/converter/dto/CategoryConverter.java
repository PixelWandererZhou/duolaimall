package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.model.CategoryHierarchy;
import com.cskaoyan.mall.product.model.FirstLevelCategory;
import com.cskaoyan.mall.product.model.SecondLevelCategory;
import com.cskaoyan.mall.product.model.ThirdLevelCategory;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryConverter {

    FirstLevelCategoryDTO firstLevelCategoryPO2DTO(FirstLevelCategory firstLevelCategory);
    List<FirstLevelCategoryDTO> firstLevelCategoryPOs2DTOs(List<FirstLevelCategory> firstLevelCategories);

    SecondLevelCategoryDTO secondLevelCategoryPO2DTO(SecondLevelCategory secondLevelCategory);
    List<SecondLevelCategoryDTO> secondLevelCategoryPOs2DTOs(List<SecondLevelCategory> secondLevelCategories);

    ThirdLevelCategoryDTO thirdLevelCategoryPO2DTO(ThirdLevelCategory thirdLevelCategory);
    List<ThirdLevelCategoryDTO> thirdLevelCategoryPOs2DTOs(List<ThirdLevelCategory> thirdLevelCategories);

    CategoryHierarchyDTO categoryViewPO2DTO(CategoryHierarchy categoryHierarchy);
    @Mapping(source = "firstLevelCategoryDTO.name",target = "categoryName")
    @Mapping(source = "firstLevelCategoryDTO.id",target = "categoryId")
    @Mapping(source = "firstLevelCategoryDTO.id",target = "index")
    FirstLevelCategoryNodeDTO firstLevelCategoryDTO2NodeDTO(FirstLevelCategoryDTO firstLevelCategoryDTO);
    List<FirstLevelCategoryNodeDTO> firstLevelCategoryDTOs2NodeDTOs(List<FirstLevelCategoryDTO> firstLevelCategoryDTOS);
    @Mapping(source = "secondLevelCategoryDTO.name",target = "categoryName")
    @Mapping(source = "secondLevelCategoryDTO.id",target = "categoryId")
    SecondLevelCategoryNodeDTO secondLevelCategoryDTO2NodeDTO(SecondLevelCategoryDTO secondLevelCategoryDTO);
    List<SecondLevelCategoryNodeDTO> secondLevelCategoryDTOs2NodeDTOs(List<SecondLevelCategoryDTO> secondLevelCategoryDTOS);
    @Mapping(source = "thirdLevelCategoryDTO.name",target = "categoryName")
    @Mapping(source = "thirdLevelCategoryDTO.id",target = "categoryId")
    ThirdLevelCategoryNodeDTO thirdLevelCategoryDTO2NodeDTO(ThirdLevelCategoryDTO thirdLevelCategoryDTO);
    List<ThirdLevelCategoryNodeDTO> thirdLevelCategoryDTOs2NodeDTOs(List<ThirdLevelCategoryDTO> thirdLevelCategoryDTOS);

}
