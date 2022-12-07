package com.husamali.khatt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpStage2 extends AppCompatActivity {
    EditText name;
    Button finish;
    FirebaseAuth firebaseAuth;
    String str_name;
    ImageView dp;
    byte[] bytes;
    Bitmap bitmap;
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

            if (name.getText() == null){
                Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_LONG).show();
            }
            else if(dpp == null) {
                Toast.makeText(getApplicationContext(), "Please Select Profile Picture", Toast.LENGTH_LONG).show();
            }
            else{
                str_name = name.getText().toString().trim();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                Calendar calendar = Calendar.getInstance();
                StorageReference storageReference = storage.getReference("Image1"+calendar.getTimeInMillis()+".jpg");
                storageReference.putFile(dpp).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
                    UserProfile mUserProfile = new UserProfile(str_name, firebaseAuth.getCurrentUser().getUid(),uri.toString());
                    databaseReference.setValue(mUserProfile);
                    Intent intent=new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                }));
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), dpp);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    bytes = stream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringRequest request=new StringRequest(
                        Request.Method.POST,
                        "http://192.168.0.109/project/insert.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj=new JSONObject(response);
                                    if(obj.getInt("code")==1)
                                    {
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(
                                                getApplicationContext(),
                                                obj.get("msg").toString()
                                                ,Toast.LENGTH_LONG
                                        ).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Incorrect JSON "+e
                                            ,Toast.LENGTH_LONG
                                    ).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Cannot Connect to the Server."+"\n"+error.toString()
                                        ,Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                {
                    //                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params=new HashMap<>();
                        params.put("name",name.getText().toString());
                        params.put("id",firebaseAuth.getCurrentUser().getUid());
                        params.put("dp", Base64.getEncoder().encodeToString(bytes));
                        params.put("number", getIntent().getStringExtra("number"));
                        return params;
                    }
                };
                RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                queue.add(request);

            }
        });
    }
}