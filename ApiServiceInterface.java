package com.softvision.app.locationfinder.rest;

import com.softvision.app.locationfinder.model.PeopleResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceInterface {
    @GET("people")
    Call<PeopleResponse> getPeople();
}
