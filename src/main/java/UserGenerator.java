import net.datafaker.Faker;

public class UserGenerator {

    static Faker faker = new Faker();

    public static User getUser() {
        String name = faker.name().firstName();
        String password = faker.password().toString();
        String email = faker.internet().emailAddress();

        return new User(email, password, name);
    }

    public static Credentials getCredentials() {
        String email = faker.internet().emailAddress();
        String password = faker.password().toString();

        return new Credentials(email, password);
    }

    public static User getWithoutLogin() {
        String name = faker.name().firstName();
        String password = faker.password().toString();
        return new User(null, name, password);
    }
    public static User getWithoutPassword() {
        String email = faker.internet().emailAddress();
        String name = faker.name().firstName();
        return new User(email, name, null);
    }
    public static User getWithoutName() {
        String email = faker.internet().emailAddress();
        String password = faker.password().toString();
        return new User(email, null, password);
    }
}