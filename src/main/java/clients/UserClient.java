package clients;

import clients.Client;
import models.User;
import models.Credentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;

public class UserClient extends Client {

    private static final String REGISTER = "auth/register/";
    private static final String LOGIN = "auth/login/";
    private static final String UPDATE = "auth/user/";
    private static final String DELETE = "auth/user/";

    @Step("User Registration")
    public Response createUser(User user) {
        return (Response) given()
                .spec(getSpecSettings())
                .body(user)
                .when()
                .post(REGISTER)
                .then()
                .extract();
    }

    @Step("User Login")
    public static Response login(Credentials credentials) {
        return (Response) given()
                .spec(getSpecSettings())
                .body(credentials)
                .when()
                .post(LOGIN)
                .then()
                .extract();
    }

    @Step("User Modify")
    public Response updateUser(User user, String accessToken) {
        return (Response) given()
                .spec(getSpecSettings())
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(UPDATE)
                .then()
                .extract();
    }

    @Step("Delete User")
    public void delete(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .spec(getSpecSettings())
                .header("authorization", accessToken)
                .when()
                .delete(DELETE)
                .then()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .extract()
                .path("ok");
    }
}

