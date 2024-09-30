package com.denproj.posmanongjaks.session;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;

public class Session {
    private static User user = null;
    private static Role role = null;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Session.user = user;
    }

    public static Role getRole() {
        return role;
    }

    public static void setRole(Role role) {
        Session.role = role;
    }
}
