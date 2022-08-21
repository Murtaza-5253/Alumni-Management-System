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
import android.widget.Button;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Ad_Placement_Register_Fragment extends AppCompatActivity
{
    CircularImageView imgCompanyProfile;
    TextInputEditText txtCompanyName,txtCompanyBranch,txtJobTitle,txtExpSalary,txtJobDescription,txtCompanyInfo,txtCompanyEmail;
    Boolean image=false;
    String url;
    RequestQueue rq;
    Spinner spnJobType;
    int imgid;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    KProgressHUD hud;
    Uri uri,bundleuris;
    FirebaseUser fbuser;
    String SrNo,cyProfile,cyName,cyBranch,JobTitle,JobType,expSalary,jobDesc,cyInfo,cyEmail;
    Button btnUpPlacement;
    Date date;
    String[] jobType={"Full-Time","Part-Time","Internship"};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_placement__register__fragment);
        init();

        try {


            Bundle b = getIntent().getExtras();
            SrNo = b.getString("SrNo");
            Toast.makeText(this, SrNo, Toast.LENGTH_SHORT).show();
            cyProfile = b.getString("CyProfile");
            cyName = b.getString("CyName");
            cyBranch = (String) b.getString("CyBranch");
            JobTitle = b.getString("JobTitle");
            JobType = b.getString("JobType");
            expSalary = b.getString("ExpSalary");
            jobDesc = b.getString("JobDesc");
            cyInfo = b.getString("CyInfo");
            cyEmail = b.getString("CyEmail");

            FirebaseStorage.getInstance().getReference().child(cyProfile).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            bundleuris=uri;
                            Glide.with(Ad_Placement_Register_Fragment.this).load(bundleuris).into(imgCompanyProfile);
                            image=true;
                        }
                    });

            txtCompanyName.setText(cyName);
            txtCompanyBranch.setText(cyBranch);
            txtJobTitle.setText(JobTitle);
            txtExpSalary.setText(expSalary);
            txtJobDescription.setText(jobDesc);
            txtCompanyInfo.setText(cyInfo);
            txtCompanyEmail.setText(cyEmail);
            btnUpPlacement.setText("Update");

        }catch (Exception e)
        {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.i("Tag",e.getMessage());
        }


    }
    private void init()
    {
        btnUpPlacement = findViewById(R.id.btnUpPlacement);
        imgCompanyProfile = findViewById(R.id.imgCompanyProfile);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtJobTitle = findViewById(R.id.txtJobTitle);
        spnJobType = findViewById(R.id.spnJobType);
        txtExpSalary = findViewById(R.id.txtExpSalary);
        txtJobDescription = findViewById(R.id.txtJobDescription);
        txtCompanyInfo = findViewById(R.id.txtCompanyInfo);
        txtCompanyEmail = findViewById(R.id.txtCompanyEmail);
        txtCompanyBranch = findViewById(R.id.txtCompanyBranch);
        rq = Volley.newRequestQueue(this);
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        adapter = new ArrayAdapter<String>(Ad_Placement_Register_Fragment.this, android.R.layout.simple_list_item_1,jobType);
        spnJobType.setAdapter(adapter);
    }


    public void profile(View view)
    {
        if(ContextCompat.checkSelfPermission(Ad_Placement_Register_Fragment.this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED && (ContextCompat.checkSelfPermission(Ad_Placement_Register_Fragment.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED))
        {
            ActivityCompat.requestPermissions(Ad_Placement_Register_Fragment.this,new String[] {Manifest.permission.INTERNET, Manifest.permission.CAMERA},101);
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
        if(requestCode==101 && data!=null)
        {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
//            uri = getImageUri(getApplicationContext(),bmp);
            uri = data.getData();
            Glide.with(this).load(uri).into(imgCompanyProfile);
            if (uri!=null)
            {
                SharedPreferences sp = getSharedPreferences("PlacementProfile",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("uri", String.valueOf(uri));
                edit.apply();
            }
            Toast.makeText(this, uri+"", Toast.LENGTH_SHORT).show();
//            imgCompanyProfile.setImageBitmap(bmp);
            image=true;
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap)
    {
        date = new Date();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hhmmss");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        String Path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bitmap,simpleDateFormat1.format(date),null);
        return Uri.parse(Path);

    }

    public void uploadPlacement(View view)
    {
        SharedPreferences sp = getSharedPreferences("PlacementProfile",MODE_PRIVATE);
        Uri datauri = Uri.parse(sp.getString("uri",""));
        if (btnUpPlacement.getText().toString().equals("Update")) {
            String upurl = "http://dnpcoealumni.horizonapp.xyz/PlacementUpdate.php";
            String tag = "Updating";


            sendData(upurl, tag,datauri);
        } else {
            String regurl = "http://dnpcoealumni.horizonapp.xyz/PlacementUp.php";
            String tag = "Uploading Placement";

            sendData(regurl, tag, uri);

        }
    }

    private void sendData(String url, String tag, Uri datauri)
    {
        if (txtCompanyName.getText().toString().isEmpty()) {
            txtCompanyName.setError("Please Enter Company Name!");
            txtCompanyName.requestFocus();

        } else if (txtCompanyBranch.getText().toString().isEmpty()) {
            txtCompanyBranch.setError("Please Enter Branch of Company!");
            txtCompanyBranch.requestFocus();

        } else if (txtJobTitle.getText().toString().isEmpty()) {
            txtJobTitle.setError("Please Enter Job Title!");
            txtJobTitle.requestFocus();
        } else if (spnJobType.getSelectedItem().equals("Select")) {
            Toast.makeText(this, "Please Select Job Type", Toast.LENGTH_SHORT).show();
        } else if (txtExpSalary.getText().toString().isEmpty()) {
            txtExpSalary.setError("Please Enter Expected Salary!");
            txtExpSalary.requestFocus();
        } else if (txtJobDescription.getText().toString().isEmpty()) {
            txtJobDescription.setError("Please Enter Job Description!");
            txtJobDescription.requestFocus();
        } else if (txtCompanyInfo.getText().toString().isEmpty()) {
            txtCompanyInfo.setError("Please Enter Company Info !");
            txtCompanyInfo.requestFocus();
        }else if (txtCompanyEmail.getText().toString().isEmpty()) {
            txtCompanyEmail.setError("Please Enter Company Email !");
            txtCompanyEmail.requestFocus();
        }
        else if (!image) {
            Toast.makeText(this, "Please Select Company Profile Pic", Toast.LENGTH_SHORT).show();
        } else {
            date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss");
            Toast.makeText(this, simpleDateFormat.format(date), Toast.LENGTH_SHORT).show();
            hud = KProgressHUD.create(Ad_Placement_Register_Fragment.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(tag+" Please Wait...");
//            SharedPreferences sp = getSharedPreferences("PlacementProfile",MODE_PRIVATE);
//            String uri1 = sp.getString("uri"," ");
//            Uri uri = Uri.parse(uri1);
            hud.show();
            StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Ad_Placement_Register_Fragment.this, response, Toast.LENGTH_SHORT).show();
                    if (datauri != null) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("Placement Profile/" + txtCompanyName.getText().toString() + ".jpg");
                        storageReference.putFile(datauri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                hud.dismiss();
                                txtCompanyName.setText("");
                                txtCompanyBranch.setText("");
                                txtJobTitle.setText("");
                                txtCompanyInfo.setText("");
                                txtExpSalary.setText("");
                                txtJobDescription.setText("");
                                txtCompanyEmail.setText("");
                                imgCompanyProfile.setImageDrawable(AppCompatResources.getDrawable(Ad_Placement_Register_Fragment.this, R.drawable.building));
                                Toast.makeText(Ad_Placement_Register_Fragment.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hud.dismiss();
                                Toast.makeText(Ad_Placement_Register_Fragment.this, "" + e.getCause().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("rtvdfgfvsa", e.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(Ad_Placement_Register_Fragment.this, "No Image", Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Ad_Placement_Register_Fragment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("hhh", error.getMessage());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> M = new HashMap<>();
                    if(SrNo==null){
                        SrNo="100";

                    }
                    M.put("SrNo",SrNo);
                    M.put("CyName", txtCompanyName.getText().toString().trim());
                    M.put("CyBranch", txtCompanyBranch.getText().toString().trim());
                    M.put("JobTitle", txtJobTitle.getText().toString().trim());
                    M.put("JobType", spnJobType.getSelectedItem().toString().trim());
                    M.put("CyProfile","Placement Profile/" + txtCompanyName.getText().toString() + ".jpg");
                    M.put("ExpSalary", txtExpSalary.getText().toString().trim());
                    M.put("CyInfo", txtCompanyInfo.getText().toString().trim());
                    M.put("CyEmail",txtCompanyEmail.getText().toString().trim());
                    M.put("JobDesc", txtJobDescription.getText().toString().trim());
                    M.put("UploadDate", simpleDateFormat.format(date));
                    M.put("UploadTime", simpleDateFormat1.format(date));
                    return M;
                }
            };
            rq.add(strq);
        }
    }


}