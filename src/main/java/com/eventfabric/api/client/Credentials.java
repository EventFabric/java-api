package ef.api.client;

public class Credentials {
    private final String username;
    private final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return this.username + ":" + this.password; 
    }
}
