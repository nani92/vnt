package com.nani.vnto.api;

import com.google.gson.JsonElement;
import com.nani.vnto.responses.AccountResponse;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;


/**
 * Created by nataliajastrzebska on 12/03/16.
 */
public final class Service {


    public static final String KEY_TOKEN = "TOKEN";

    public interface GitlabServiceInterface {
        @FormUrlEncoded
        @POST("tokens")
        Call<AccountResponse> authenticate(@Field("email") String username, @Field("password") String password);

        /*@GET("user/?private_token")
        Call<JsonElement> getUser(@Query("private_token") String token);

        @GET("projects/?private_token")
        Call<List<JsonElement>> getProjects(@Query("private_token") String token);
*/
    }
}