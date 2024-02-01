import com.cskaoyan.mall.search.ServiceSearchApplication;
import com.cskaoyan.mall.search.param.SearchParam;
import com.cskaoyan.mall.search.service.SearchService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.cskaoyan.mall.search.mapper.GoodsMapper;

import java.io.IOException;

@SpringBootTest(classes = ServiceSearchApplication.class)
@RunWith(SpringRunner.class)
public class ListApiTest {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    SearchService searchService;
    @Test
    public void testCreateIndex() {
        // 测试创建索引,框架会根据实体类及字段上加的自定义注解一键帮您生成索引 需确保索引托管模式处于manual手动挡(默认处于此模式),若为自动挡则会冲突
        boolean success = goodsMapper.createIndex("goods");
        Assertions.assertTrue(success);
    }
    @Test
    public void upperGoods(){
        searchService.lowerGoods(3L);
    }
    @Test
    public void list() throws IOException {
        SearchParam searchParam = new SearchParam();
        searchParam.setKeyword("小米");
        searchParam.setTrademark("11:小米");
        searchService.search(searchParam);
    }
}
