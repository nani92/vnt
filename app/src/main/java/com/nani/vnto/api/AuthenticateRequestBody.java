package com.nani.vnto.api;

import org.json.JSONObject;

/**
 * Created by nataliajastrzebska on 18/04/16.
 */
public class AuthenticateRequestBody {
    String email;
    String password;

    public AuthenticateRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public JSONObject getBody() {
        JSONObject body = new JSONObject();

        try {
            body.put("email", this.email);
            body.put("password", this.password);
        } catch (Exception e) {

        }

        return body;
    }
}
