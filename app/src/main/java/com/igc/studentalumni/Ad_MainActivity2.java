package com.igc.studentalumni;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.igc.studentalumni.Messages.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Ad_MainActivity2 extends AppCompatActivity {

    int SELF = 123;
    String UIDSP;
    int UserID;
    String NameSP;
    ArrayList<Message> messages;

    RecyclerView recyclerView;
    RequestQueue rq;
    EditText txtMessage;
    SharedPreferences sp;
    MyClass mc;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_main2);
        broadcastReceiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent.getAction().equals("Push Notification"))
                {
                    try {
                        String UName = intent.getStringExtra("name");
                        String RMessage = intent.getStringExtra("message");
                        String RId = intent.getStringExtra("id");

                        processMessage(UName,RMessage,RId);
                    }catch (Exception e)
                    {
                        Toast.makeText(context, "werrr"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        };
        sp = getSharedPreferences("User",MODE_PRIVATE);
        UIDSP = sp.getString("UserId","-1");
        NameSP = sp.getString("Username",null);
        Toast.makeText(this, UIDSP+"-----------"+NameSP, Toast.LENGTH_SHORT).show();
        txtMessage = findViewById(R.id.txtMessage);
        messages = new ArrayList<>();
        rq = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetchMessages();

    }

    private void fetchMessages()
    {

        String url = "https://dnpcoealumni.horizonapp.xyz/showMessage.php";
        StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for(int i=0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        UserID = jsonObject1.getInt("User_Id");
                        String message = jsonObject1.getString("Message");
                        String name = jsonObject1.getString("Name");
                        String sentAt = jsonObject1.getString("Sent_At");
                        Message message1 = new Message(UserID,message,name,sentAt);
                        messages.add(message1);
                    }
                    mc = new MyClass(Ad_MainActivity2.this,messages,Integer.parseInt(UIDSP));
                    recyclerView.setAdapter(mc);
                    mc.notifyDataSetChanged();
                    scrollToBottom();
                    txtMessage.requestFocus();
                }catch (Exception e){
                    Log.i("Exception Error:- ",e.getMessage());
                    Toast.makeText(Ad_MainActivity2.this, "Exception error:- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error:- ",error.getMessage());
                Toast.makeText(Ad_MainActivity2.this, "error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(strq);
    }

    private void processMessage(String name, String message, String id) {
       Message m = new Message(Integer.parseInt(id), message, getTimeStamp(), name);
        messages.add(m);
        scrollToBottom();
    }

    private void scrollToBottom() {
        mc.notifyDataSetChanged();
        if (mc.getItemCount() > 1) {
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mc.getItemCount() - 1);
        }
    }



    private String getTimeStamp() 
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private void sendMessage() {
        final String message = txtMessage.getText().toString().trim();
        if (message.equalsIgnoreCase(""))
            return;
        String userId = UIDSP;
        Toast.makeText(this, ""+userId, Toast.LENGTH_SHORT).show();
        String name = NameSP;
        String sentAt = getTimeStamp();

       Message m = new Message(Integer.parseInt(userId), message, sentAt, name);
        messages.add(m);
        mc.notifyDataSetChanged();

        scrollToBottom();

        txtMessage.setText("");
        String url = "https://dnpcoealumni.horizonapp.xyz/addMessage.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Ad_MainActivity2.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Ad_MainActivity2.this, "error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Id", userId);
                params.put("Message", message);
                params.put("Name", name);
                params.put("Sent_At",getTimeStamp());
                return params;
            }
        };

        //Disabling retry to prevent duplicate messages
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);
        rq.add(stringRequest);
    }

    public void Send(View view)
    {
        sendMessage();
        scrollToBottom();
    }

    class MyClass extends RecyclerView.Adapter<MyClass.NewHolder>
    {
        private final int UserId;

        public MyClass(Context context, ArrayList<Message> messages, int UserId){
            this.UserId = UserId;
        }

        public int getItemViewType(int position)
        {
            Message message = messages.get(position);
            if (message.getUsersId()==UserId)
            {
                return SELF;
            }
            return position;
        }


        @NonNull
        @Override
        public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v ;
            if (viewType == SELF){
                v = LayoutInflater.from(Ad_MainActivity2.this).inflate(R.layout.ad_chatself,parent,false);
            }
            else{
                v  = LayoutInflater.from(Ad_MainActivity2.this).inflate(R.layout.ad_chatother,parent,false);
            }
            return new NewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position) {
            Message message = messages.get(position);
            holder.textViewMessage.setText(message.getMessage());
            holder.textViewTime.setText(message.getName()+", "+message.getSentAt());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class NewHolder extends RecyclerView.ViewHolder{
            public TextView textViewMessage;
            public TextView textViewTime;
            public NewHolder(@NonNull View itemView) {
                super(itemView);
                textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
                textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
            }
        }
    }
}