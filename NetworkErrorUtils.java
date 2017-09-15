package com.softvision.app.locationfinder.rest;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class NetworkErrorUtils {

    public static ApiError parseError(RetrofitHelper retrofitHelper, Response<?> response) {
        Converter<ResponseBody, ApiError> converter =
                retrofitHelper.getClient().responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }

        return error;
    }
}
