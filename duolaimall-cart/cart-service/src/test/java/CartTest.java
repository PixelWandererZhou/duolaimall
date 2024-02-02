import com.cskaoyan.mall.cart.ServiceCartApplication;
import com.cskaoyan.mall.cart.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes= ServiceCartApplication.class)
@RunWith(SpringRunner.class)
public class CartTest {
    @Autowired
    CartService cartService;
    @Test
    public void test() {
        cartService.addToCart(1L, "1", 1);
    }
}
