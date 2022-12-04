package com.husamali.khatt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpComplete extends AppCompatActivity {

    ImageView dp;
    EditText name;
    Button finish;

    static int PICK_IMAGE;
    private Uri imagePath;

    FirebaseAuth firebaseAuth;
    String str_name;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String imageURIAccessToken;

    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_complete);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        dp = findViewById(R.id.dp);
        finish = findViewById(R.id.finish);

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }


        });



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_name = name.getText().toString();
                if (str_name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_LONG).show();
                }
                else if(imagePath==null){
                    Toast.makeText(getApplicationContext(), "Please Select Profile Picture", Toast.LENGTH_LONG).show();
                }
                else{
                    sendDataForNewUser();
                    Intent intent=new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                }


            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode==RESULT_OK){
            imagePath = data.getData();
            dp.setImageURI(imagePath);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendDataForNewUser() {
        sendDataToRealTimeDatabase();
    }

    private void sendDataToRealTimeDatabase() {
        str_name = name.getText().toString().trim();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile mUserProfile = new UserProfile(str_name, firebaseAuth.getUid());
        databaseReference.setValue(mUserProfile);
        Toast.makeText(getApplicationContext(), "User Profile Added", Toast.LENGTH_LONG).show();
        sendImageToStorage();

    }

    private void sendImageToStorage() {
        StorageReference imagesRef = storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Picture");

        //Image Compression
        Bitmap bitmap = null;
        try {
            bitmap  = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte [] data = byteArrayOutputStream.toByteArray();

        //string image to storage
        UploadTask uploadTask = imagesRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURIAccessToken = uri.toString();
                        Toast.makeText(getApplicationContext(), "URI successful", Toast.LENGTH_LONG).show();
                        sendDataToCloudFirestore();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "URI failed", Toast.LENGTH_LONG).show();
                    }
                });

                Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendDataToCloudFirestore() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", str_name);
        userData.put("image", imageURIAccessToken);
        userData.put("uid", firebaseAuth.getUid());
        userData.put("status", "Online");

        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Data added on Cloud Firestore", Toast.LENGTH_LONG).show();
            }
        });
    }
}

