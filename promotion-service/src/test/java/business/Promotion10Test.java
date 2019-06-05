package business;

import com.project.business.Promotion10;
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
public class Promotion10Test {

    @InjectMocks
    private Promotion10 promotion10;

    @Test
    public void testApplyPromo(){

        List<Product> products = promotion10.applyPromotion(Arrays.asList(TestObjectCreator.createProduct()));

        Assert.assertEquals(10, products.get(0).getPromotion());
        Assert.assertEquals(18.0, products.get(0).getPrice(), 2.0);
        Assert.assertEquals(20.0, products.get(0).getOriginalPrice(), 0.0);
    }
}
