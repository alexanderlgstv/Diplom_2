import clients.OrderClient;
import clients.UserClient;
import generators.OrderGenerator;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.Credentials;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class OrderListTest {

    private User user;
    private UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private String accessToken;

    private final String errorMessage = "You should be authorised";

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        User user = UserGenerator.getUser();
        userClient.createUser(user);
        Response loginResponse = userClient.login(Credentials.from(user));
        accessToken = loginResponse.body().jsonPath().getString("accessToken");
        orderClient = new OrderClient();
        orderClient.orderCreate(OrderGenerator.getOrderDefault(), accessToken);
    }


    @Test
    @DisplayName("Get order list. models.User is logged in")
    @Description("Try to get order list when user is logged in")
    public void getOrderListWithAuthTest() {
        Response response = orderClient.getOrderList(accessToken);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_OK, statusCode);

        boolean success = response.jsonPath().getBoolean("success");
        Assert.assertTrue(success);

        List<Object> body = response.jsonPath().getList("orders");
        Assert.assertFalse("Body is empty", body.isEmpty());
    }

    @Test
    @DisplayName("Get order list. models.User is not logged in")
    @Description("Try to get order list when user is not logged in")
    public void getOrderListWithoutAuthTest() {
        Response response = orderClient.getOrderList("");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_UNAUTHORIZED, statusCode);

        boolean success = response.jsonPath().getBoolean("success");
        Assert.assertFalse(success);

        String message = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, message);
    }

    @After
    public void deleteUser() {
        userClient.delete(accessToken);
    }
}
