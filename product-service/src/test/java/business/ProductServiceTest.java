package business;

import com.project.business.KafkaProducer;
import com.project.business.ProductService;
import com.project.model.Product;
import com.project.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import utils.TestObjectCreator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProductById(){

        when(productRepository.findById("1")).thenReturn(Optional.of(TestObjectCreator.createProduct()));

        Product productFound = productService.getProductById("1");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(productRepository).findById(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue());

        assertEquals("p1", productFound.getName());
        assertEquals(BigDecimal.valueOf(1.0), productFound.getPrice());
    }

    @Test
    public void testGetAllProducts(){
        when(productRepository.findAll()).thenReturn(Collections.singletonList(TestObjectCreator.createProduct()));

        List<Product> productList = productService.getAllProducts();

        assertEquals("1", productList.get(0).getId());
        assertEquals("p1", productList.get(0).getName());
        assertEquals(BigDecimal.valueOf(1.0), productList.get(0).getPrice());
    }

    @Test
    public void testSave(){
        Product product = TestObjectCreator.createProduct();
        when(productRepository.save(product)).thenReturn(product);

        Product productSaved = productService.save(product);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(argumentCaptor.capture());

        assertEquals("1", argumentCaptor.getValue().getId());

        assertEquals("1", productSaved.getId());
        assertEquals("p1", productSaved.getName());
        assertEquals(BigDecimal.valueOf(1.0), productSaved.getPrice());
    }

    @Test
    public void testDeleteProductById(){
        productService.deleteProductById("1");
    }

    @Test
    public void testDeleteProduct(){
        productService.deleteProduct(TestObjectCreator.createProduct());
    }
}
