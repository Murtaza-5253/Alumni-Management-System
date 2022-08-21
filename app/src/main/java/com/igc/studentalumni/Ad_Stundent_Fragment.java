package com.igc.studentalumni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
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

public class Ad_Stundent_Fragment extends Fragment
{
    String Purl="https://dnpcoealumni.horizonapp.xyz/StudAccP.php";
    String Turl="http://dnpcoealumni.horizonapp.xyz/StudAccT.php";
    String tag1 = "Accessing Pending Students";
    String tag2 = "Accessing All Students";
    Uri uris;
    RecyclerRefreshLayout recyclerRefreshLayout,refreshLayout;
    RecyclerView lstPendStudents,lstTotStudents;
    KProgressHUD hud;
    RequestQueue rq;
    TabHost tabHost;
    Activity activity;
    TextView txtNoData,txtPendStudents,txtTotStudents;
    List<String> lstSrNo,lstSProfile,lstPNR,lstName,lstEmail,lstMobNo,lstBranch,lstBatch,lstCollege,lstDept,lstUName,lstPassword,lstStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_stundent__fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(tag1);
        hud.show();
        AccData(Purl);
        clearlists();

        recyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                clearlists();
                AccData(Purl);
                recyclerRefreshLayout.setRefreshing(false);
            }
        });
        refreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearlists();
                AccData(Turl);
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private void init(View view)
    {
        lstPendStudents = view.findViewById(R.id.lstPendStudents);
        lstTotStudents = view.findViewById(R.id.lstTotStudents);
        lstSrNo = new ArrayList<>();
        lstSProfile = new ArrayList<>();
        lstPNR = new ArrayList<>();
        lstName = new ArrayList<>();
        lstMobNo = new ArrayList<>();
        lstEmail = new ArrayList<>();
        lstBranch = new ArrayList<>();
        lstBatch = new ArrayList<>();
        lstCollege = new ArrayList<>();
        lstDept = new ArrayList<>();
        txtNoData = view.findViewById(R.id.txtNoData);
        lstUName = new ArrayList<>();
        lstPassword = new ArrayList<>();
        lstStatus = new ArrayList<>();
        recyclerRefreshLayout = view.findViewById(R.id.refrshlyt);
        refreshLayout = view.findViewById(R.id.refrshlyt1);
        rq= Volley.newRequestQueue(this.getActivity());
        txtPendStudents = view.findViewById(R.id.txtPendingStudents);
        txtTotStudents = view.findViewById(R.id.txtTotalStudents);
        tabHost = view.findViewById(R.id.tabhost);
        setupTabs();

    }

    private void setupTabs() {
        tabHost.setup();
        TabHost.TabSpec sp1 = tabHost.newTabSpec("tab1");
        sp1.setIndicator("Pending",getResources().getDrawable(R.drawable.studnet));
        sp1.setContent(R.id.tab1);
        tabHost.addTab(sp1);
        TabHost.TabSpec sp2 = tabHost.newTabSpec("tab2");
        sp2.setIndicator("Total",getResources().getDrawable(R.drawable.reading));
        sp2.setContent(R.id.tab2);
        tabHost.addTab(sp2);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s)
            {
                int i = tabHost.getCurrentTab();
                if(i==0)
                {
                    txtPendStudents.setText("Pending Students: "+lstPendStudents.getAdapter().getItemCount());
                    txtTotStudents.setText("Total Students: "+lstTotStudents.getAdapter().getItemCount());
                    hud = KProgressHUD.create(getActivity())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel(tag1);
                    hud.show();
                    clearlists();
                    AccData(Purl);
                    recyclerRefreshLayout.setRefreshing(false);
                    refreshLayout.setRefreshing(false);
                }
                if(i==1)
                {
                    txtTotStudents.setText("Total Students: "+lstTotStudents.getAdapter().getItemCount());
                    txtPendStudents.setText("Pending Students: "+lstPendStudents.getAdapter().getItemCount());
                    hud = KProgressHUD.create(getActivity())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel(tag2);
                    hud.show();
                    clearlists();
                    AccData(Turl);
                    refreshLayout.setRefreshing(false);
                }


            }
        });
    }


    private void clearlists()
    {
        lstSrNo.clear();
        lstName.clear();
        lstPNR.clear();
        lstMobNo.clear();
        lstEmail.clear();
        lstBranch.clear();
        lstBatch.clear();
        lstDept.clear();
        lstCollege.clear();
        lstStatus.clear();
        lstUName.clear();
        lstPassword.clear();
        lstSProfile.clear();
    }
    private void AccData(String url)
    {
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
                        lstSProfile.add(jsonObject.getString("SProfileImg"));
                        lstPNR.add(jsonObject.getString("PNR"));
                        lstName.add(jsonObject.getString("Name"));
                        lstMobNo.add(jsonObject.getString("MobNo"));
                        lstEmail.add(jsonObject.getString("Email"));
                        lstBranch.add(jsonObject.getString("Branch"));
                        lstBatch.add(jsonObject.getString("Batch"));
                        lstDept.add(jsonObject.getString("Department"));
                        lstCollege.add(jsonObject.getString("College"));
                        lstUName.add(jsonObject.getString("UName"));
                        lstPassword.add(jsonObject.getString("Password"));
                        lstStatus.add(jsonObject.getString("Status"));

                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                    lstPendStudents.setLayoutManager(linearLayoutManager);
                    lstTotStudents.setLayoutManager(linearLayoutManager1);
                    hud.dismiss();
                    MyClass mc = new MyClass();
                    lstPendStudents.setAdapter(mc);
                    lstTotStudents.setAdapter(mc);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(lstName.size()==0)
                {
                    txtNoData.setVisibility(View.VISIBLE);
                    txtPendStudents.setText("Pending Students: 0");
                    lstPendStudents.setVisibility(View.GONE);
                    lstTotStudents.setVisibility(View.GONE);
                }
                else {

                    lstTotStudents.setVisibility(View.VISIBLE);
                    lstPendStudents.setVisibility(View.VISIBLE);
                    txtPendStudents.setText("Pending Students: "+lstPendStudents.getAdapter().getItemCount());
                    txtTotStudents.setText("Total Students: "+lstTotStudents.getAdapter().getItemCount());
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

    class MyClass extends RecyclerView.Adapter<MyClass.NewHolder>
    {
        @NonNull
        @Override
        public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v =  inflater.inflate(R.layout.ad_student_design,parent,false);
            return new NewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position)
        {
            if(activity==null)
            {
                return;
            }
            holder.txtSName.setText(lstName.get(position));
            holder.txtSBranch.setText(lstBranch.get(position));
            holder.txtSBatch.setText(lstBatch.get(position));
            holder.txtSCollege.setText(lstCollege.get(position));
            holder.txtSDept.setText(lstDept.get(position));
            holder.txtPNR.setText(lstPNR.get(position));

            FirebaseStorage.getInstance().getReference().child(lstSProfile.get(position)).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            if(activity==null)
                            {
                                return;
                            }
                            uris=uri;
                            Glide.with(getActivity()).load(uri).into(holder.imgSProfile);
                            hud.dismiss();
                        }
                    });


            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentBundle(holder,position);
                    Toast.makeText(getActivity(), holder.txtSName.getText().toString(), Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getActivity(),Ad_Alumni_Register_Fragment.class));
                }
            });

            //notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return lstName.size();
        }

        class NewHolder extends RecyclerView.ViewHolder
        {
            CircularImageView imgSProfile,imgEdit;
            TextView txtPNR,txtSName,txtSBatch,txtSCollege,txtSDept,txtSBranch;
            public NewHolder(@NonNull View itemView) {
                super(itemView);
                imgSProfile = itemView.findViewById(R.id.imgSProfile);
                imgEdit = itemView.findViewById(R.id.imgEdit);
                txtPNR = itemView.findViewById(R.id.txtPNR);
                txtSName = itemView.findViewById(R.id.txtSName);
                txtSBatch = itemView.findViewById(R.id.txtSBatch);
                txtSCollege = itemView.findViewById(R.id.txtSCollege);
                txtSDept = itemView.findViewById(R.id.txtSDept);
                txtSBranch = itemView.findViewById(R.id.txtSBranch);
            }
        }
    }
    private void intentBundle(@NonNull MyClass.NewHolder holder, int position)
    {
        Intent i = new Intent(getActivity(), Ad_Student_Verify.class);
        i.putExtra("SProfile",lstSProfile.get(position));
        i.putExtra("PNR",lstPNR.get(position));
        i.putExtra("Name",holder.txtSName.getText().toString());
        i.putExtra("MobNo",lstMobNo.get(position));
        i.putExtra("Email",lstEmail.get(position));
        i.putExtra("Branch",holder.txtSBranch.getText().toString());
        i.putExtra("Batch",holder.txtSBatch.getText().toString());
        i.putExtra("College",holder.txtSCollege.getText().toString());
        i.putExtra("Dept",holder.txtSDept.getText().toString());
        i.putExtra("UName",lstUName.get(position));
        i.putExtra("Password",lstPassword.get(position));
        i.putExtra("Status",lstStatus.get(position));
        startActivity(i);
    }
}