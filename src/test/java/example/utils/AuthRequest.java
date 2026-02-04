package example.utils;

public class AuthRequest {
    public String username;
    public String password;

    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
