package com.nani.vnto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import com.nani.vnto.api.Service;
import com.nani.vnto.api.ServiceGenerator;
import com.nani.vnto.model.Contact;
import com.nani.vnto.model.Photo;

import java.util.ArrayList;
import java.util.List;

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
public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView contactsRecyclerView;
    @Bind(R.id.contactsCoordinatorLayout)
    CoordinatorLayout contactsCoordinatorLayout;

    RecyclerViewAdapter recyclerViewAdapter;
    List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        createLayoutManager();
        createAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contacts.clear();
        apiGetContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.addContactButton)
    public void onAddContactButtonClicked() {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ContactActivity.KEY_EXTRA_NEW, true);

        startActivity(intent);
    }

    private void createLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(manager);
    }

    private void createAdapter() {
        this.recyclerViewAdapter = new RecyclerViewAdapter(this, this.contacts, this);
        contactsRecyclerView.setAdapter(this.recyclerViewAdapter);
    }

    private void showSnackbar(String message){
        Snackbar.make(contactsCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void receivedContactsList(List<Contact> contacts) {
        this.contacts.addAll(contacts);
        this.recyclerViewAdapter.notifyDataSetChanged();

        for (Contact contact: contacts) {
            apiGetContactImage(contact);
        }
    }

    public void apiGetContacts(){
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, this);

        Call<List<Contact>> account = client.getContacts();
        account.enqueue(new Callback<List<Contact>>() {

            @Override
            public void onResponse(Response<List<Contact>> response, Retrofit retrofit) {

                if (response.errorBody() != null) {
                    showSnackbar(response.message());

                    return;
                }

                receivedContactsList(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                showSnackbar(getString(R.string.error));
            }
        });
    }

    public void apiGetContactImage(final Contact contact) {
        Service.ServiceInterface client = ServiceGenerator.createService(Service.ServiceInterface.class, this);

        Call<Photo> account = client.getPhoto(contact.getId());
        account.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Response<Photo> response, Retrofit retrofit) {

                if (response.errorBody() != null) {
                    //showSnackbar(response.message());

                    return;
                }

                contact.setImageBytes(Base64.decode(response.body().getContent(), Base64.DEFAULT));
            }

            @Override
            public void onFailure(Throwable t) {
                showSnackbar(getString(R.string.error));
            }

        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ContactActivity.KEY_EXTRA_NEW, false);
        intent.putExtra(ContactActivity.KEY_EXTRA_CONTACT, contacts.get(position).getId());

        startActivity(intent);
    }
}
