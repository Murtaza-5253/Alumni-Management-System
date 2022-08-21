package com.igc.studentalumni;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Ad_Student_Verify extends AppCompatActivity
{
    String[] Status = {"Unverified","Verified","Blocked"};
    Uri bundleuri,uri;
    boolean image,yes;
    AutoCompleteTextView spnVStatus;
    CircularImageView imgVProfile;
    TextInputEditText txtVPNR,txtVName,txtVBatch,txtVCollege,txtVDept,txtVBranch,txtVMobNo,txtVEmail,txtVUName,txtVPass;
    String SProfile,PNR,Name,MobNo,Email,Branch,Batch,Dept,College,UName,Password;
    RequestQueue rq;
    int imgid;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_student__verify);
        init();
        try {
            yes=true;

            Bundle b = getIntent().getExtras();
            SProfile = b.getString("SProfile");
            PNR = b.getString("PNR");
            Name = (String) b.getString("Name");
            MobNo = b.getString("MobNo");
            Email = b.getString("Email");
            Branch = b.getString("Branch");
            Batch = b.getString("Batch");
            Dept = b.getString("Dept");
            College = b.getString("College");
            UName = b.getString("UName");
            Password = b.getString("Password");
            Toast.makeText(this, SProfile, Toast.LENGTH_SHORT).show();

            FirebaseStorage.getInstance().getReference().child(SProfile).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            bundleuri=uri;
                            Toast.makeText(Ad_Student_Verify.this, bundleuri+"", Toast.LENGTH_SHORT).show();
                            Glide.with(Ad_Student_Verify.this).load(uri).into(imgVProfile);
                            image=true;
                        }
                    });

            txtVPNR.setText(PNR);
            txtVName.setText(Name);
            txtVMobNo.setText(MobNo);
            txtVEmail.setText(Email);
            txtVBranch.setText(Branch);
            txtVBatch.setText(Batch);
            txtVDept.setText(Dept);
            txtVCollege.setText(College);
            txtVUName.setText(UName);
            txtVPass.setText(Password);

        }catch (Exception e)
        {

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Status);
        spnVStatus.setAdapter(adapter);
    }
    private void init()
    {
        imgVProfile = findViewById(R.id.imgVProfile);
        txtVPNR= findViewById(R.id.txtVPNR);
        txtVName = findViewById(R.id.txtVName);
        txtVBatch = findViewById(R.id.txtVBatch);
        txtVBranch = findViewById(R.id.txtVBranch);
        txtVCollege = findViewById(R.id.txtVCollege);
        txtVDept = findViewById(R.id.txtVDepartment);
        txtVMobNo = findViewById(R.id.txtVMobNo);
        txtVEmail = findViewById(R.id.txtVEmail);
        spnVStatus = findViewById(R.id.spnVStatus);
        txtVUName = findViewById(R.id.txtVUName);
        txtVPass = findViewById(R.id.txtVPassword);
        rq = Volley.newRequestQueue(this);
//        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseApp.initializeApp(this);
        FirebaseStorage fb = FirebaseStorage.getInstance();
        storageReference = fb.getReference();
    }

    public void profile(View view)
    {
        if(ContextCompat.checkSelfPermission(Ad_Student_Verify.this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED && (ContextCompat.checkSelfPermission(Ad_Student_Verify.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED))
        {
            ActivityCompat.requestPermissions(Ad_Student_Verify.this,new String[] {Manifest.permission.INTERNET, Manifest.permission.CAMERA},101);
        }
        else
        {
            if(!yes){
                ImagePicker.with(this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);

            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && data!=null)
        {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
//            uri = getImageUri(getApplicationContext(),bmp);
            uri = data.getData();
            Glide.with(this).load(uri).into(imgVProfile);

            if (uri!=null)
            {
                SharedPreferences sp = getSharedPreferences("StudentInfo",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("uri", String.valueOf(uri));
                edit.apply();
            }
            Toast.makeText(this, uri+"", Toast.LENGTH_SHORT).show();




//            imgVProfile.setImageBitmap(bmp);
            image=true;
        }
    }
    private Uri getImageUri(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        String Path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bitmap,txtVUName.getText().toString(),null);
        return Uri.parse(Path);

    }

    public void update(View view)
    {
        SharedPreferences sp = getSharedPreferences("StudentInfo",MODE_PRIVATE);
        Uri datauri = Uri.parse(sp.getString("uri",""));
        if (datauri!=null)
        {
            Toast.makeText(this, datauri+"", Toast.LENGTH_SHORT).show();
            String url="http://dnpcoealumni.horizonapp.xyz/StudentUpdate.php";
            sendData(url,datauri);
        }
        else {
            Toast.makeText(this, datauri+"", Toast.LENGTH_SHORT).show();
            String url="http://dnpcoealumni.horizonapp.xyz/StudentUpdate.php";
            sendData(url,datauri);
        }

    }
    private void sendData(String url, Uri datauri)
    {
        if (txtVPNR.getText().toString().isEmpty()) {
            txtVPNR.setError("Please Enter PNR Number of Student!");
            txtVPNR.requestFocus();

        } else if (txtVName.getText().toString().isEmpty()) {
            txtVName.setError("Please Enter Name of Student!");
            txtVName.requestFocus();

        } else if (txtVMobNo.getText().toString().isEmpty()) {
            txtVMobNo.setError("Please Enter Mobile Number of Student!");
            txtVMobNo.requestFocus();
        } else if (txtVEmail.getText().toString().isEmpty()) {
            txtVEmail.setError("Please Enter Email of Student!");
            txtVEmail.requestFocus();
        } else if (txtVBatch.getText().toString().isEmpty()) {
            txtVBatch.setError("Please Enter Batch of Student!");
            txtVBatch.requestFocus();
        } else if (txtVDept.getText().toString().isEmpty()) {
            txtVDept.setError("Please Enter Depatment of Student!");
            txtVDept.requestFocus();
        } else if (txtVCollege.getText().toString().isEmpty()) {
            txtVCollege.setError("Please Enter College Name of Studnet!");
            txtVCollege.requestFocus();
        } else if (txtVUName.getText().toString().isEmpty()) {
            txtVUName.setError("Please Generate Username");
            txtVUName.requestFocus();
        } else if (txtVPass.getText().toString().isEmpty()) {
            txtVPass.setError("Please Enter Password of Student!");
            txtVPass.requestFocus();
        } else if (txtVBranch.getText().toString().isEmpty()) {
            txtVBranch.setError("Please Enter Branch of Student!");
            txtVBranch.requestFocus();
        } else if (!image) {
            Toast.makeText(this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
        } else {
            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Updating Please Wait...");
            //Uri uri = Uri.parse(uri1);
            hud.show();
            StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Ad_Student_Verify.this, "Response:- "+response, Toast.LENGTH_SHORT).show();
                    hud.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ad_Student_Verify.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("hhh", "Error:- "+ error.getMessage());
                    hud.dismiss();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> M = new HashMap<>();
                    M.put("PNR", txtVPNR.getText().toString().trim());
                    M.put("Name", txtVName.getText().toString().trim());
                    M.put("MobNo", txtVMobNo.getText().toString().trim());
                    M.put("Email", txtVEmail.getText().toString().trim());
                    M.put("College", txtVCollege.getText().toString().trim());
                    M.put("Branch", txtVBatch.getText().toString().trim());
                    M.put("Department", txtVDept.getText().toString().trim());
                    M.put("Batch", txtVBatch.getText().toString().trim());
                    M.put("SProfileImg", "Student Profile/" + txtVUName.getText().toString() + ".jpg");
                    M.put("UName", txtVUName.getText().toString().trim());
                    M.put("Password", txtVPass.getText().toString().trim());
                    M.put("isStatus",spnVStatus.getText().toString());
                    return M;
                }
            };
            rq.add(strq);
        }
    }
}