package com.example.admin.firebaseedel.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.admin.firebaseedel.R;
import com.example.admin.firebaseedel.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    EditText etFirstName;
    EditText etLastName;
    EditText etBirthday;
    EditText etAddress;
    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etBirthday = (EditText) findViewById(R.id.etBirthday);
        etAddress = (EditText) findViewById(R.id.etAddress);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference("users");


    }

    public void saveProfile(View view){

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String birthday = etBirthday.getText().toString();
        String address = etAddress.getText().toString();

        UserProfile userProfile = new UserProfile(firstName, lastName, birthday, address);

        myRefUsers.child(uid).setValue(userProfile);
    }

    public void addMovies(View view) {


        Intent intent = new Intent(UserProfileActivity.this, MovieActivity.class);
        startActivity(intent);

    }
}
