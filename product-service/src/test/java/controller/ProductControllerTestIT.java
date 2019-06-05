package controller;

import com.project.ProductServiceApplication;
import com.project.business.ProductService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"application-test.yml"})
public class ProductControllerTestIT {

    @LocalServerPort
    private int serverPort;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = serverPort;
    }


    @Test
    public void testGetProductById(){
        RestAssured.when().get("/id/1").then().statusCode(200).assertThat().body("id", equalTo("1"));
    }
}
