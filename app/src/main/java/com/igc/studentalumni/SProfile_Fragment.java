package com.igc.studentalumni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

import static android.content.Context.MODE_PRIVATE;

public class SProfile_Fragment extends Fragment
{
    String UserId,SProfile,PNR,Name,MobNo,Email,Branch,Batch,Dept,College,UName,Password;
    CircularImageView imgPProfile;
    TextInputEditText txtPPNR,txtPName,txtPMobNo,txtPEmail,txtPBatch,txtPDepartment,txtPCollege,txtPUName,txtPPassword,txtPBranch;
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
    Button btnUpdate;
    Activity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sactivity_profile__fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        SharedPreferences editor = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        UserId = editor.getString("UserId","1");
        PNR = editor.getString("PNR","1");
        Name = editor.getString("Name", "1");
        MobNo = editor.getString("MobNo","1");
        Email = editor.getString("Email", "1");
        College = editor.getString("College","1");
        Branch = editor.getString("Branch", "1");
        Dept = editor.getString("Dept","1");
        Batch = editor.getString("Batch", "1");
        SProfile = editor.getString("SProfileImg","1");
        UName = editor.getString("Username", "1");
        Password = editor.getString("Password", "1");
        FirebaseStorage.getInstance().getReference().child(SProfile).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        bundleuri=uri;
                        Toast.makeText(getActivity(), bundleuri+"", Toast.LENGTH_SHORT).show();
                        Glide.with(getActivity()).load(uri).into(imgPProfile);
                        image=true;
                    }
                });

        txtPPNR.setText(PNR);
        txtPName.setText(Name);
        txtPMobNo.setText(MobNo);
        txtPEmail.setText(Email);
        txtPCollege.setText(College);
        txtPBranch.setText(Branch);
        txtPDepartment.setText(Dept);
        txtPBatch.setText(Batch);
        txtPUName.setText(UName);
        txtPPassword.setText(Password);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStud();
            }
        });

        imgPProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile();
            }
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity=null;
    }
    public void profile()
    {
        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED))
        {
            ActivityCompat.requestPermissions(requireActivity(),new String[] {Manifest.permission.INTERNET, Manifest.permission.CAMERA},101);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && data!=null)
        {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
//            uri = getImageUri(getActivity(),bmp);
            uri = data.getData();
            Glide.with(this).load(uri).into(imgPProfile);
            if (uri!=null)
            {
                SharedPreferences sp = getActivity().getSharedPreferences("StudentProfile",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("uri", String.valueOf(uri));
                edit.apply();
            }
            Toast.makeText(getActivity(), uri+"", Toast.LENGTH_SHORT).show();




//            imgPProfile.setImageBitmap(bmp);
            image=true;
        }
    }


    private Uri getImageUri(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        String Path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),bitmap,txtPUName.getText().toString(),null);
        return Uri.parse(Path);

    }

    private void init(View view)
    {
        btnUpdate = view.findViewById(R.id.btnUpdate);
        imgPProfile = view.findViewById(R.id.imgPProfile);
        txtPPNR = view.findViewById(R.id.txtPPNR);
        txtPName = view.findViewById(R.id.txtPName);
        txtPMobNo = view.findViewById(R.id.txtPMobNo);
        txtPBatch = view.findViewById(R.id.txtPBatch);
        txtPDepartment = view.findViewById(R.id.txtPDepartment);
        txtPCollege = view.findViewById(R.id.txtPCollege);
        txtPUName = view.findViewById(R.id.txtPUName);
        txtPPassword = view.findViewById(R.id.txtPPassword);
        txtPBranch = view.findViewById(R.id.txtPBranch);
        txtPEmail = view.findViewById(R.id.txtPEmail);
        rq = Volley.newRequestQueue(getActivity());
//        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseApp.initializeApp(getActivity());
        FirebaseStorage fb = FirebaseStorage.getInstance();
        storageReference = fb.getReference();
    }
    private void updateStud(){
        SharedPreferences sp = getActivity().getSharedPreferences("StudentProfile",MODE_PRIVATE);
        Uri datauri = Uri.parse(sp.getString("uri",""));
        String upurl = "https://dnpcoealumni.horizonapp.xyz/StudentUpdate.php";
        if (txtPPNR.getText().toString().isEmpty()) {
            txtPPNR.setError("Please Enter PNR Number of Student!");
            txtPPNR.requestFocus();

        } else if (txtPName.getText().toString().isEmpty()) {
            txtPName.setError("Please Enter Name of Student!");
            txtPName.requestFocus();

        } else if (txtPMobNo.getText().toString().isEmpty()) {
            txtPMobNo.setError("Please Enter Mobile Number of Student!");
            txtPMobNo.requestFocus();
        } else if (txtPEmail.getText().toString().isEmpty()) {
            txtPEmail.setError("Please Enter Email of Student!");
            txtPEmail.requestFocus();
        } else if (txtPBatch.getText().toString().isEmpty()) {
            txtPBatch.setError("Please Enter Batch of Student!");
            txtPBatch.requestFocus();
        } else if (txtPDepartment.getText().toString().isEmpty()) {
            txtPDepartment.setError("Please Enter Depatment of Student!");
            txtPDepartment.requestFocus();
        } else if (txtPCollege.getText().toString().isEmpty()) {
            txtPCollege.setError("Please Enter College Name of Studnet!");
            txtPCollege.requestFocus();
        } else if (txtPUName.getText().toString().isEmpty()) {
            txtPUName.setError("Please Generate Username");
            txtPUName.requestFocus();
        } else if (txtPPassword.getText().toString().isEmpty()) {
            txtPPassword.setError("Please Enter Password of Student!");
            txtPPassword.requestFocus();
        } else if (txtPBranch.getText().toString().isEmpty()) {
            txtPBranch.setError("Please Enter Branch of Student!");
            txtPBranch.requestFocus();
        } else if (!image) {
            Toast.makeText(getActivity(), "Please Select Profile Pic", Toast.LENGTH_SHORT).show();
        } else {
            hud = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Updating Please Wait...");

            //Uri uri = Uri.parse(uri1);
            hud.show();
            StringRequest strq = new StringRequest(Request.Method.POST, upurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(), "Response:- "+response, Toast.LENGTH_SHORT).show();
                    if (datauri != null) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("Student Profile/" + txtPUName.getText().toString() + ".jpg");
                        storageReference.putFile(datauri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                hud.dismiss();

                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("User",MODE_PRIVATE).edit();
                                editor.putString("PNR",txtPPNR.getText().toString());
                                editor.putString("Name", txtPName.getText().toString());
                                editor.putString("MobNo",txtPMobNo.getText().toString());
                                editor.putString("Email", txtPEmail.getText().toString());
                                editor.putString("College",txtPCollege.getText().toString());
                                editor.putString("Branch", txtPBranch.getText().toString());
                                editor.putString("Dept",txtPDepartment.getText().toString());
                                editor.putString("Batch", txtPBatch.getText().toString());
                                editor.putString("SProfileImg","Student Profile/" + txtPUName.getText().toString() + ".jpg");
                                editor.putString("Username", txtPUName.getText().toString());
                                editor.putString("Password", txtPPassword.getText().toString());
                                editor.apply();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hud.dismiss();
                                Toast.makeText(getContext(), "exception:- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("rtvdfgfvsa", e.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "No Image", Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("hhh", error.getMessage());
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> M = new HashMap<>();
                    M.put("PNR", txtPPNR.getText().toString().trim());
                    M.put("Name", txtPName.getText().toString().trim());
                    M.put("MobNo", txtPMobNo.getText().toString().trim());
                    M.put("Email", txtPEmail.getText().toString().trim());
                    M.put("College", txtPCollege.getText().toString().trim());
                    M.put("Branch", txtPBranch.getText().toString().trim());
                    M.put("Department", txtPDepartment.getText().toString().trim());
                    M.put("Batch", txtPBatch.getText().toString().trim());
                    M.put("SProfileImg", "Student Profile/" + txtPUName.getText().toString() + ".jpg");
                    M.put("UName", txtPUName.getText().toString().trim());
                    M.put("Password", txtPPassword.getText().toString().trim());
                    return M;
                }
            };
            rq.add(strq);
        }
    }

}