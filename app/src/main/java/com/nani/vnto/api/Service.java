package com.nani.vnto.api;

import com.nani.vnto.model.Authentication;
import com.nani.vnto.model.Contact;
import com.nani.vnto.responses.AccountResponse;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;


/**
 * Created by nataliajastrzebska on 12/03/16.
 */
public final class Service {


    public static final String KEY_TOKEN = "TOKEN";

    public interface ServiceInterface {
        @POST("tokens")
        @Headers("Content-Type: application/json")
        Call<Authentication> authenticate(@Body RequestBody body);

        @POST("contacts")
        @Headers("Content-Type: application/json")
        Call<AccountResponse> createContact(@Body Contact body);

        /*@GET("user/?private_token")
        Call<JsonElement> getUser(@Query("private_token") String token);

        @GET("projects/?private_token")
        Call<List<JsonElement>> getProjects(@Query("private_token") String token);
*/
    }


}