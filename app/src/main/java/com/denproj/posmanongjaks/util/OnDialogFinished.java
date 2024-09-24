package com.denproj.posmanongjaks.util;

import javax.annotation.Nullable;

public interface OnDialogFinished<T> {
    void onDialogFinishedSuccessfully(T result);
    void onDialogFailed(@Nullable Exception e);
}
