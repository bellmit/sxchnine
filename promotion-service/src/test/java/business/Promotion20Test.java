package business;

import com.project.business.Promotion20;
import com.project.model.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import utils.TestObjectCreator;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class Promotion20Test {

    @InjectMocks
    private Promotion20 promotion20;

    @Test
    public void testApplyPromo(){
        List<Product> products = promotion20.applyPromotion(Arrays.asList(TestObjectCreator.createProduct()));

        Assert.assertEquals(20, products.get(0).getPromotion());
        Assert.assertEquals(16.0, products.get(0).getPrice(), 4.0);
        Assert.assertEquals(20.0, products.get(0).getOriginalPrice(), 0.0);
    }
}
