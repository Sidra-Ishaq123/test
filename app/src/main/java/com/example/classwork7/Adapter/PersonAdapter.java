package com.example.classwork7.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classwork7.DisplayData;
import com.example.classwork7.PersonModel.PersonModel;
import com.example.classwork7.R;
import com.google.firebase.database.core.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    private DisplayData ctx;
    private ArrayList<PersonModel> list;

    public PersonAdapter(DisplayData ctx, ArrayList<PersonModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public @NotNull PersonViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_design,parent,false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        PersonModel model = list.get(position);

        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.phone.setText(model.getPhone());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView name,email,phone;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.personName);
            email = itemView.findViewById(R.id.personEmail);
            phone = itemView.findViewById(R.id.personPhone);

        }
    }
}
