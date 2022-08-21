package com.igc.studentalumni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Al_Placements_Fragment extends Fragment {
    String url;
    Uri uris;
    RecyclerRefreshLayout recyclerRefreshLayout;
    RecyclerView lstPlacements;
    KProgressHUD hud;
    RequestQueue rq;
    Activity activity;
    StorageReference storageReference;
    TextView txtNoPlacements;
    List<String> lstSrNo,lstimgCompany,lstCompanyName,lstCyLocation,lstJobTitle,lstCyEmail,lstJobType,lstUploadDate,lstUploadTime,lstExpSalary,lstCyInfo,lstJobDesc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.al_placements__fragment,container,false);
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
        lstPlacements = view.findViewById(R.id.lstPlacements);
        lstSrNo = new ArrayList<>();
        lstCompanyName = new ArrayList<>();
        lstCyLocation = new ArrayList<>();
        lstJobTitle = new ArrayList<>();
        lstJobType = new ArrayList<>();
        lstExpSalary = new ArrayList<>();
        lstJobDesc = new ArrayList<>();
        lstCyInfo = new ArrayList<>();
        lstCyEmail = new ArrayList<>();
        lstimgCompany = new ArrayList<>();
        lstUploadDate = new ArrayList<>();
        lstUploadTime = new ArrayList<>();
        txtNoPlacements = view.findViewById(R.id.txtNoPlacements);
        recyclerRefreshLayout = view.findViewById(R.id.refrshlyt);
        rq= Volley.newRequestQueue(this.getActivity());
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    private void clearlists()
    {
        lstSrNo.clear();
        lstCompanyName.clear();
        lstCyLocation.clear();
        lstJobTitle.clear();
        lstJobType.clear();
        lstExpSalary.clear();
        lstJobDesc.clear();
        lstCyInfo.clear();
        lstimgCompany.clear();
        lstCyEmail.clear();
        lstUploadDate.clear();
        lstUploadTime.clear();
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

    private void AccData()
    {
        url="https://dnpcoealumni.horizonapp.xyz/PlacementAcc.php";
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
                        lstCompanyName.add(jsonObject.getString("CyName"));
                        lstCyLocation.add(jsonObject.getString("CyBranch"));
                        lstJobTitle.add(jsonObject.getString("JobTitle"));
                        lstJobType.add(jsonObject.getString("JobType"));
                        lstExpSalary.add(jsonObject.getString("ExpSalary"));
                        lstJobDesc.add(jsonObject.getString("JobDesc"));
                        lstCyInfo.add(jsonObject.getString("CyInfo"));
                        lstCyEmail.add(jsonObject.getString("CyEmail"));
                        lstimgCompany.add(jsonObject.getString("CyProfile"));
                        lstUploadDate.add(jsonObject.getString("UploadDate"));
                        lstUploadTime.add(jsonObject.getString("UploadTime"));

                    }
                    Toast.makeText(activity, lstimgCompany+"", Toast.LENGTH_SHORT).show();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    lstPlacements.setLayoutManager(linearLayoutManager);

                    MyClass mc = new MyClass();
                    lstPlacements.setAdapter(mc);
                    hud.dismiss();
//                    Toast.makeText(getActivity(), ""+lstCompanyName+""+lstCyLocation+""+lstJobTitle+""+lstJobType+""+lstExpSalary+""+lstJobDesc+""+lstCyInfo+""+lstUploadDate+""+lstUploadTime, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(lstCompanyName.size()==0)
                {
                    txtNoPlacements.setVisibility(View.VISIBLE);
                    lstPlacements.setVisibility(View.GONE);
                }
                else {
                    lstPlacements.setVisibility(View.VISIBLE);
                    txtNoPlacements.setVisibility(View.GONE);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("tafs", error.getMessage());
            }
        });
        rq.add(jsobj);
    }
    class MyClass extends RecyclerView.Adapter<MyClass.NewHolder>
    {
        @NonNull
        @Override
        public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v =  inflater.inflate(R.layout.al_placements_design,parent,false);
            return new NewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position)
        {
            if(activity==null)
            {
                return;
            }
            holder.txtCyName.setText(lstCompanyName.get(position));
            holder.txtCyLocation.setText(lstCyLocation.get(position));
            holder.txtJobTitle.setText(lstJobTitle.get(position));
            holder.txtJobType.setText(lstJobType.get(position));
            holder.txtPostedOn.setText(lstUploadDate.get(position));
            holder.txtExpectedSalary.setText(lstExpSalary.get(position));
            //Toast.makeText(getActivity(), "set", Toast.LENGTH_SHORT).show();
            FirebaseStorage.getInstance().getReference().child(lstimgCompany.get(position)).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            if(activity==null)
                            {
                                return;
                            }
                            uris=uri;
                            Glide.with(getActivity()).load(uri).into(holder.imgCompany);
                            hud.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



            holder.imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentBundle(holder,position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return lstCompanyName.size();
        }

        class NewHolder extends RecyclerView.ViewHolder
        {
            CircularImageView imgCompany;
            ImageView imgMore;
            TextView txtCyName,txtCyLocation,txtJobTitle,txtJobType,txtExpectedSalary,txtPostedOn;

            public NewHolder(@NonNull View itemView) {
                super(itemView);
                imgCompany = itemView.findViewById(R.id.imgCompany);
                imgMore = itemView.findViewById(R.id.imgMore);


                txtCyName = itemView.findViewById(R.id.txtCyName);
                txtCyLocation = itemView.findViewById(R.id.txtcyLocation);
                txtJobTitle = itemView.findViewById(R.id.txtJobTitle);
                txtJobType = itemView.findViewById(R.id.txtJobType);
                txtExpectedSalary = itemView.findViewById(R.id.txtExpectedSalary);
                txtPostedOn = itemView.findViewById(R.id.txtPostedOn);
            }
        }
    }
    private void intentBundle(@NonNull MyClass.NewHolder holder, int position)
    {
        Intent i = new Intent(getActivity(),Al_More_Placement_Fragment.class);
        i.putExtra("SrNo",lstSrNo.get(position));
        i.putExtra("CyProfile",lstimgCompany.get(position));
        i.putExtra("CyName",lstCompanyName.get(position));
        i.putExtra("CyBranch",lstCyLocation.get(position));
        i.putExtra("CyEmail",lstCyEmail.get(position));
        i.putExtra("JobTitle",lstJobTitle.get(position));
        i.putExtra("JobType",lstJobType.get(position));
        i.putExtra("ExpSalary",lstExpSalary.get(position));
        i.putExtra("UploadDate",lstUploadDate.get(position));
        i.putExtra("JobDesc",lstJobDesc.get(position));
        i.putExtra("CyInfo",lstCyInfo.get(position));
        i.putExtra("UploadTime",lstUploadTime.get(position));

        startActivity(i);
    }


}