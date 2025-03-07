package com.aa.designmyexperience.Models;

public class Session {
    private static Session instance;
    private User currentUser;

    private Session(User user) {
        this.currentUser = user;
    }

    public static void createSession(User user) {
        instance = new Session(user);
    }

    public static Session getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        instance = null;
    }
}
