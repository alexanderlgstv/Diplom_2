import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import models.Credentials;
import models.User;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class UserUpdateTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private final String errorMessage = "You should be authorised";
    private final String emailErrorMessage = "User with such email already exists";

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userClient = new UserClient();
        userClient.createUser(user);
        Response loginResponse = userClient.login(Credentials.from(user));
        accessToken = loginResponse.body().jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Change data when user is logged in")
    @Description("Authorized user try to update data")
    public void updateAuthorizedUserTest() {
        User updateUser = UserGenerator.getUser();
        Response UpdateUserResponse = userClient.updateUser(updateUser, accessToken);

        int statusCode = UpdateUserResponse.getStatusCode();
        assertThat(statusCode, equalTo(SC_OK));

        boolean isUpdateUserResponseSuccess = UpdateUserResponse.jsonPath().getBoolean("success");
        assertTrue(isUpdateUserResponseSuccess);

        String email = UpdateUserResponse.jsonPath().getString("user.email");
        assertEquals(updateUser.getEmail().toLowerCase(), email);

        String name = UpdateUserResponse.jsonPath().getString("user.name");
        assertEquals(updateUser.getName(), name);
    }

    @Test
    @DisplayName("Change data when user is not logged in")
    @Description("Unauthorized user try to update data")
    public void updateUnauthorizedUserTest() {
        Response UpdateUserResponse = userClient.updateUser(UserGenerator.getUser(), "");

        int statusCode = UpdateUserResponse.getStatusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));

        String message = UpdateUserResponse.jsonPath().getString("message");
        assertEquals(errorMessage, message);
    }

    @Test
    @DisplayName("Update email to existing")
    @Description("User try to update email to existing")
    public void updateExistingEmailTest() {
        User newUser = UserGenerator.getUser();
        userClient.createUser(newUser);

        Response responseLoginNewUser = userClient.login(Credentials.from(newUser));
        String NewUserEmail = responseLoginNewUser.body().jsonPath().getString("user.email");

        User updateExistingEmail = new User(NewUserEmail, user.getPassword(), user.getName());
        Response responseRegUpdateUser = userClient.updateUser(updateExistingEmail, accessToken);

        int statusCode = responseRegUpdateUser.getStatusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));

        String message = responseRegUpdateUser.jsonPath().getString("message");
        assertEquals(emailErrorMessage, message);
    }

    @Test
    @DisplayName("Update email when user is logged in")
    @Description("Authorized user try to update email")
    public void updateUserEmailTest() {
        User updateUserEmail = new User(UserGenerator.getUser().getEmail(), user.getPassword(), user.getName());
        Response UpdateUserResponse = userClient.updateUser(updateUserEmail, accessToken);

        int statusCode = UpdateUserResponse.getStatusCode();
        assertThat(statusCode, equalTo(SC_OK));

        boolean isUpdateUserResponseSuccess = UpdateUserResponse.jsonPath().getBoolean("success");
        assertTrue(isUpdateUserResponseSuccess);

        String email = UpdateUserResponse.jsonPath().getString("user.email");
        assertEquals(updateUserEmail.getEmail().toLowerCase(), email);
    }

    @Test
    @DisplayName("Update password when user is logged in")
    @Description("Authorized user try to update password")
    public void updatePasswordTest() {
        String newPassword = UserGenerator.getUser().getPassword();
        User newUser = new User(user.getEmail(), newPassword, user.getName());
        var UpdateUserResponse = userClient.updateUser(newUser, accessToken);

        boolean responseSuccess = UpdateUserResponse.jsonPath().getBoolean("success");
        Assert.assertTrue(responseSuccess);

        int statusCode = UpdateUserResponse.getStatusCode();
        Assert.assertEquals(statusCode, SC_OK);
    }

    @After
    public void deleteUser() {
        userClient.delete(accessToken);
    }
}