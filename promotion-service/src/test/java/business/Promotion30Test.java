package business;

import com.project.business.Promotion30;
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
public class Promotion30Test {

    @InjectMocks
    private Promotion30 promotion30;

    @Test
    public void testApplyPromo(){
        List<Product> products = promotion30.applyPromotion(Arrays.asList(TestObjectCreator.createProduct()));

        Assert.assertEquals(30, products.get(0).getPromotion());
        Assert.assertEquals(14.0, products.get(0).getPrice(), 6.0);
        Assert.assertEquals(20.0, products.get(0).getOriginalPrice(), 0.0);
    }
}
