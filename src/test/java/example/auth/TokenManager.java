package example.auth;

import example.utils.AuthResponse;

public class TokenManager {

    private static final Object LOCK = new Object();
    private static String cachedToken;
    private static long expiryEpochMs = 0;

    private static final long DEFAULT_TTL_MS = 10 * 60 * 1000; // 10 mins fallback
    private static final long SKEW_MS = 30 * 1000;

    private TokenManager() {}

    public static String getToken() {
        synchronized (LOCK) {
            if (cachedToken == null || isExpired()) {
                refresh();
            }
            return cachedToken;
        }
    }

    private static boolean isExpired() {
        return System.currentTimeMillis() >= (expiryEpochMs - SKEW_MS);
    }

    private static void refresh() {
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        if (username == null || password == null) {
            throw new IllegalStateException("Missing -Dusername and -Dpassword system properties.");
        }

        AuthResponse lr = new AuthClient().login(username, password);

        // Pick token field based on your API response
        String token = (lr.accessToken != null) ? lr.accessToken : null;

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Auth response did not contain a token.");
        }

        cachedToken = token;

        // If expires_in is present, use it. Otherwise use default TTL.
        long ttlMs = (lr.expiresIn > 0) ? lr.expiresIn * 1000L : DEFAULT_TTL_MS;
        expiryEpochMs = System.currentTimeMillis() + ttlMs;
    }

}
