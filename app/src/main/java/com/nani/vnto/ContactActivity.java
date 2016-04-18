package com.nani.vnto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.nani.vnto.api.Service;
import com.nani.vnto.api.ServiceGenerator;
import com.nani.vnto.imageUtils.ImageWriter;
import com.nani.vnto.model.Contact;
import com.nani.vnto.model.Photo;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;

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
public class ContactActivity extends AppCompatActivity {

    @Bind(R.id.addContactLinearLayout)
    LinearLayout createContactLinearLayout;
    @Bind(R.id.editButtonsLinearLayout)
    LinearLayout editButtonsLinearLayout;

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
    @Bind(R.id.addPhotoButton)
    Button addPhotoButton;
    @Bind(R.id.photoImageView)
    ImageView photoImageView;
    @Bind(R.id.imageLinearLayout)
    LinearLayout imageLinearLayout;

    public static String KEY_EXTRA_NEW = "key_new";
    public static String KEY_EXTRA_CONTACT = "key_contact";
    public static int REQUEST_PHOTO = 101;
    int contactId;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        if (shouldDisplayExistingContact()) {
            addContactButton.setVisibility(View.GONE);

            this.contactId = getIntent().getExtras().getInt(KEY_EXTRA_CONTACT);
            apiGetContact(contactId);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PHOTO) {

            if (resultCode == Activity.RESULT_OK) {
                chosenPhoto();

                return;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private boolean shouldDisplayExistingContact() {
        return !getIntent().getExtras().getBoolean(KEY_EXTRA_NEW);
    }

    private void displayExistingContact(Contact contact) {
        this.firstNameEditText.setText(contact.getFirstName());
        this.lastNameEditText.setText(contact.getLastName());
        this.companyNameEditText.setText(contact.getCompanyName());
        this.emailEditText.setText(contact.getEmail());
        this.phoneEditText.setText(contact.getPhoneNumber());

        this.editButtonsLinearLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.addContactButton)
    void onAddContactClicked() {
        validateForm();
    }

    private void validateForm() {

        if (!TextUtils.isEmpty(this.firstNameEditText.getText()) && !TextUtils.isEmpty(this.lastNameEditText.getText()) && Validator.isEmailValid(this.emailEditText.getText())){
            addContactButton.setEnabled(false);
            apiAddContact(getContactFromForm());

            return;
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

    private com.nani.vnto.model.Contact getContactFromForm(){
        Contact contact = new com.nani.vnto.model.Contact();
        contact.setFirstName(this.firstNameEditText.getText().toString());
        contact.setLastName(this.lastNameEditText.getText().toString());
        contact.setEmail(this.emailEditText.getText().toString());

        if (!TextUtils.isEmpty(this.companyNameEditText.getText())) {
            contact.setCompanyName(this.companyNameEditText.getText().toString());
        }

        if (!TextUtils.isEmpty(this.phoneEditText.getText())) {
            contact.setPhoneNumber(this.phoneEditText.getText().toString());
        }
        contact.setId(this.contactId);

        return contact;
    }

    private void showSnackbar(String message){
       Snackbar.make(createContactLinearLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.removeContactButton)
    void onRemoveButtonClicked(){
        apiDeleteContact(this.contactId);
    }

    @OnClick(R.id.editContactButton)
    void onEditButtonClicked() {
        apiEditContact(this.contactId);
    }

    @OnClick(R.id.addPhotoButton)
    void onAddPhotoButtonClicked() {
        startCamera();
    }

    private void startCamera() {
        this.imageFile = ImageWriter.getOutputMediaFile();
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(this.imageFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(cameraIntent, REQUEST_PHOTO);
    }

    private void chosenPhoto() {
        this.addPhotoButton.setVisibility(View.GONE);
        this.imageLinearLayout.setVisibility(View.VISIBLE);
        Glide.with(this).load(this.imageFile).centerCrop().into(this.photoImageView);
    }

    @OnClick(R.id.sendPhotoButton)
    void onSendPhotoButtonClicked() {
        apiSendPhoto(this.contactId);
    }

    public void apiAddContact(com.nani.vnto.model.Contact contact){
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, ContactActivity.this);

        Call<Contact> account = client.createContact(contact);
        account.enqueue(new Callback<Contact>() {

            @Override
            public void onResponse(Response<Contact> response, Retrofit retrofit) {

                if (response.errorBody() != null) {
                    showSnackbar(response.message());

                    return;
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

    public void apiGetContact(int contactId) {
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, this);

        Call<com.nani.vnto.model.Contact> contact = client.getContact(contactId);
        contact.enqueue(new Callback<com.nani.vnto.model.Contact>() {
            @Override
            public void onResponse(Response<com.nani.vnto.model.Contact> response, Retrofit retrofit) {

                if (response.errorBody() != null) {
                    showSnackbar(response.message());

                    return;
                }

                displayExistingContact(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                showSnackbar(getString(R.string.error));
            }

        });
    }

    public void apiDeleteContact(int contactId) {
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, this);

        Call<Contact> contactCall = client.deleteContact(contactId);
        contactCall.enqueue(new Callback<com.nani.vnto.model.Contact>() {
            @Override
            public void onResponse(Response<com.nani.vnto.model.Contact> response, Retrofit retrofit) {

                if (response.errorBody() != null) {
                    showSnackbar(response.message());

                    return;
                }

                showSnackbar(getString(R.string.contact_deleted));
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                showSnackbar(getString(R.string.error));
            }

        });
    }

    public void apiEditContact(int contactId) {
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, this);

        Call<Contact> contactCall = client.editContact(contactId, getContactFromForm());
        contactCall.enqueue(new Callback<com.nani.vnto.model.Contact>() {
            @Override
            public void onResponse(Response<com.nani.vnto.model.Contact> response, Retrofit retrofit) {

                if (response.errorBody() != null) {
                    showSnackbar(response.message());

                    return;
                }

                showSnackbar(getString(R.string.contact_edited));
            }

            @Override
            public void onFailure(Throwable t) {
                showSnackbar(getString(R.string.error));
            }

        });
    }

    public void apiSendPhoto(int contactId) {
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, this);

        RequestBody photo = RequestBody.create(MediaType.parse("image/jpeg"), this.imageFile);
        RequestBody body = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("file", this.imageFile.getName(), photo)
                .build();

        Call<Photo> contactCall = client.setPhoto(contactId, body);
        contactCall.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Response<Photo> response, Retrofit retrofit) {

                if (response.errorBody() != null) {
                    showSnackbar(response.message());

                    return;
                }

                showSnackbar(getString(R.string.contact_edited));
            }

            @Override
            public void onFailure(Throwable t) {
                showSnackbar(getString(R.string.error));
            }

        });
    }
}
