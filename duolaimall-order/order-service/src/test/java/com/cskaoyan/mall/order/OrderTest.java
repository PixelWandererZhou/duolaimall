package com.cskaoyan.mall.order;

import com.cskaoyan.mall.order.client.UserApiClient;
import com.cskaoyan.mall.order.service.OrderService;
import com.cskaoyan.mall.user.dto.UserAddressDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = ServiceOrderApplication.class)
@RunWith(SpringRunner.class)
public class OrderTest {
    @Autowired
    OrderService orderService;
    @Autowired
    UserApiClient userApiClient;
    @Test
    public void test() {
        List<UserAddressDTO> userAddressListByUserId = userApiClient.findUserAddressListByUserId("1");
        System.out.println();
    }
}