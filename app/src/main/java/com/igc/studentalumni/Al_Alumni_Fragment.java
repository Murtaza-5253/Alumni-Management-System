package com.igc.studentalumni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Al_Alumni_Fragment extends Fragment
{
    String url;
    Uri uris;
    RecyclerRefreshLayout recyclerRefreshLayout;
    RecyclerView lstAlumni;
    KProgressHUD hud;
    RequestQueue rq;
    Activity activity;
    TextView txtNoData;
    List<String> lstSrNo,lstAlProfile,lstGrNo,lstName,lstEmail,lstMobNo,lstBranch,lstBatch,lstCompany,lstPackage,lstDept,lstPost;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.al_alumni__fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Accessing");
        hud.show();
        AccData();

        recyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                clearlists();
                AccData();
                recyclerRefreshLayout.setRefreshing(false);
            }
        });

    }
    private void init(View view)
    {
        lstAlumni = view.findViewById(R.id.lstAlumni);
        lstSrNo = new ArrayList<>();
        lstAlProfile = new ArrayList<>();
        lstGrNo = new ArrayList<>();
        lstName = new ArrayList<>();
        lstMobNo = new ArrayList<>();
        lstEmail = new ArrayList<>();
        lstBranch = new ArrayList<>();
        lstBatch = new ArrayList<>();
        lstCompany = new ArrayList<>();
        lstDept = new ArrayList<>();
        lstPackage = new ArrayList<>();
        lstPost = new ArrayList<>();
        txtNoData = view.findViewById(R.id.txtNoData);
        recyclerRefreshLayout = view.findViewById(R.id.refrshlyt);
        rq= Volley.newRequestQueue(this.getActivity());

    }
    private void clearlists()
    {
        lstSrNo.clear();
        lstName.clear();
        lstGrNo.clear();
        lstMobNo.clear();
        lstEmail.clear();
        lstBranch.clear();
        lstBatch.clear();
        lstDept.clear();
        lstCompany.clear();
        lstPackage.clear();
        lstPost.clear();
        lstAlProfile.clear();
    }

    private void AccData()
    {
        url="https://dnpcoealumni.horizonapp.xyz/AlumniAcc.php";
        JsonObjectRequest jsobj = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                JSONObject jsonObject = null;


                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        lstSrNo.add(jsonObject.getString("SrNo"));
                        lstAlProfile.add(jsonObject.getString("AlProfileImg"));
                        lstGrNo.add(jsonObject.getString("GrNo"));
                        lstName.add(jsonObject.getString("Name"));
                        lstMobNo.add(jsonObject.getString("MobNo"));
                        lstEmail.add(jsonObject.getString("Email"));
                        lstBranch.add(jsonObject.getString("Branch"));
                        lstBatch.add(jsonObject.getString("Batch"));
                        lstDept.add(jsonObject.getString("Department"));
                        lstCompany.add(jsonObject.getString("Company"));
                        lstPost.add(jsonObject.getString("Post"));
                        lstPackage.add(jsonObject.getString("Package"));

                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    lstAlumni.setLayoutManager(linearLayoutManager);
                    hud.dismiss();
                    MyClass mc = new MyClass();
                    lstAlumni.setAdapter(mc);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(lstName.size()==0)
                {
                    txtNoData.setVisibility(View.VISIBLE);
                    lstAlumni.setVisibility(View.GONE);
                }
                else {
                    lstAlumni.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(jsobj);

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

    class MyClass extends RecyclerView.Adapter<MyClass.NewHolder>{
        @NonNull
        @Override
        public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v =  inflater.inflate(R.layout.al_alumni_design,parent,false);
            return new NewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position) {
            if(activity==null)
            {
                return;
            }
            holder.txtAlName.setText(lstName.get(position));
            holder.txtAlBranch.setText(lstBranch.get(position));
            holder.txtAlBatch.setText(lstBatch.get(position));
            holder.txtALCompany.setText(lstCompany.get(position));
            holder.txtAlPost.setText(lstPost.get(position));
            holder.txtAlPackage.setText(lstPackage.get(position));
            holder.txtAlDept.setText(lstDept.get(position));
            holder.txtAlMob.setText(lstMobNo.get(position));
            holder.txtAlEmail.setText(lstEmail.get(position));

            FirebaseStorage.getInstance().getReference().child(lstAlProfile.get(position)).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            if(activity==null)
                            {
                                return;
                            }
                            uris=uri;
                            Glide.with(getActivity()).load(uri).into(holder.imgAlProfile);
                            hud.dismiss();
                        }
                    });
            holder.imgSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    hud = KProgressHUD.create(getActivity())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait");
                    hud.show();
                    Uri uri = Uri.parse("mailto:"+lstEmail.get(position)).buildUpon()
                            .build();
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(Intent.createChooser(intent,"Send Mail.."));
                    hud.dismiss();
                }
            });

            holder.imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hud = KProgressHUD.create(getActivity())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait");
                    hud.show();
                    Uri uri = Uri.parse("tel:"+lstMobNo.get(position));
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                    hud.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return lstName.size();
        }

        class NewHolder extends RecyclerView.ViewHolder{
            ImageView imgCall,imgSMS;
            CircularImageView imgAlProfile;
            TextView txtAlName,txtAlBatch,txtALCompany,txtAlDept,txtAlBranch,txtAlPost,txtAlPackage,txtAlMob,txtAlEmail;
            public NewHolder(@NonNull View itemView) {
                super(itemView);
                imgAlProfile = itemView.findViewById(R.id.imgAlProfile);
                imgCall = itemView.findViewById(R.id.imgCall);
                imgSMS = itemView.findViewById(R.id.imgSMS);
                txtAlName = itemView.findViewById(R.id.txtAlName);
                txtAlBranch = itemView.findViewById(R.id.txtAlBranch);
                txtAlBatch = itemView.findViewById(R.id.txtAlBatch);
                txtALCompany = itemView.findViewById(R.id.txtAlCompany);
                txtAlDept = itemView.findViewById(R.id.txtAlDept);
                txtAlPost = itemView.findViewById(R.id.txtAlPost);
                txtAlPackage = itemView.findViewById(R.id.txtAlPackage);
                txtAlMob = itemView.findViewById(R.id.txtAlMob);
                txtAlEmail = itemView.findViewById(R.id.txtAlEmail);
            }
        }
    }
}