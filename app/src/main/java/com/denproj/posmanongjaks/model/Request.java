package com.denproj.posmanongjaks.model;

public class Request<T>{
    T data;
    boolean isSuccessful;
    Exception exception;

    public Request(T data, boolean isSuccessful, Exception exception) {
        this.data = data;
        this.isSuccessful = isSuccessful;
        this.exception = exception;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
