package com.softvision.app.locationfinder.rest;

import com.softvision.app.locationfinder.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private final Retrofit mRetrofit;
    private final ApiServiceInterface mApiServiceInterface;
    private ConnectionPool mConnectionPool = new ConnectionPool(5, 60, TimeUnit.SECONDS);

    public RetrofitHelper() {
        mRetrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .client(getClientWithConfigs(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiServiceInterface = mRetrofit.create(ApiServiceInterface.class);
    }

    public Retrofit getClient() {
        return mRetrofit;
    }

    public ApiServiceInterface getApiServiceInterface() {
        return mApiServiceInterface;
    }

    private OkHttpClient getClientWithConfigs(HttpLoggingInterceptor.Level level) {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(level);
        final Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(final Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("key1", "value1")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        };
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .connectionPool(mConnectionPool)
                .retryOnConnectionFailure(true)
                .build();
        return okHttpClient;
    }
}
