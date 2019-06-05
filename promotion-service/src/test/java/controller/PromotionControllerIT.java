package controller;

import com.project.PromotionApplication;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = PromotionApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PromotionControllerIT {

    @LocalServerPort
    private int portServer;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = portServer;
    }

    @Test
    public void testApplyPromoIT(){
        RestAssured.when().post("/applyPromotion/10/productId/2").then().statusCode(200);
    }
}
