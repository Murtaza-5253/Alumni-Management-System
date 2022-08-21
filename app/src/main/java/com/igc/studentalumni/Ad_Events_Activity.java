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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ad_Events_Activity extends Fragment
{

    FloatingActionButton floatingActionButton1;
    String url;
    Uri uris;
    RecyclerRefreshLayout recyclerRefreshLayout;
    RecyclerView lstEvents;
    KProgressHUD hud;
    RequestQueue rq;
    Activity activity;
    StorageReference storageReference;
    TextView txtNoPlacements;
    List<String> lstSrNo,lstimgEvent,lstEventName,lstEventDate,lstCName,lstEvtReq,lstEvtCo,lstECoEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_ad__events_,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        hud = KProgressHUD.create(getContext())
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
        lstEvents = view.findViewById(R.id.lstEvents);
        lstSrNo = new ArrayList<>();
        lstimgEvent = new ArrayList<>();
        lstEventName = new ArrayList<>();
        lstEventDate = new ArrayList<>();
        lstEvtReq = new ArrayList<>();
        lstEvtCo = new ArrayList<>();
        lstECoEmail = new ArrayList<>();
        lstCName = new ArrayList<>();
        txtNoPlacements = view.findViewById(R.id.txtNoPlacements);
        recyclerRefreshLayout = view.findViewById(R.id.refrshlyt);
        rq= Volley.newRequestQueue(getContext());
        storageReference = FirebaseStorage.getInstance().getReference();
        floatingActionButton1 = view.findViewById(R.id.floatingActionButton1);
        onEventRegister(floatingActionButton1);
    }

    private void clearlists()
    {
        lstSrNo.clear();
        lstimgEvent.clear();
        lstEventName.clear();
        lstEventDate.clear();
        lstEvtReq.clear();
        lstEvtCo.clear();
        lstECoEmail.clear();
        lstCName.clear();

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (Activity) getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity=null;
    }

    private void AccData()
    {
        url="https://dnpcoealumni.horizonapp.xyz/EventsAcc.php";
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
                        lstimgEvent.add(jsonObject.getString("imgEvent"));
                        lstEvtReq.add(jsonObject.getString("EvtReq"));
                        lstEventName.add(jsonObject.getString("EName"));
                        lstEventDate.add(jsonObject.getString("EventDate"));
                        lstCName.add(jsonObject.getString("CName"));
                        lstECoEmail.add(jsonObject.getString("ECoEmail"));
                        lstEvtCo.add(jsonObject.getString("EvtCo"));

                    }
//                    Toast.makeText(activity, lstimgCompany+"", Toast.LENGTH_SHORT).show();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    lstEvents.setLayoutManager(linearLayoutManager);

                    MyClass mc = new MyClass();
                    lstEvents.setAdapter(mc);
                    hud.dismiss();
//                    Toast.makeText(getActivity(), ""+lstCompanyName+""+lstCyLocation+""+lstJobTitle+""+lstJobType+""+lstExpSalary+""+lstJobDesc+""+lstCyInfo+""+lstUploadDate+""+lstUploadTime, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(lstEventName.size()==0)
                {
                    txtNoPlacements.setVisibility(View.VISIBLE);
                    lstEvents.setVisibility(View.GONE);
                }
                else {
                    lstEvents.setVisibility(View.VISIBLE);
                    txtNoPlacements.setVisibility(View.GONE);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("tafs", error.getMessage());
            }
        });
        rq.add(jsobj);
    }

    private void DelData(int position,String CName)
    {
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait...");
        hud.show();
        url="https://dnpcoealumni.horizonapp.xyz/EventsDel.php";
        StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                storageReference = FirebaseStorage.getInstance().getReference().child("Event Profile/" + CName + ".jpg");
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                hud.dismiss();
                clearlists();
                AccData();
                //Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                hud.dismiss();
                Log.i("GTags",error.getMessage());

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> m = new HashMap<>();
                m.put("SrNo",lstSrNo.get(position));
                return m;
            }
        };
        rq.add(strq);
    }



    private void onEventRegister(FloatingActionButton floatingActionButton1)
    {
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Ad_Event_Register_Fragment.class);
                startActivity(i);
            }
        });
    }

    class MyClass extends RecyclerView.Adapter<MyClass.NewHolder>
    {
        @NonNull
        @Override
        public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v =  inflater.inflate(R.layout.ad_events_design,parent,false);
            return new NewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position)
        {
            if(activity==null)
            {
                return;
            }
            holder.txtEName.setText(lstEventName.get(position));
            holder.txtEDate.setText(lstEventDate.get(position));
            holder.txtECo.setText(lstEvtCo.get(position));
            holder.txtCName.setText(lstCName.get(position));
            //Toast.makeText(getActivity(), "set", Toast.LENGTH_SHORT).show();
            FirebaseStorage.getInstance().getReference().child(lstimgEvent.get(position)).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            if(activity==null)
                            {
                                return;
                            }
                            uris=uri;
                            Glide.with(getContext()).load(uri).into(holder.imgEvent);
                            hud.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



            holder.imgJobEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentBundle(holder,position);
                }
            });

            holder.imgJobDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelData(position,holder.txtEName.getText().toString());
                }
            });

        }

        @Override
        public int getItemCount() {
            return lstEventName.size();
        }

        class NewHolder extends RecyclerView.ViewHolder
        {
            ImageView imgJobEdit,imgJobDelete;
            CircularImageView imgEvent;
            TextView txtEName,txtEReq,txtEDate,txtCName,txtECEmail,txtECo;


            public NewHolder(@NonNull View itemView) {
                super(itemView);
                imgEvent = itemView.findViewById(R.id.imgEvent);
                imgJobEdit = itemView.findViewById(R.id.imgJobEdit);
                imgJobDelete = itemView.findViewById(R.id.imgJobDelete);


                txtEName = itemView.findViewById(R.id.txtEName);
                txtEDate = itemView.findViewById(R.id.txtEDate);
                txtECEmail = itemView.findViewById(R.id.txtECEmail);
                txtECo = itemView.findViewById(R.id.txtECo);
                txtCName = itemView.findViewById(R.id.txtCName);
                txtEReq = itemView.findViewById(R.id.txtEReq);



            }
        }
    }


    private void intentBundle(@NonNull MyClass.NewHolder holder, int position)
    {
        Intent i = new Intent(getContext(), Ad_Placement_Register_Fragment.class);
        i.putExtra("SrNo",lstSrNo.get(position));
        i.putExtra("CyProfile",lstimgEvent.get(position));
        i.putExtra("CyName",lstEventName.get(position));
        i.putExtra("CyBranch",lstEventDate.get(position));
        i.putExtra("JobTitle",lstEvtReq.get(position));
        i.putExtra("JobType",lstEvtCo.get(position));
        i.putExtra("ExpSalary",lstECoEmail.get(position));
        i.putExtra("UploadDate",lstCName.get(position));
        startActivity(i);
    }
}