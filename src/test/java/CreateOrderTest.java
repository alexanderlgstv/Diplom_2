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

import static org.apache.http.HttpStatus.*;


public class CreateOrderTest {

    private Order order;
    private OrderClient orderClient;
    private User user;
    private UserClient userClient;
    private String accessToken;

    private static final String errorIngredientsMessage = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        User user = UserGenerator.getUser();
        userClient = new UserClient();
        userClient.createUser(user);
        Response responseLogin = userClient.login(Credentials.from(user));
        accessToken = responseLogin.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Create order. models.User is logged in")
    @Description("Try to create order when user is logged in")
    public void createOrderWithAuthAvailableTest() {
        order = OrderGenerator.getOrderDefault();
        Response orderResponse = orderClient.orderCreate(order, accessToken);

        int statusCode = orderResponse.getStatusCode();
        Assert.assertEquals(SC_OK, statusCode);

        boolean orderCreate = orderResponse.jsonPath().getBoolean("success");
        Assert.assertTrue(orderCreate);

        int orderNumber = orderResponse.jsonPath().getInt("order.number");
        Assert.assertNotEquals(0, orderNumber);
    }

    @Test
    @DisplayName("Create order. models.User is not logged in")
    @Description("Try to create order when user is not logged in")
    public void createOrderWithoutAuthAvailableTest() {
        order = OrderGenerator.getOrderDefault();
        Response response = orderClient.orderCreate(order, "");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_OK, statusCode);

        boolean orderCreate = response.jsonPath().getBoolean("success");
        Assert.assertTrue(orderCreate);
    }

    @Test
    @DisplayName("Create order. models.Order without ingredients")
    @Description("Try to create order without ingredients")
    public void createOrderWithoutIngredientsTest() {
        order = OrderGenerator.getOrderWithoutIngredients();
        Response response = orderClient.orderCreate(order, "accessToken");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_BAD_REQUEST, statusCode);

        boolean orderCreate = response.jsonPath().getBoolean("success");
        Assert.assertFalse(orderCreate);

        String message = response.jsonPath().getString("message");
        Assert.assertEquals(errorIngredientsMessage, message);
    }

    @Test
    @DisplayName("Create order. models.Order with wrong hash ingredients")
    @Description("Try to create order with wrong hash ingredients")
    public void createOrderWithWrongHashTest() {
        order = OrderGenerator.getOrderWithWrongIngredientsHash();
        Response response = orderClient.orderCreate(order, accessToken);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

    @After
    public void deleteUser() {
        userClient.delete(accessToken);
    }
}