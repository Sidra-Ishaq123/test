package com.example.classwork7;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.example.classwork7.PersonModel.PersonModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    Button choose_button;
    TextView gotoLogin;
    EditText user, email, password, confirmpass, phone;
    Button Register;
    RadioGroup gender;
    RadioButton genderButton;

    Uri fileData = null;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    //private Object LoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        choose_button = findViewById(R.id.reg_image);
        Register = findViewById(R.id.reg_button);
        user = findViewById(R.id.reg_username);
        email = findViewById(R.id.reg_Email);
        password = findViewById(R.id.reg_Password);
        confirmpass = findViewById(R.id.regcon_Password);
        phone = findViewById(R.id.reg_Phone);
        gender = findViewById(R.id.gender_group);
        gotoLogin = findViewById(R.id.goto_logIn);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            finish();
        });
        Register.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            CheckAllValidation();
        });

        choose_button.setOnClickListener(v -> Choose_Image());
    }

    private void Choose_Image() {
        Intent ImagePick = new Intent();
        ImagePick.setType("image/*");
        ImagePick.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(ImagePick, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            fileData = data.getData();
        }
    }

    private void CheckAllValidation() {
        String userName = user.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPass = password.getText().toString().trim();
        String userCon = confirmpass.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            user.setError("Please Enter UserName Here");
        } else if (TextUtils.isEmpty(userEmail)) {
            email.setError("Please Enter Email Here");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Please Valid Email");
        } else if (TextUtils.isEmpty(userPass)) {
            password.setError("Please Enter Password Here");
        } else if (TextUtils.isEmpty(userCon)) {
            confirmpass.setError("Please Re-Enter Password");
        } else if (!(userPass.equals(userCon))) {
            Toast.makeText(RegistrationActivity.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userPhone)) {
            phone.setError("Please Enter Phone Here");
        } else if (fileData == null) {
            Toast.makeText(RegistrationActivity.this, "Please Choose an Image", Toast.LENGTH_SHORT).show();
        } else {
            int selectedID = gender.getCheckedRadioButtonId();
            genderButton = findViewById(selectedID);
            String userGender = genderButton.getText().toString();

            InsertInDataBase(userName, userEmail, userPass, userCon, userPhone, userGender, fileData);
        }
    }

    private void InsertInDataBase(final String userName, final String userEmail, final String userPass, final String userCon, final String userPhone, final String userGender, final Uri fileData) {
        progressDialog.setMessage("please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        auth.createUserWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        SendImageInStorage(userName, userEmail, userPass, userCon, userPhone, userGender, fileData);
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this, "Authentication Not Completed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void SendImageInStorage(final String userName, final String userEmail, final String userPass, final String userCon, final String userPhone, final String userGender, Uri fileData) {
        final StorageReference ref = FirebaseStorage.getInstance().getReference("PersonImages/"+ Objects.requireNonNull(auth.getCurrentUser()).getUid());
        ref.putFile(fileData).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri DownloadedURL = task.getResult();
                InsertInRealTimeDataBase(userName, userEmail, userPass, userCon, userPhone, userGender, DownloadedURL);
            } else {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this, "Url Not Generated", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void InsertInRealTimeDataBase(String userName, String userEmail, String userPass, String userCon, String userPhone, String userGender, @NotNull Uri downloadedURL) {
        PersonModel values = new PersonModel(userName,userEmail,userPass,userPhone,userGender,downloadedURL.toString());

        FirebaseDatabase.getInstance().getReference("PersonTable").child(auth.getCurrentUser().getUid()).setValue(values)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "User Not Created", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}