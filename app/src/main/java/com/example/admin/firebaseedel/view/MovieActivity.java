package com.example.admin.firebaseedel.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.firebaseedel.R;
import com.example.admin.firebaseedel.model.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MovieActivity extends AppCompatActivity {

    private EditText etMovieName;
    private EditText etMovieYear;
    private EditText etMovieDirector;

    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;

    private static final String TAG = "MovieActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        etMovieName = (EditText) findViewById(R.id.etMovieName);
        etMovieYear = (EditText) findViewById(R.id.etMovieYear);
        etMovieDirector = (EditText) findViewById(R.id.etMovieDirector);

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
        //GET DATA FROM FIREBASE

        myRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean hasChildren = dataSnapshot
                        .child(uid)
                        .child("movies")
                        .hasChildren();

                if (hasChildren) {
                    for (DataSnapshot snapshot : dataSnapshot
                            .child(uid)
                            .child("movies")
                            .getChildren()) {
                        Movie movie = snapshot.getValue(Movie.class);
                        Log.d("TAG", "OnDataChange: " + uid + " " + movie.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void addMovie(View view) {


        String movieName = etMovieName.getText().toString();
        String movieYear = etMovieYear.getText().toString();
        String movieDirector = etMovieDirector.getText().toString();

        Movie movie = new Movie(movieName, movieDirector, movieYear);

        myRefUsers.child(uid)
                .child("movies")
                .push()
                .setValue(movie)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MovieActivity.this, "Movie Was Added", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MovieActivity.this, "Movie Was Not Added", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    public void signOut(View view) {

        FirebaseAuth.getInstance().signOut();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
        }
    }

    public void getMovies(View view) {


    }
}
