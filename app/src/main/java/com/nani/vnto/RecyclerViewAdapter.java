package com.nani.vnto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nani.vnto.model.Contact;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nataliajastrzebska on 11/04/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Contact> contacts;
    OnContactClickListener onContactClickListener;

    public RecyclerViewAdapter(Context context, List<Contact> contacts, OnContactClickListener onContactClickListener) {
        this.context = context;
        this.contacts = contacts;
        this.onContactClickListener = onContactClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);

        return new ViewHolder(itemView, onContactClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.firstNameTextView.setText(contact.getFirstName());
        holder.lastNameTextView.setText(contact.getLastName());
        setCompany(holder, contact);
        setImage(holder, contact);
    }

    private void setCompany(ViewHolder holder, Contact contact) {

        if (TextUtils.isEmpty(contact.getCompanyName())) {
            holder.companyNameTextView.setVisibility(View.GONE);
        } else {
            holder.companyNameTextView.setText(contact.getCompanyName());
        }
    }

    private void setImage(ViewHolder holder, Contact contact) {

        if (contact.getImageBytes() != null) {
            Glide.with(this.context).load(contact.getImageBytes()).centerCrop().into(holder.contactImageView);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.contactFirstName)
        TextView firstNameTextView;
        @Bind(R.id.contactLastName)
        TextView lastNameTextView;
        @Bind(R.id.companyName)
        TextView companyNameTextView;
        @Bind(R.id.contactImageView)
        ImageView contactImageView;
        OnContactClickListener onContactClickListener;

        public ViewHolder(View itemView, OnContactClickListener onContactClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onContactClickListener = onContactClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onContactClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnContactClickListener {
        void onItemClick(int position);
    }
}
