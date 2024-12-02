package com.denproj.posmanongjaks.util;

import com.denproj.posmanongjaks.session.SessionSimple;

public interface OnLoginSuccessful {
    void onLoginSuccess(SessionSimple sessionSimple);
    void onLoginFailed(Exception e);
}