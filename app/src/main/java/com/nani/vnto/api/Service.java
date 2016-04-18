package com.nani.vnto.api;

import com.nani.vnto.model.Authentication;
import com.nani.vnto.model.Contact;
import com.nani.vnto.model.Photo;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;


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
        Call<Contact> createContact(@Body Contact body);

        @GET("contacts")
        @Headers("Content-Type: application/json")
        Call<List<com.nani.vnto.model.Contact>> getContacts();

        @GET("contacts/{id}/photo")
        Call<Photo> getPhoto(@Path("id") int id);

        @Multipart
        @POST("contacts/{id}/photo")
        @Headers("Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
        Call<Photo> setPhoto(@Path("id") int id, @Part("file") RequestBody photoFile);

        @GET("contacts/{id}")
        Call<Contact> getContact(@Path("id") int id);

        @DELETE("contacts/{id}")
        @Headers("Content-Type: application/json")
        Call<Contact> deleteContact(@Path("id") int id);

        @PUT("contacts/{id}")
        @Headers("Content-Type: application/json")
        Call<Contact> editContact(@Path("id") int id, @Body Contact contact);
    }
}