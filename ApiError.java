package com.softvision.app.locationfinder.rest;

/**
 * Created by lavakush.v on 15-09-2017.
 */

public class ApiError {
    private String code;
    private String message;

    public ApiError() {
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
