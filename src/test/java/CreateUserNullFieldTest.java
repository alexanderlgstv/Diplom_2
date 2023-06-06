import org.junit.Test;
import org.junit.After;
import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Description;
import static org.apache.http.HttpStatus.*;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;

@RunWith(Parameterized.class)
public class CreateUserNullFieldTest {

    private UserClient userClient;
    private User user;
    private int statusCode;
    private String message;
    private Response createNullField;

    public CreateUserNullFieldTest(User user, int statusCode, String message) {
        this.user = user;
        this.statusCode = statusCode;
        this.message = message;
    }


    @Parameterized.Parameters(name = "Test Data: {0} {1}")
    public static Object[][] getTestData() {

        return new Object[][]{
                {UserGenerator.getWithoutLogin(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGenerator.getWithoutPassword(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGenerator.getWithoutName(), SC_FORBIDDEN, "Email, password and name are required fields"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Create user without field test")
    @Description("Try to create user with one null field test")
    public void createUserWithoutFieldTest() {

        userClient = new UserClient();
        createNullField = userClient.createUser(user);

        assertThat(statusCode, equalTo(SC_FORBIDDEN));

        assertThat(message, equalTo("Email, password and name are required fields"));
    }

    @After
    public void deleteUser() {
        String accessToken = createNullField.body().jsonPath().getString("accessToken");
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}
