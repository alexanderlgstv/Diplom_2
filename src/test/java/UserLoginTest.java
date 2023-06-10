import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import models.Credentials;
import models.User;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private final String errorMessage = "email or password are incorrect";

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userClient = new UserClient();
        userClient.createUser(user);
    }

    @Test
    @DisplayName("Successful user authorization with valid credentials")
    @Description("models.User try to logged in with valid credentials ")
    public void userValidCredentialsLoginTest() {
        Response loginResponse = userClient.login(Credentials.from(user));

        int statusCode = loginResponse.getStatusCode();
        assertThat(statusCode, equalTo(SC_OK));

        boolean successLogin = loginResponse.body().jsonPath().getBoolean("success");
        assertTrue(successLogin);

        accessToken = loginResponse.body().jsonPath().getString("accessToken");
        assertNotNull(accessToken);

        String refreshToken = loginResponse.body().jsonPath().getString("accessToken");
        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("Login with invalid credentials")
    @Description("models.User try to logged in with invalid credentials")
    public void userInvalidCredentialsLoginTest() {
        Response loginResponse = userClient.login(UserGenerator.getCredentials());

        int statusCode = loginResponse.getStatusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));

        String message = loginResponse.body().jsonPath().getString("message");
        assertThat(message, equalTo(errorMessage));
    }

    @After
    public void deleteUser() {
        userClient.delete(accessToken);
    }
}