package com.cskaoyan.mall.cart.service.impl;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.cart.client.ProductApiClient;
import com.cskaoyan.mall.cart.converter.SkuInfoConverter;
import com.cskaoyan.mall.cart.service.CartService;
import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    RedissonClient redissonSingle;
    @Autowired
    ProductApiClient productApiClient;
    @Autowired
    SkuInfoConverter skuInfoConverter;
    @Override
    public void addToCart(Long skuId, String userId, Integer skuNum) {
        String key = RedisConst.USER_KEY_PREFIX+ userId + RedisConst.USER_CART_KEY_SUFFIX;
        // 获取用户购物车
        RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
        // 查看用户购物车中是否包含该商品
        boolean containsKey = map.containsKey(skuId);
        // 如果包含，更新数量（根据skuId，获取购物车中的CartInfoDTO, 修改数量）
        if(containsKey){
            // 更新数量
            CartInfoDTO cartInfoDTO = map.get(skuId);
            cartInfoDTO.setSkuNum(cartInfoDTO.getSkuNum() + skuNum);
            //存入redis
            map.put(skuId, cartInfoDTO);
        }
        // 如果不包含，添加商品到购物车(根据skuId调用商品服务(skuName, skuPrice, defaultImg) + skuNum ——> CartInfoDTO)
        else {
            //查询商品信息
            SkuInfoDTO skuInfo = productApiClient.getSkuInfo(skuId);
            // 添加商品到购物车
            CartInfoDTO cartInfoDTO = skuInfoConverter.skuInfoToCartInfo(skuInfo, skuNum, skuId, userId);
            //存入redis
            map.put(skuId, cartInfoDTO);
        }
    }

    @Override
    public List<CartInfoDTO> getCartList(String userId, String userTempId) {
        List<CartInfoDTO> cartInfoDTOList = new ArrayList<>();
        //判断userId是否为空
        if(userId.isEmpty()){
            //用户未登录
            //获取临时用户购物车
            String key = RedisConst.USER_KEY_PREFIX+ userTempId  + RedisConst.USER_CART_KEY_SUFFIX;
            RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
            //获取临时用户购物车中的所有商品
            cartInfoDTOList = new ArrayList<>(map.values());
        }
        else {
            //用户已登录
            //获取临时用户购物车
            String tempKey = RedisConst.USER_KEY_PREFIX+ userTempId  + RedisConst.USER_CART_KEY_SUFFIX;
            RMap<Long, CartInfoDTO> map = redissonSingle.getMap(tempKey);
            //获取临时用户购物车中的所有商品
            List<CartInfoDTO> tempCartInfoDTOList = new ArrayList<>(map.values());
            //获取登录用户购物车
            String loginedKey = RedisConst.USER_KEY_PREFIX+ userId  + RedisConst.USER_CART_KEY_SUFFIX;
            RMap<Long, CartInfoDTO> loginedMap = redissonSingle.getMap(loginedKey);
            if(tempCartInfoDTOList.isEmpty()){
                return new ArrayList<>(loginedMap.values());
            }
            //合并购物车
            for (CartInfoDTO tempCartInfoDTO : tempCartInfoDTOList) {
                //判断登录用户购物车中是否包含该商品
                boolean containsKey = loginedMap.containsKey(tempCartInfoDTO.getSkuId());
                if(containsKey){
                    //包含，更新数量
                    CartInfoDTO loginedCartInfoDTO = loginedMap.get(tempCartInfoDTO.getSkuId());
                    loginedCartInfoDTO.setSkuNum(loginedCartInfoDTO.getSkuNum() + tempCartInfoDTO.getSkuNum());
                    //存入redis
                    loginedMap.put(tempCartInfoDTO.getSkuId(), loginedCartInfoDTO);
                }
                else {
                    //不包含，添加商品到购物车
                    tempCartInfoDTO.setUserId(userId);
                    loginedMap.put(tempCartInfoDTO.getSkuId(), tempCartInfoDTO);
                }
                cartInfoDTOList = new ArrayList<>(loginedMap.values());
            }
            map.delete();
        }
        return cartInfoDTOList;
    }

    @Override
    public void checkCart(String userId, Integer isChecked, Long skuId) {
        //获取用户购物车
        String key = RedisConst.USER_KEY_PREFIX+ userId + RedisConst.USER_CART_KEY_SUFFIX;
        RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
        //获取购物车中的所有商品
        Collection<CartInfoDTO> values = map.values();
        //遍历购物车中的所有商品
        for (CartInfoDTO cartInfoDTO : values) {
            //判断商品是否为选中状态
            if(cartInfoDTO.getSkuId().equals(skuId)){
                //修改商品的选中状态
                cartInfoDTO.setIsChecked(isChecked);
                //存入redis
                map.put(skuId, cartInfoDTO);
            }
        }
    }

    @Override
    public void deleteCart(Long skuId, String userId) {
        String key = RedisConst.USER_KEY_PREFIX+ userId + RedisConst.USER_CART_KEY_SUFFIX;
        //删除购物车中的对应商品
        RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
        map.remove(skuId);
    }

    @Override
    public void deleteChecked(String userId) {
        //获取用户购物车
        String key = RedisConst.USER_KEY_PREFIX+ userId + RedisConst.USER_CART_KEY_SUFFIX;
        RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
        //获取购物车中的所有商品
        Collection<CartInfoDTO> values = map.values();
        //遍历购物车中的所有商品
        for (CartInfoDTO cartInfoDTO : values) {
            //判断商品是否为选中状态
            if(cartInfoDTO.getIsChecked() == 1){
                //删除购物车中的对应商品
                map.remove(cartInfoDTO.getSkuId());
            }
        }
    }

    @Override
    public List<CartInfoDTO> getCartCheckedList(String userId) {
        //获取用户购物车
        String key = RedisConst.USER_KEY_PREFIX+ userId + RedisConst.USER_CART_KEY_SUFFIX;
        RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
        //获取购物车中的所有isCheck为1的商品
        ArrayList<CartInfoDTO> cartInfoDTOS = new ArrayList<>(map.values());
        return cartInfoDTOS.stream().filter(cartInfoDTO -> cartInfoDTO.getIsChecked() == 1).collect(Collectors.toList());
    }

    @Override
    public void delete(String userId, List<Long> skuIds) {
        //获取用户购物车
        String key = RedisConst.USER_KEY_PREFIX+ userId + RedisConst.USER_CART_KEY_SUFFIX;
        RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
        //删除购物车中的对应商品
        for (Long skuId : skuIds) {
            map.remove(skuId);
        }
    }

    @Override
    public void refreshCartPrice(String userId, Long skuId) {
        //查询skuInfo
        SkuInfoDTO skuInfo = productApiClient.getSkuInfo(skuId);
        //获取用户购物车
        String key = RedisConst.USER_KEY_PREFIX+ userId + RedisConst.USER_CART_KEY_SUFFIX;
        RMap<Long, CartInfoDTO> map = redissonSingle.getMap(key);
        //获取购物车中对应skuId的商品
        CartInfoDTO cartInfoDTO = map.get(skuId);
        //更新商品价格
        cartInfoDTO.setSkuPrice(skuInfo.getPrice());
        //存入redis
        map.put(skuId, cartInfoDTO);
    }
}
