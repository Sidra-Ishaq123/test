package com.example.classwork7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.classwork7.Adapter.PersonAdapter;
import com.example.classwork7.PersonModel.PersonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayData extends AppCompatActivity {
    
    PersonAdapter adapter;
    ArrayList<PersonModel> list;
    RecyclerView recyclerView;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        
        recyclerView = findViewById(R.id.myRecyclerView);
        manager = new LinearLayoutManager(this);
        list = new ArrayList<>();
        
        recyclerView.setLayoutManager(manager);
        
        FetchData();
    }

    private void FetchData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("PersonTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            String Name = snapshot.child("name").getValue().toString();
                            String Email = snapshot.child("email").getValue().toString();
                            String Password = snapshot.child("password").getValue().toString();
                            String Gender = snapshot.child("gender").getValue().toString();
                            String Phone = snapshot.child("phone").getValue().toString();
                            String ImgUrl = snapshot.child("imageURL").getValue().toString();

                            list.add(new PersonModel(Name,Email,Password,Phone,Gender,ImgUrl));
                        }

                        adapter = new PersonAdapter(DisplayData.this,list);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DisplayData.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}