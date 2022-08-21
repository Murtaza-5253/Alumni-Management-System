package com.igc.studentalumni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SLogin extends AppCompatActivity {


    EditText txtEmail,txtPassword;
    RequestQueue rq;
    boolean doublebackpressedonce=false;
    KProgressHUD hud;
    String id,PNR,Name,MobNo,Email,College,Branch,Dept,Batch,SProfile,UName,Pass,Status;
    String uid;
    Spinner spnUType;
    String[] utype = {"Select User Type","Admin","Alumni","Student"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sactivity_login);
        spnUType  =findViewById(R.id.spnUType);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,utype);
        spnUType.setAdapter(adapter);
//
//        pd = new ProgressDialog(com.example.alumini_system.SLogin.this);
        rq = Volley.newRequestQueue(SLogin.this);

        txtEmail = findViewById(R.id.txtUName);
        txtPassword = findViewById(R.id.txtPassword);
        rq = Volley.newRequestQueue(this);

//        if(isLoggedIn())
//        {
//            startActivity(new Intent(this, SDashboard_Activity.class));
//            finish();
//
//        }

    }
    private void SRegisterUser()
    {
        hud = KProgressHUD.create(SLogin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait");
        hud.show();

        String url = "https://dnpcoealumni.horizonapp.xyz/UserReg.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                id = jsonObject1.getString("SrNo");
                                PNR = jsonObject1.getString("PNR");
                                Name = jsonObject1.getString("Name");
                                MobNo= jsonObject1.getString("MobNo");
                                Email= jsonObject1.getString("Email");
                                College= jsonObject1.getString("College");
                                Branch= jsonObject1.getString("Branch");
                                Dept= jsonObject1.getString("Department");
                                Batch= jsonObject1.getString("Batch");
                                SProfile= jsonObject1.getString("SProfileImg");
                                UName = jsonObject1.getString("UName");
                                Pass = jsonObject1.getString("Password");
                                Status = jsonObject1.getString("Status");
                                uid = jsonObject1.getString("Id");
                                SloginUser();

                            }

                            hud.dismiss();

                            //SLogin user


                            //Starting chat room we need to create this activity

                        } catch (JSONException e ) {

                            Log.e("Exception",e.getMessage());
                            Toast.makeText(SLogin.this, "Exception"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SLogin.this, "Error:- "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("SName", txtEmail.getText().toString().trim());
                params.put("SPass", txtPassword.getText().toString().trim());
                return params;
            }
        };
        rq.add(stringRequest);
    }

    private void SloginUser()
    {
        //Toast.makeText(this, "id:- "+id+"\nName:- "+name+"\nPass:- "+password, Toast.LENGTH_SHORT).show();
        if (Status.equalsIgnoreCase("Unverified"))
        {
            Toast.makeText(this, "You are not a verified user\n Please Contact your college", Toast.LENGTH_SHORT).show();
        }
        else if(Status.equalsIgnoreCase("Verified"))
        {
            Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getSharedPreferences("User",MODE_PRIVATE).edit();
            editor.putString("UserId", id);
            editor.putString("PNR",PNR);
            editor.putString("Name", Name);
            editor.putString("MobNo",MobNo);
            editor.putString("Email", Email);
            editor.putString("College",College);
            editor.putString("Branch", Branch);
            editor.putString("Dept",Dept);
            editor.putString("Batch", Batch);
            editor.putString("SProfileImg",SProfile);
            editor.putString("Username", UName);
            editor.putString("Password", Pass);
            editor.putString("Status",Status);
            editor.putString("UID",uid);
            editor.putBoolean("IS_LOGGED_IN", true);
            editor.apply();
            startActivity(new Intent(SLogin.this, SDashboard_Activity.class));

            finish();
        }
        else if(Status.equalsIgnoreCase("Blocked"))
        {
            Toast.makeText(this, "Sorry you are blocked by your admin", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Please Contact your admin", Toast.LENGTH_SHORT).show();
        }


    }

    private void AlRegisterUser()
    {

        String url = "https://dnpcoealumni.horizonapp.xyz/UserReg.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String SrNo = jsonObject1.getString("SrNo");
                                String name = jsonObject1.getString("UName");
                                String pass = jsonObject1.getString("Password");
                                String id = jsonObject1.getString("Id");
                                AlloginUser(SrNo,name,pass,id);

                            }


                            //Al_Login user


                            //Starting chat room we need to create this activity
                            startActivity(new Intent(SLogin.this, Al_Dashboard.class));
                            finish();
                        } catch (JSONException e ) {
                            Toast.makeText(SLogin.this, ""+response, Toast.LENGTH_SHORT).show();
                            Log.e("Exception",e.getMessage());
                            Toast.makeText(SLogin.this, "Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SLogin.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ALName", txtEmail.getText().toString().trim());
                params.put("ALPass", txtPassword.getText().toString().trim());
                return params;
            }
        };
        rq.add(stringRequest);
    }

    private void AlloginUser(String srNo, String id, String name, String password)
    {
        Toast.makeText(this, "id:- "+srNo+"\nName:- "+name+"\nPass:- "+password+"\nID:- "+id, Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = getSharedPreferences("User",MODE_PRIVATE).edit();
        editor.putString("UserId", srNo);
        editor.putString("Username", name);
        editor.putString("Password", password);
        editor.putString("UID", id);
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.apply();

    }
    private void AdRegisterUser()
    {

        String url = "https://dnpcoealumni.horizonapp.xyz/UserReg.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("SrNo");
                                String name = jsonObject1.getString("UName");
                                String pass = jsonObject1.getString("Password");
                                String UID = jsonObject1.getString("Id");
                                AdloginUser(id,name,pass, UID);

                            }


                            //Ad_Login user


                            //Starting chat room we need to create this activity
                            startActivity(new Intent(SLogin.this, Ad_Dashboard_Activity.class));
                            finish();
                        } catch (JSONException e ) {
//                            Toast.makeText(Ad_Login.this, ""+response, Toast.LENGTH_SHORT).show();
                            Log.e("Exception",e.getMessage());
//                            Toast.makeText(Ad_Login.this, "Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Ad_Login.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AName", txtEmail.getText().toString().trim());
                params.put("APass", txtPassword.getText().toString().trim());
                return params;
            }
        };
        rq.add(stringRequest);
    }

    private void AdloginUser(String uid, String id, String name, String password)
    {
//        Toast.makeText(this, "id:- "+id+"\nName:- "+name+"\nPass:- "+password+"\nUID:- "+uid, Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = getSharedPreferences("User",MODE_PRIVATE).edit();
        editor.putString("UserId", id);
        editor.putString("Username", name);
        editor.putString("Password", password);
        editor.putString("UID", uid);
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.apply();

    }

    private boolean isLoggedIn()
    {
        return getSharedPreferences("User",MODE_PRIVATE).getBoolean("IS_LOGGED_IN", false);
    }

    public void Register(View view)
    {
        SRegisterUser();
    }

    public void onLogin(View view)
    {
        if (spnUType.getSelectedItem().equals("Select User Type")){
            Toast.makeText(this, "Please Select User Type", Toast.LENGTH_SHORT).show();
        }
        else{
            if(spnUType.getSelectedItem().equals("Student")) {

                SRegisterUser();
            }
            if(spnUType.getSelectedItem().equals("Alumni")){
                AlRegisterUser();
            }
            if(spnUType.getSelectedItem().equals("Admin")){
                AdRegisterUser();
            }
        }

//        if (txtEmail.getText().toString().equals("Admin")||txtEmail.getText().toString().equals("admin"))
//        {
//            if (txtPassword.getText().toString().equals("Admin")||txtPassword.getText().toString().equals("admin"))
//            {
//                startActivity(new Intent(SLogin.this,SDashboard_Activity.class));
//                finish();
//            }
//            else
//            {
//                Toast.makeText(this, "Please Check Password !", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "Please check Username !", Toast.LENGTH_SHORT).show();
//        }
    }

    public void Accregister(View view)
    {
        startActivity(new Intent(SLogin.this, SStudent_Register_Activity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.doublebackpressedonce = false;
    }

    @Override
    public void onBackPressed() {
        if (doublebackpressedonce)
        {
            super.onBackPressed();
            return;
        }
        this.doublebackpressedonce = true;
        Toast.makeText(this, "tap back twice to exit", Toast.LENGTH_SHORT).show();
    }
}