package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.TrademarkConverter;
import com.cskaoyan.mall.product.converter.dto.TrademarkPageConverter;
import com.cskaoyan.mall.product.dto.TrademarkDTO;
import com.cskaoyan.mall.product.dto.TrademarkPageDTO;
import com.cskaoyan.mall.product.mapper.TrademarkMapper;
import com.cskaoyan.mall.product.model.Trademark;
import com.cskaoyan.mall.product.query.TrademarkParam;
import com.cskaoyan.mall.product.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrademarkServiceImpl implements TrademarkService {
    @Autowired
    TrademarkMapper trademarkMapper;
    @Autowired
    TrademarkConverter trademarkConverter;
    @Autowired
    TrademarkPageConverter trademarkPageConverter;


    @Override
    public TrademarkDTO getTrademarkByTmId(Long tmId) {
        Trademark trademark = trademarkMapper.selectById(tmId);
        return trademarkConverter.trademarkPO2DTO(trademark);
    }

    @Override
    public TrademarkPageDTO getPage(Page<Trademark> pageParam) {
        Page<Trademark> trademarkPage = trademarkMapper.selectPage(pageParam, null);
        return trademarkPageConverter.tradeMarkPagePO2PageDTO(trademarkPage);
    }

    @Override
    public void save(TrademarkParam trademarkParam) {
        Trademark trademark = trademarkConverter.trademarkParam2Trademark(trademarkParam);
        trademarkMapper.insert(trademark);
    }

    @Override
    public void updateById(TrademarkParam trademarkParam) {
        Trademark trademark = trademarkConverter.trademarkParam2Trademark(trademarkParam);
        trademarkMapper.updateById(trademark);
    }

    @Override
    public void removeById(Long id) {
        trademarkMapper.deleteById(id);
    }
}
