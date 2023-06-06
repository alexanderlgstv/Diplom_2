import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private final String ORDERS = "orders";

    @Step("Create order")
    public Response orderCreate(Order order, String token) {
        return (Response) given()
                .spec(getSpecSettings())
                .headers("Authorization", token)
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .extract();
    }

    @Step("Get list of orders")
    public Response getOrderList(String token) {
        return (Response) given()
                .spec(getSpecSettings())
                .header("Authorization", token)
                .when()
                .get(ORDERS)
                .then()
                .extract();
    }
}
