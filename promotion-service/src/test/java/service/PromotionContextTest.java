package service;

import com.project.business.Promotion10;
import com.project.business.PromotionStrategy;
import com.project.service.PromotionContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PromotionContextTest {

    @Spy
    private List<PromotionStrategy> strategies = new ArrayList<>();

    @InjectMocks
    private PromotionContext promotionContext;

    @Before
    public void setUp(){
        strategies.add(new Promotion10());
    }


    @Test
    public void testGetStrategy(){
        promotionContext.loadStrategies();
        PromotionStrategy promotionStrategy = promotionContext.getStrategy(10);
        assertEquals(10, promotionStrategy.getPromotion());
    }
}
