import com.cskaoyan.mall.search.ServiceSearchApplication;
import com.cskaoyan.mall.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ServiceSearchApplication.class)
@RunWith(SpringRunner.class)
public class ListApiTest {
    @Autowired
    SearchService searchService;
    @Test
    public void testUpperGoods() {
        searchService.upperGoods(3L);
        searchService.upperGoods(4L);
        searchService.lowerGoods(3L);
    }
}
