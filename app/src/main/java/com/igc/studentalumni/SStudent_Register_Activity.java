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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SStudent_Register_Activity extends AppCompatActivity
{
    CircularImageView imgProfile;
    TextInputEditText txtPNR,txtName,txtMobNo,txtEmail,txtBatch,txtDepartment,txtCollege,txtUName,txtPassword,txtBranch;
    Boolean image=false;
    String url;
    RequestQueue rq;
    int imgid;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    KProgressHUD hud;
    Uri uri,bundleuri;
    FirebaseUser fbuser;
    String Path;
    String alprofile,GrNo,Name,MobNo,Email,Branch,Batch,Dept,Company,Package,Post,UName,Password;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sstudent__register__layout);
        init();

//        try {
//
//
//            Bundle b = getIntent().getExtras();
//            alprofile = b.getString("AlProfile");
//            GrNo = b.getString("GrNo");
//            Name = (String) b.getString("Name");
//            MobNo = b.getString("MobNo");
//            Email = b.getString("Email");
//            Branch = b.getString("Branch");
//            Batch = b.getString("Batch");
//            Dept = b.getString("Dept");
//            Company = b.getString("Company");
//            Package = b.getString("Package");
//            Post = b.getString("Post");
//            UName = b.getString("UName");
//            Password = b.getString("Password");
//
//            FirebaseStorage.getInstance().getReference().child(alprofile).getDownloadUrl()
//                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            bundleuri=uri;
//                            Toast.makeText(SStudent_Register_Activity.this, bundleuri+"", Toast.LENGTH_SHORT).show();
//                            Glide.with(SStudent_Register_Activity.this).load(uri).into(imgProfile);
//                            image=true;
//                        }
//                    });
//
//            txtGrNo.setText(GrNo);
//            txtName.setText(Name);
//            txtMobNo.setText(MobNo);
//            txtEmail.setText(Email);
//            txtBranch.setText(Branch);
//            txtBatch.setText(Batch);
//            txtDepartment.setText(Dept);
//            txtCompany.setText(Company);
//            txtPackage.setText(Package);
//            txtPost.setText(Post);
//            txtUName.setText(UName);
//            txtPassword.setText(Password);
//            btnRegister.setText("Update");
//
//        }catch (Exception e)
//        {
//
//        }


    }
    private void init()
    {
        btnRegister = findViewById(R.id.btnRegister);
        imgProfile = findViewById(R.id.imgProfile);
        txtPNR = findViewById(R.id.txtPNR);
        txtName = findViewById(R.id.txtName);
        txtMobNo = findViewById(R.id.txtMobNo);
        txtBatch = findViewById(R.id.txtBatch);
        txtDepartment = findViewById(R.id.txtDepartment);
        txtCollege = findViewById(R.id.txtCollege);
        txtUName = findViewById(R.id.txtUName);
        txtPassword = findViewById(R.id.txtPassword);
        txtBranch = findViewById(R.id.txtBranch);
        txtEmail = findViewById(R.id.txtEmail);
        rq = Volley.newRequestQueue(this);
//        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseApp.initializeApp(this);
        FirebaseStorage fb = FirebaseStorage.getInstance();
        storageReference = fb.getReference();
    }


    public void profile(View view)
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED))
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.INTERNET, Manifest.permission.CAMERA},101);
        }
        else
        {
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start(101);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && data!=null)
        {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
//            uri = getImageUri(getApplicationContext(),bmp);
            uri = data.getData();
            Glide.with(this).load(uri).into(imgProfile);
            if (uri!=null)
            {
                SharedPreferences sp = getSharedPreferences("StudentProfile",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("uri", String.valueOf(uri));
                edit.apply();
            }
            Toast.makeText(this, uri+"", Toast.LENGTH_SHORT).show();




//            imgProfile.setImageBitmap(bmp);
            image=true;
        }
    }


    private Uri getImageUri(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        String Path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bitmap,txtUName.getText().toString(),null);
        return Uri.parse(Path);

    }

    public void register(View view)
    {
        SharedPreferences sp = getSharedPreferences("StudentProfile",MODE_PRIVATE);
        Uri datauri = Uri.parse(sp.getString("uri",""));
        if(btnRegister.getText().toString().equals("Update"))
        {
            String upurl = "http://dnpcoeStudent.horizonapp.xyz/StudentUpdate.php";
            String tag = "Updating";


            sendData(upurl,tag,datauri);
        }
        else {
            String regurl = "https://dnpcoealumni.horizonapp.xyz/UserReg.php";
            String tag = "Registering";

            sendData(regurl,tag,datauri);

        }

    }

    private void sendData(String url, String tag, Uri datauri)
    {
        if (txtPNR.getText().toString().isEmpty()) {
            txtPNR.setError("Please Enter PNR Number of Student!");
            txtPNR.requestFocus();

        } else if (txtName.getText().toString().isEmpty()) {
            txtName.setError("Please Enter Name of Student!");
            txtName.requestFocus();

        } else if (txtMobNo.getText().toString().isEmpty()) {
            txtMobNo.setError("Please Enter Mobile Number of Student!");
            txtMobNo.requestFocus();
        } else if (txtEmail.getText().toString().isEmpty()) {
            txtEmail.setError("Please Enter Email of Student!");
            txtEmail.requestFocus();
        } else if (txtBatch.getText().toString().isEmpty()) {
            txtBatch.setError("Please Enter Batch of Student!");
            txtBatch.requestFocus();
        } else if (txtDepartment.getText().toString().isEmpty()) {
            txtDepartment.setError("Please Enter Depatment of Student!");
            txtDepartment.requestFocus();
        } else if (txtCollege.getText().toString().isEmpty()) {
            txtCollege.setError("Please Enter College Name of Studnet!");
            txtCollege.requestFocus();
        } else if (txtUName.getText().toString().isEmpty()) {
            txtUName.setError("Please Generate Username");
            txtUName.requestFocus();
        } else if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Please Enter Password of Student!");
            txtPassword.requestFocus();
        } else if (txtBranch.getText().toString().isEmpty()) {
            txtBranch.setError("Please Enter Branch of Student!");
            txtBranch.requestFocus();
        } else if (!image) {
            Toast.makeText(this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
        } else {
            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(tag+" Please Wait...");
            String Status = "Unverified";
            //Uri uri = Uri.parse(uri1);
            hud.show();
            StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(SStudent_Register_Activity.this, "Response:- "+response, Toast.LENGTH_SHORT).show();
                    if (datauri != null) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("Student Profile/" + txtUName.getText().toString() + ".jpg");
                        storageReference.putFile(datauri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                hud.dismiss();
                                txtPNR.setText("");
                                txtName.setText("");
                                txtMobNo.setText("");
                                txtEmail.setText("");
                                txtBatch.setText("");
                                txtDepartment.setText("");
                                txtCollege.setText("");
                                txtUName.setText("");
                                txtPassword.setText("");
                                txtBranch.setText("");
                                imgProfile.setImageDrawable(AppCompatResources.getDrawable(SStudent_Register_Activity.this, R.drawable.reading));
                                Toast.makeText(SStudent_Register_Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hud.dismiss();
                                Toast.makeText(SStudent_Register_Activity.this, "exception:- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("rtvdfgfvsa", e.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(SStudent_Register_Activity.this, "No Image", Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SStudent_Register_Activity.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("hhh", error.getMessage());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> M = new HashMap<>();
                    M.put("PNR", txtPNR.getText().toString().trim());
                    M.put("Name", txtName.getText().toString().trim());
                    M.put("MobNo", txtMobNo.getText().toString().trim());
                    M.put("Email", txtEmail.getText().toString().trim());
                    M.put("College", txtCollege.getText().toString().trim());
                    M.put("Branch", txtBranch.getText().toString().trim());
                    M.put("Department", txtDepartment.getText().toString().trim());
                    M.put("Batch", txtBatch.getText().toString().trim());
                    M.put("SProfileImg", "Student Profile/" + txtUName.getText().toString() + ".jpg");
                    M.put("UName", txtUName.getText().toString().trim());
                    M.put("Password", txtPassword.getText().toString().trim());
                    M.put("isStatus",Status.trim());
                    return M;
                }
            };
            rq.add(strq);
        }
    }
}