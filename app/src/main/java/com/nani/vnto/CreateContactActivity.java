package com.nani.vnto;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nani.vnto.api.Service;
import com.nani.vnto.api.ServiceGenerator;
import com.nani.vnto.model.Contact;
import com.nani.vnto.responses.AccountResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nataliajastrzebska on 18/04/16.
 */
public class CreateContactActivity extends AppCompatActivity {

    @Bind(R.id.addContactLinearLayout)
    LinearLayout createContactLinearLayout;

    @Bind(R.id.firstNameEditText)
    EditText firstNameEditText;
    @Bind(R.id.lastNameEditText)
    EditText lastNameEditText;
    @Bind(R.id.companyNameEditText)
    EditText companyNameEditText;
    @Bind(R.id.emailEditText)
    EditText emailEditText;
    @Bind(R.id.phoneEditText)
    EditText phoneEditText;

    @Bind(R.id.addContactButton)
    Button addContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.addContactButton)
    void onAddContactClicked() {
        addContactButton.setEnabled(false);
        validateForm();
    }

    private void validateForm() {

        if (!TextUtils.isEmpty(this.firstNameEditText.getText()) && !TextUtils.isEmpty(this.lastNameEditText.getText()) && Validator.isEmailValid(this.emailEditText.getText())){
            apiAddContact(getContactFromForm());
        }

        showSnackbar(getString(R.string.form_not_filled_properly));
    }

    private void clearForm() {
        this.firstNameEditText.setText("");
        this.lastNameEditText.setText("");
        this.companyNameEditText.setText("");
        this.emailEditText.setText("");
        this.phoneEditText.setText("");
        this.addContactButton.setEnabled(true);
    }

    private Contact getContactFromForm(){
        Contact contact = new Contact();
        contact.setFirstName(this.firstNameEditText.getText().toString());
        contact.setLastName(this.lastNameEditText.getText().toString());
        contact.setEmail(this.emailEditText.getText().toString());

        if (!TextUtils.isEmpty(this.companyNameEditText.getText())) {
            contact.setCompanyName(this.companyNameEditText.getText().toString());
        }

        if (!TextUtils.isEmpty(this.phoneEditText.getText())) {
            contact.setPhoneNumber(this.phoneEditText.getText().toString());
        }

        return contact;
    }

    private void showSnackbar(String message){
       Snackbar.make(createContactLinearLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public void apiAddContact(Contact contact){
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, CreateContactActivity.this);

        Call<AccountResponse> account = client.createContact(contact);
        account.enqueue(new Callback<AccountResponse>() {

            @Override
            public void onResponse(Response<AccountResponse> response, Retrofit retrofit) {

                if (response.errorBody()!= null) {
                    showSnackbar(response.message());
                }
                showSnackbar(getString(R.string.contact_created));
                clearForm();
            }

            @Override
            public void onFailure(Throwable t) {
                showSnackbar(getString(R.string.error));
            }
        });
    }
}
