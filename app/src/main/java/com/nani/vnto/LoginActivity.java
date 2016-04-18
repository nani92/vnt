package com.nani.vnto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.nani.vnto.api.AuthenticateRequestBody;
import com.nani.vnto.api.Service;
import com.nani.vnto.api.ServiceGenerator;
import com.nani.vnto.model.Authentication;
import com.nani.vnto.responses.AccountResponse;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.emailEditText)
    EditText emailEditText;
    @Bind(R.id.passwordEditText)
    EditText passwordEditText;
    @Bind(R.id.loginButton)
    Button loginButton;

    private int minPasswordLength = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnTextChanged(R.id.passwordEditText)
    void onPasswordChanged() {
        validateForm();
    }

    @OnTextChanged(R.id.emailEditText)
    void onEmailChanged() {
        validateForm();
    }

    private void validateForm() {
        if (isEmailValid() && isPasswordValid()) {
            loginButton.setEnabled(true);
        }
    }

    private Boolean isEmailValid() {
        return Validator.isEmailValid(this.emailEditText.getText());
    }

    private Boolean isPasswordValid() {
        return !TextUtils.isEmpty(passwordEditText.getText()) && passwordEditText.getText().length() > minPasswordLength;
    }

    @OnClick(R.id.loginButton)
    void onLoginButtonClicked() {
        loginButton.setEnabled(false);

        apiLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    public void apiLogin( String email, String password){
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, LoginActivity.this);
        AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody(email, password);

        Call<Authentication> account = client.authenticate(RequestBody.create(MediaType.parse("text/plain"), authenticateRequestBody.getBody().toString()));
        account.enqueue(new Callback<Authentication>() {

            @Override
            public void onResponse(Response<Authentication> response, Retrofit retrofit) {
                saveToken(response.body().getValue());
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Throwable t) {
                LoginActivity.this.loginButton.setEnabled(true);
            }
        });
    }

    public void saveToken(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString(Service.KEY_TOKEN, token).apply();
    }
}
