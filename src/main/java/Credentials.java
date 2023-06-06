import org.apache.commons.lang3.RandomStringUtils;

public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Credentials from(User user) {
        return new Credentials(user.getEmail(), user.getPassword());
    }

    @Override
    public String toString() {
        return "email: " + email + ", password: " + password;
    }
}

