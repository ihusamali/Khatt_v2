package com.husamali.khatt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class SignUpStage2 extends AppCompatActivity {
    EditText name;
    Button finish;
    FirebaseAuth firebaseAuth;
    String str_name;
    ImageView dp;
    Uri dpp;
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode()== RESULT_OK){
                if(result.getData()!=null) {
                    dpp = result.getData().getData();
                    dp.setImageURI(dpp);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_stage2);
        dp = findViewById(R.id.dpSignUp);
        firebaseAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nameSignUp);
        finish = findViewById(R.id.finishSignUp);
        dp.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startForResult.launch(Intent.createChooser(i,"Pick your DP"));
        });
        finish.setOnClickListener(v -> {
            if(dpp == null){
                Toast.makeText(getApplicationContext(), "Please Select Profile Picture", Toast.LENGTH_LONG).show();
            }else if (name.getText() == null){
                Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_LONG).show();
            }
            else{
                str_name = name.getText().toString().trim();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                Calendar calendar = Calendar.getInstance();
                StorageReference storageReference = storage.getReference("Image1"+calendar.getTimeInMillis()+".jpg");
                storageReference.putFile(dpp).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
                    Toast.makeText(getApplicationContext(),databaseReference.toString(),Toast.LENGTH_LONG).show();
                    UserProfile mUserProfile = new UserProfile(str_name, firebaseAuth.getCurrentUser().getUid(),uri.toString());
                    databaseReference.setValue(mUserProfile);
                    Intent intent=new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                }));


            }
        });
    }
}