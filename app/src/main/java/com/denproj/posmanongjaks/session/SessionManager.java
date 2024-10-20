package com.denproj.posmanongjaks.session;

public class SessionManager {
    private static Session SESSION = null;

    public synchronized static Session getInstance(String branchId) {
        if (SESSION == null) {
            SESSION = new Session();
            SESSION.setBranchId(branchId);
        }
        return SESSION;
    }

    private SessionManager() {

    }

    public static Session getInstance() {
        return SESSION;
    }
}
