package com.igc.studentalumni;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Al_Login extends AppCompatActivity {


    EditText txtEmail,txtPassword;
    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.al_activity_login);
//
//        pd = new ProgressDialog(com.example.alumini_system.Al_Login.this);
        rq = Volley.newRequestQueue(Al_Login.this);

        txtEmail = findViewById(R.id.txtUName);
        txtPassword = findViewById(R.id.txtPassword);
        rq = Volley.newRequestQueue(this);

        if(isLoggedIn())
        {
            startActivity(new Intent(this, Al_Dashboard.class));
            finish();

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
                                loginUser(SrNo,name,pass,id);

                            }


                            //Al_Login user


                            //Starting chat room we need to create this activity
                            startActivity(new Intent(Al_Login.this, Al_Dashboard.class));
                            finish();
                        } catch (JSONException e ) {
                            Toast.makeText(Al_Login.this, ""+response, Toast.LENGTH_SHORT).show();
                            Log.e("Exception",e.getMessage());
                            Toast.makeText(Al_Login.this, "Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Al_Login.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loginUser(String srNo, String id, String name, String password)
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

    private boolean isLoggedIn()
    {
        return getSharedPreferences("User",MODE_PRIVATE).getBoolean("IS_LOGGED_IN", false);
    }

    public void Register(View view)
    {
        AlRegisterUser();
    }

    public void onLogin(View view) {
        AlRegisterUser();

//        if (txtEmail.getText().toString().equals("Admin")||txtEmail.getText().toString().equals("admin"))
//        {
//            if (txtPassword.getText().toString().equals("Admin")||txtPassword.getText().toString().equals("admin"))
//            {
//                startActivity(new Intent(Al_Login.this,Dashboard_Activity.class));
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

}