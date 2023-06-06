import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;

public class CreateNewUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("New unique user creating")
    @Description("Try to create new unique user")
    public void uniqueUserCreateTest() {
        Response responseReg = userClient.createUser(user);

        int statusCode = responseReg.getStatusCode();
        assertThat(statusCode, equalTo(SC_OK));

        boolean successCreated = responseReg.jsonPath().getBoolean("success");
        assertTrue(successCreated);

        accessToken = responseReg.body().jsonPath().getString("accessToken");
        assertNotNull(accessToken);

        String refreshToken = responseReg.body().jsonPath().getString("accessToken");
        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("Existing user try to create")
    @Description("Try to create existing user")
    public void existingUserCreateTest() {
        userClient.createUser(user);
        Response doubleReg = userClient.createUser(user);

        accessToken = doubleReg.body().jsonPath().getString("accessToken");

        int statusCode = doubleReg.getStatusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }

    @After
    public void deleteUser() {
        userClient.delete(accessToken);
    }
}