package com.example.chitchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnSubmit;
    private TextView txtLoginInfo;

    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide actionbar


        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        //check if user already signin or not
       if ( FirebaseAuth.getInstance().getCurrentUser() != null ){
           startActivity(new Intent(MainActivity.this, FriendsActivity.class));
           finish();
       }


        //check if user already signin or not

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if all input field are filled correctly
                if (edtEmail.getText().toString().isEmpty() || edtPassword
                        .getText().toString().isEmpty()){
                 if (isSigningUp && edtUsername.getText().toString().isEmpty()){
                     Toast.makeText(MainActivity.this, "All Field must Filled correctly", Toast.LENGTH_SHORT).show();
                     return; // jump out of function
                 }else {
                     Toast.makeText(MainActivity.this, "All Field must Filled correctly", Toast.LENGTH_SHORT).show();
                     return;
                 }
                }



                //check if all input field are filled correctly


                if (isSigningUp) {
                    handleSignUp();
                } else {
                    handleLogin();
                }
            }
        });


        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSigningUp) {
                    isSigningUp = false;
                    btnSubmit.setText("SignIn");
                    txtLoginInfo.setText("Dont have An Account? Register Here");
                    edtUsername.setVisibility(View.GONE);
                } else {
                    isSigningUp = true;
                    edtUsername.setVisibility(View.VISIBLE);
                    btnSubmit.setText("SignUp");
                    txtLoginInfo.setText("Already have an account? Login Here");
                }
            }
        });


    }

    private void handleSignUp() {
        //call createuser function to create signup form
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword
        .getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //if acc created successfully
                    FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser()
                    .getUid()).setValue(new User(edtUsername
                    .getText().toString(), edtEmail.getText().toString(), ""));
                    startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }else {
                    //if failed to create account
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword
                .getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //if login successfully
                    startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }else {
                    //if failed to login
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}