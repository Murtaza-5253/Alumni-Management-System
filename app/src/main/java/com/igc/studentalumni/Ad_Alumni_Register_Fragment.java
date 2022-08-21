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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Ad_Alumni_Register_Fragment extends AppCompatActivity
{
    CircularImageView imgProfile;
    TextInputEditText txtGrNo,txtName,txtMobNo,txtEmail,txtBatch,txtDepartment,txtCompany,txtCyCity,txtPost,txtPackage,txtUName,txtPassword,txtBranch;
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
    String alprofile,GrNo,Name,MobNo,Email,Branch,Batch,Dept,Company,CyCity,Package,Post,UName,Password;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_alumni__register__fragment);
        init();

        try {


            Bundle b = getIntent().getExtras();
            alprofile = b.getString("AlProfile");
            GrNo = b.getString("GrNo");
            Name = (String) b.getString("Name");
            MobNo = b.getString("MobNo");
            Email = b.getString("Email");
            Branch = b.getString("Branch");
            Batch = b.getString("Batch");
            Dept = b.getString("Dept");
            Company = b.getString("Company");
            CyCity = b.getString("CyCity");
            Package = b.getString("Package");
            Post = b.getString("Post");
            UName = b.getString("UName");
            Password = b.getString("Password");

            FirebaseStorage.getInstance().getReference().child(alprofile).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            bundleuri=uri;
//                            Toast.makeText(Ad_Alumni_Register_Fragment.this, bundleuri+"", Toast.LENGTH_SHORT).show();
                            Glide.with(Ad_Alumni_Register_Fragment.this).load(uri).into(imgProfile);
                            image=true;
                        }
                    });

            txtGrNo.setText(GrNo);
            txtName.setText(Name);
            txtMobNo.setText(MobNo);
            txtEmail.setText(Email);
            txtBranch.setText(Branch);
            txtBatch.setText(Batch);
            txtDepartment.setText(Dept);
            txtCompany.setText(Company);
            txtCyCity.setText(CyCity);
            txtPackage.setText(Package);
            txtPost.setText(Post);
            txtUName.setText(UName);
            txtPassword.setText(Password);
            btnRegister.setText("Update");

        }catch (Exception e)
        {

        }


    }
    private void init()
    {
        btnRegister = findViewById(R.id.btnRegister);
        imgProfile = findViewById(R.id.imgProfile);
        txtGrNo = findViewById(R.id.txtGrNo);
        txtName = findViewById(R.id.txtName);
        txtMobNo = findViewById(R.id.txtMobNo);
        txtBatch = findViewById(R.id.txtBatch);
        txtDepartment = findViewById(R.id.txtDepartment);
        txtCompany = findViewById(R.id.txtCompany);
        txtCyCity = findViewById(R.id.txtCyCity);
        txtPost = findViewById(R.id.txtPost);
        txtPackage = findViewById(R.id.txtPackage);
        txtUName = findViewById(R.id.txtUName);
        txtPassword = findViewById(R.id.txtPassword);
        txtBranch = findViewById(R.id.txtBranch);
        txtEmail = findViewById(R.id.txtEmail);
        rq = Volley.newRequestQueue(this);
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
    }


    public void profile(View view)
    {
        if(ContextCompat.checkSelfPermission(Ad_Alumni_Register_Fragment.this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED && (ContextCompat.checkSelfPermission(Ad_Alumni_Register_Fragment.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED))
        {
            ActivityCompat.requestPermissions(Ad_Alumni_Register_Fragment.this,new String[] {Manifest.permission.INTERNET, Manifest.permission.CAMERA},101);
        }
        else
        {
            if (!btnRegister.getText().toString().equals("Update")){
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
            try {
//                uri = getImageUri(getApplicationContext(),bmp);
                uri = data.getData();
                Glide.with(this).load(uri).into(imgProfile);


            }catch (Exception e){
                Log.e("e",e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (uri!=null)
            {
                SharedPreferences sp = getSharedPreferences("AlumniProfile",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("uri", String.valueOf(uri));
                edit.apply();
            }
//            Toast.makeText(this, uri+"", Toast.LENGTH_SHORT).show();




//            imgProfile.setImageBitmap(bmp);
            image=true;
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }


    private Uri getImageUri(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        String Path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bitmap,txtUName.getText().toString(),null);
        return Uri.parse(Path);

    }

    public void Generate_Username(View view) {

        if(txtName.getText().toString().length()<=3)
        {
            String Uname = txtGrNo.getText().toString() + txtName.getText().toString();
            txtUName.setText(Uname);
        }
        else
        {
            String Uname = txtGrNo.getText().toString() + txtName.getText().toString().substring(0, 3);
            txtUName.setText(Uname);

        }

    }

    public void register(View view)
    {
        SharedPreferences sp = getSharedPreferences("AlumniProfile",MODE_PRIVATE);
        Uri datauri = Uri.parse(sp.getString("uri",""));
        if(btnRegister.getText().toString().equals("Update"))
        {
            String upurl = "http://dnpcoealumni.horizonapp.xyz/AlumniUpdate.php";
            String tag = "Updating";


            sendData(upurl,tag,datauri);
        }
        else {
            String regurl = "http://dnpcoealumni.horizonapp.xyz/AlumniReg.php";
            String tag = "Registering";

            sendData(regurl,tag,datauri);

        }

    }

    private void sendData(String url, String tag, Uri datauri)
    {
        if (txtGrNo.getText().toString().isEmpty()) {
            txtGrNo.setError("Please Enter GR Number of Alumni!");
            txtGrNo.requestFocus();

        } else if (txtName.getText().toString().isEmpty()) {
            txtName.setError("Please Enter Name of Alumni!");
            txtName.requestFocus();

        } else if (txtMobNo.getText().toString().isEmpty()) {
            txtMobNo.setError("Please Enter Mobile Number of Alumni!");
            txtMobNo.requestFocus();
        } else if (txtEmail.getText().toString().isEmpty()) {
            txtEmail.setError("Please Enter Email of Alumni!");
            txtEmail.requestFocus();
        } else if (txtBatch.getText().toString().isEmpty()) {
            txtBatch.setError("Please Enter Batch of Alumni!");
            txtBatch.requestFocus();
        } else if (txtDepartment.getText().toString().isEmpty()) {
            txtDepartment.setError("Please Enter Depatment of Alumni!");
            txtDepartment.requestFocus();
        } else if (txtCompany.getText().toString().isEmpty()) {
            txtCompany.setError("Please Enter Name of Company of Alumni!");
            txtCompany.requestFocus();
        }
        else if (txtCyCity.getText().toString().isEmpty()) {
            txtCyCity.setError("Please Enter Name of City of Company of Alumni!");
            txtCyCity.requestFocus();
        }
        else if (txtPost.getText().toString().isEmpty()) {
            txtPost.setError("Please Enter Post of Alumni!");
            txtPost.requestFocus();
        } else if (txtPackage.getText().toString().isEmpty()) {
            txtPackage.setError("Please Enter PACKAGE of Alumni!");
            txtPackage.requestFocus();
        } else if (txtUName.getText().toString().isEmpty()) {
            txtUName.setError("Please Generate Username");
            txtUName.requestFocus();
        } else if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Please Enter Password of Alumni!");
            txtPassword.requestFocus();
        } else if (txtBranch.getText().toString().isEmpty()) {
            txtBranch.setError("Please Enter Branch of Alumni!");
            txtBranch.requestFocus();
        } else if (!image) {
            Toast.makeText(this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
        } else {
            hud = KProgressHUD.create(Ad_Alumni_Register_Fragment.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(tag+" Please Wait...");
            //Uri uri = Uri.parse(uri1);
            hud.show();
            StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Ad_Alumni_Register_Fragment.this, response, Toast.LENGTH_SHORT).show();
                    if (datauri != null) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("Alumni Profile/" + txtUName.getText().toString() + ".jpg");
                        storageReference.putFile(datauri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                hud.dismiss();
                                txtGrNo.setText("");
                                txtName.setText("");
                                txtMobNo.setText("");
                                txtEmail.setText("");
                                txtBatch.setText("");
                                txtDepartment.setText("");
                                txtCompany.setText("");
                                txtCyCity.setText("");
                                txtPackage.setText("");
                                txtPost.setText("");
                                txtUName.setText("");
                                txtPassword.setText("");
                                txtBranch.setText("");
                                imgProfile.setImageDrawable(AppCompatResources.getDrawable(Ad_Alumni_Register_Fragment.this, R.drawable.reading));
                                Toast.makeText(Ad_Alumni_Register_Fragment.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hud.dismiss();
                                Toast.makeText(Ad_Alumni_Register_Fragment.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("rtvdfgfvsa", e.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(Ad_Alumni_Register_Fragment.this, "No Image", Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ad_Alumni_Register_Fragment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("hhh", error.getMessage());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> M = new HashMap<>();
                    M.put("GrNo", txtGrNo.getText().toString().trim());
                    M.put("Name", txtName.getText().toString().trim());
                    M.put("MobNo", txtMobNo.getText().toString().trim());
                    M.put("Email", txtEmail.getText().toString().trim());
                    M.put("Batch", txtBatch.getText().toString().trim());
                    M.put("Department", txtDepartment.getText().toString().trim());
                    M.put("Company", txtCompany.getText().toString().trim());
                    M.put("CyCity", txtCyCity.getText().toString().trim());
                    M.put("Post", txtPost.getText().toString().trim());
                    M.put("Package", txtPackage.getText().toString().trim());
                    M.put("AlProfileImg", "Alumni Profile/" + txtUName.getText().toString() + ".jpg");
                    M.put("Branch", txtBranch.getText().toString().trim());
                    M.put("UName", txtUName.getText().toString().trim());
                    M.put("Password", txtPassword.getText().toString().trim());
                    return M;
                }
            };
            rq.add(strq);
        }
    }
}