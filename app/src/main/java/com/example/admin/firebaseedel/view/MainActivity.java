package com.example.admin.firebaseedel.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.firebaseedel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";

    private EditText etPassword;
    private EditText etEmail;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private StorageReference mStorageRef;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();

        //VERIFY IF USER IS SIGNED IN
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }



    public void usingFirebase(View view) {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        switch (view.getId()){

            case R.id.btnSignIn:

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(MainActivity.this, "Failed to sign in",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "Signed in successfully",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                    startActivity(intent);

                                }

                            }
                        });
                break;

            case R.id.btnSignUp:

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Fail to sign up",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "Sign up successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
        }
    }

    private static final int GALLERY_INTENT = 2;
    public void sendPictureToStorage(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent, GALLERY_INTENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();

            StorageReference filepath = mStorageRef.child("Photos").child(uri.getLastPathSegment());
            //StorageReference filepath = mStorageRef.child("Edel").child(uri.getPath());


            filepath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Upload Unsuccessful", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure: "+ e);
                }
            });
        }
    }

    EditText adEditTextAmount;
    EditText adEditTextDescription;
    public void requestAdvance(View view) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.advance_request_layout, null);

        adEditTextAmount = mView.findViewById(R.id.adEditTextAmount);
        adEditTextDescription = mView.findViewById(R.id.adEditTextDescription);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendAdvance(View view) {
        if(Objects.equals(adEditTextAmount.getText().toString(), "")
                && Objects.equals(adEditTextDescription.getText().toString(), "")){
            Toast.makeText(this, "Empty Fields", Toast.LENGTH_SHORT).show();
        }else{
            float requestedAmountAdvance = Float.valueOf(adEditTextAmount.getText().toString());
            Toast.makeText(this, "Amount: " + adEditTextAmount.getText().toString(), Toast.LENGTH_SHORT).show();
            String advanceDescription = adEditTextDescription.getText().toString();
        }
    }
    public void cancelAlertDialog(View view) {
        dialog.dismiss();
    }


}
