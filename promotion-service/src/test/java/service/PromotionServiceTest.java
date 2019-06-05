package service;

import com.project.business.Promotion10;
import com.project.client.ProductClient;
import com.project.model.Product;
import com.project.service.PromotionContext;
import com.project.service.PromotionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import utils.TestObjectCreator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PromotionServiceTest {

    @Mock
    private ProductClient productClient;

    @Mock
    private PromotionContext promotionContext;

    @InjectMocks
    private PromotionService promotionService;

    @Test
    public void testProcessPromotion(){
        Product product = TestObjectCreator.createProduct();
        when(productClient.getProductById(ArgumentMatchers.anyString())).thenReturn(product);
        when(promotionContext.getStrategy(anyInt())).thenReturn(new Promotion10());
        when(productClient.createOrUpdateProduct(any(Product.class))).thenReturn(product);

        promotionService.processPromotion(10, "2");

        product.setOriginalPrice(20.0);
        product.setPromotion(10);

        verify(productClient).getProductById("2");
        verify(promotionContext).getStrategy(10);
        verify(productClient).createOrUpdateProduct(product);
    }
}
