package com.husamali.khatt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ManageProfile extends AppCompatActivity {
    EditText name;
    ImageView dp;
    Button save;
    byte[] bytes;
    Bitmap bitmap;
    Uri dpp;
    FirebaseAuth firebaseAuth;
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
        setContentView(R.layout.activity_manage_profile);
        name = findViewById(R.id.nameManage);
        dp = findViewById(R.id.dpManage);
        firebaseAuth = FirebaseAuth.getInstance();
        save = findViewById(R.id.save);

//        getData();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("username").getValue(String.class));
                Picasso.get().load(snapshot.child("dp").getValue(String.class)).into(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dp.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startForResult.launch(Intent.createChooser(i,"Pick your DP"));
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), dpp);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,60,stream);
                    bytes = stream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FirebaseStorage storage = FirebaseStorage.getInstance();
                Calendar calendar = Calendar.getInstance();
                StorageReference storageReference = storage.getReference("Image1"+calendar.getTimeInMillis()+".jpg");
                storageReference.putFile(dpp).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
                    databaseReference.child("username").setValue(name.getText().toString());
                    databaseReference.child("dp").setValue(uri.toString());

                }));
                String dpdp = Base64.getEncoder().encodeToString(bytes);
                StringRequest request=new StringRequest(
                        Request.Method.POST,
                        "http://192.168.0.109/project/update.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("response = ",response);
                                    Toast.makeText(
                                            getApplicationContext(),
                                            response
                                            ,Toast.LENGTH_LONG
                                    ).show();
                                    JSONObject obj=new JSONObject(response);
                                    if(obj.getInt("code")==1)
                                    {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                obj.get("msg").toString()
                                                ,Toast.LENGTH_LONG
                                        ).show();
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
                        params.put("dp",dpdp);

                        return params;
                    }
                };
                RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
                finish();
            }
        });

    }

    private void getData() {
        StringRequest request=new StringRequest(
                Request.Method.POST,
                "http://192.168.0.109/project/getdp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj=new JSONObject(response);
                            if(obj.getString("code").equals("1"))
                            {
                                JSONArray contacts=obj.getJSONArray("dp");
                                for (int i=0; i<contacts.length();i++) {
                                    JSONObject contact = contacts.getJSONObject(i);
                                    byte[] z = Base64.getDecoder().decode(contact.getString("dp"));
                                    name.setText(contact.getString("name"));
                                    dp.setImageBitmap(BitmapFactory.decodeByteArray(z, 0, z.length));
                                }
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
                params.put("id",firebaseAuth.getCurrentUser().getUid());
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }
}