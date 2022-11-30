package com.husamali.khatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    Button next;
    EditText number;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        next = findViewById(R.id.next);
        number = findViewById(R.id.numberSignIn);
        mAuth = FirebaseAuth.getInstance();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText()!=null) {

                    Intent intent = new Intent(getApplicationContext(), Otp.class);
                    intent.putExtra("number", number.getText().toString());
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        }

    }
}