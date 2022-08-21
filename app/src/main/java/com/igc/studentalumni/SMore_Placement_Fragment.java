package com.igc.studentalumni;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;

public class SMore_Placement_Fragment extends AppCompatActivity
{
    CircularImageView imgCompanyProfile;
    TextInputEditText txtCompanyName,txtCompanyBranch,txtJobTitle,txtJobType,txtExpSalary,txtJobDescription,txtCompanyInfo,txtCyEmail;
    Boolean image=false;
    String url;
    RequestQueue rq;
    int imgid;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    KProgressHUD hud;
    Uri uri,bundleuris;
    FirebaseUser fbuser;
    String SrNo,cyProfile,cyName,cyBranch,JobTitle,JobType,expSalary,jobDesc,cyInfo,cyEmail;
    Date date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smore_placements);
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
            cyEmail = b.getString("CyEmail");
            cyInfo = b.getString("CyInfo");

            FirebaseStorage.getInstance().getReference().child(cyProfile).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            bundleuris=uri;
                            Glide.with(SMore_Placement_Fragment.this).load(bundleuris).into(imgCompanyProfile);
                            image=true;
                        }
                    });

            txtCompanyName.setText(cyName);
            txtCompanyBranch.setText(cyBranch);
            txtJobTitle.setText(JobTitle);
            txtJobType.setText(JobType);
            txtExpSalary.setText(expSalary);
            txtJobDescription.setText(jobDesc);
            txtCyEmail.setText(cyEmail);
            txtCompanyInfo.setText(cyInfo);

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Tag",e.getMessage());
        }
    }
    private void init()
    {
        imgCompanyProfile = findViewById(R.id.imgCompanyProfile);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtJobTitle = findViewById(R.id.txtJobTitle);
        txtJobType = findViewById(R.id.txtJobType);
        txtCyEmail = findViewById(R.id.txtCyEmail);
        txtExpSalary = findViewById(R.id.txtExpSalary);
        txtJobDescription = findViewById(R.id.txtJobDescription);
        txtCompanyInfo = findViewById(R.id.txtCompanyInfo);
        txtCompanyBranch = findViewById(R.id.txtCompanyBranch);
        rq = Volley.newRequestQueue(this);
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    public void Apply(View view)
    {
        hud = KProgressHUD.create(SMore_Placement_Fragment.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait");
        hud.show();
        Uri uri = Uri.parse("mailto:"+txtCyEmail.getText().toString().trim());
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Apply for "+txtJobTitle.getText().toString());
        startActivity(Intent.createChooser(intent,"Send Mail.."));
        hud.dismiss();
    }
}
