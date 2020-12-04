package com.example.classwork7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseRegistrar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button signout, display;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        signout = findViewById(R.id.signout);
        display = findViewById(R.id.display_Data);
        signout.setOnClickListener(v -> {
            if (auth != null) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finishAffinity();
                finish();

            }
        });

        display.setOnClickListener(v -> Fetch_Data());
    }

    private void Fetch_Data() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("PersonTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String Name = snapshot.child("name").getValue().toString();
                            String Email = snapshot.child("email").getValue().toString();
                            String Password = snapshot.child("password").getValue().toString();
                            String Gender = snapshot.child("gender").getValue().toString();
                            String Phone = snapshot.child("phone").getValue().toString();
                            String ImgUrl = snapshot.child("imageURL").getValue().toString();

                            Toast.makeText(MainActivity.this, Name+"\n"+Email+"\n"+
                                    Password+"\n"+
                                    Gender+"\n"+
                                    Phone+"\n"+
                                    ImgUrl+"\n", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
