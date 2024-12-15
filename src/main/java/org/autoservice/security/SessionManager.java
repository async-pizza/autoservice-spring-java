package org.autoservice.security;

import jakarta.servlet.http.Cookie;
import org.autoservice.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, User> sessionStore = new ConcurrentHashMap<>();

    public static void createSession(String sessionId, User user) {
        sessionStore.put(sessionId, user);
    }

    public static User getUserFromSession(String sessionId) {
        return sessionStore.get(sessionId);
    }

    public static boolean isValidSessionId(String sessionId) {
        return sessionStore.containsKey(sessionId);
    }

    public static void invalidateSession(String sessionId) {
        sessionStore.remove(sessionId);
    }
}