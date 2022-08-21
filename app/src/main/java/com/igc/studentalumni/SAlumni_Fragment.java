package com.igc.studentalumni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.stream.Collectors;

public class SAlumni_Fragment extends Fragment
{
    String url;
    Uri uris;
    RecyclerRefreshLayout recyclerRefreshLayout;
    RecyclerView lstAlumni;
    KProgressHUD hud;
    RequestQueue rq;
    Activity activity;
    ListView listCity;
    LinearLayout lytSort,lytFilter;
    TextView txtNoData,txtFilter,txtSort;
    List<String> lstSrNo,lstAlProfile,lstGrNo,lstName,lstEmail,lstCyCity,lstMobNo,lstBranch,lstBatch,lstCompany,lstPackage,lstDept,lstPost,uniqList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sactivity_alumni__fragment,container,false);
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
        txtSort.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_sort_24,0,0,0);
        txtSort.setGravity(Gravity.CENTER);

        txtFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_filter_list_24,0,0,0);
        txtFilter.setGravity(Gravity.CENTER);
        recyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                clearlists();
                AccData();
                recyclerRefreshLayout.setRefreshing(false);
            }
        });

        lytFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Filter");
                View v = getLayoutInflater().inflate(R.layout.sortfilterdes,null);
                alert.setView(v);
                listCity = v.findViewById(R.id.listCity);
                MyCity ma = new MyCity();
                listCity.setAdapter(ma);
                alert.setCancelable(false);
                alert.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                });


                AlertDialog alertDialog = alert.create();
                alertDialog.show();

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
        lstCyCity = new ArrayList<>();
        lstBatch = new ArrayList<>();
        lstCompany = new ArrayList<>();
        lstDept = new ArrayList<>();
        lstPackage = new ArrayList<>();
        lstPost = new ArrayList<>();
        txtNoData = view.findViewById(R.id.txtNoData);
        recyclerRefreshLayout = view.findViewById(R.id.refrshlyt);
        rq= Volley.newRequestQueue(this.getActivity());
        txtFilter = view.findViewById(R.id.txtFilter);
        txtSort = view.findViewById(R.id.txtSort);
        lytSort = view.findViewById(R.id.lytSort);
        lytFilter = view.findViewById(R.id.lytFilter);
        uniqList = new ArrayList<>();

    }
    private void clearlists()
    {
        lstSrNo.clear();
        lstName.clear();
        lstGrNo.clear();
        lstMobNo.clear();
        lstEmail.clear();
        lstBranch.clear();
        lstCyCity.clear();
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
                        lstCyCity.add(jsonObject.getString("CyCity"));
                        lstBatch.add(jsonObject.getString("Batch"));
                        lstDept.add(jsonObject.getString("Department"));
                        lstCompany.add(jsonObject.getString("Company"));
                        lstPost.add(jsonObject.getString("Post"));
                        lstPackage.add(jsonObject.getString("Package"));
                        Toast.makeText(activity, lstCyCity.get(i), Toast.LENGTH_SHORT).show();

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
            View v =  inflater.inflate(R.layout.salumni_design,parent,false);
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

    class MyCity extends BaseAdapter{

        @Override
        public int getCount() {
            return lstCyCity.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            LayoutInflater li = getLayoutInflater();
            view = li.inflate(R.layout.sfilterdes,null);
            RadioButton rbCity;
            RadioGroup rbgCity;
            TextView txtCity;
            rbgCity = view.findViewById(R.id.rbgCity);
            rbCity = view.findViewById(R.id.rbCity);


            uniqList = lstCyCity.stream().distinct().collect(Collectors.toList());


            try {

//                    rbCity.setText(uniqList);


            }catch (IndexOutOfBoundsException e){
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return view;
        }
    }
}