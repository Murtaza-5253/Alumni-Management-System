package com.igc.studentalumni;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class Ad_Notices_Fragment extends Fragment
{
    int SELF = 123;
    String UIDSP;
    int UserID;
    String NameSP;
    Button btnSend;
    ArrayList<Message> messages;

    RecyclerView recyclerView;
    RequestQueue rq;
    EditText txtMessage;
    SharedPreferences sp;
    MyClass mc;
    BroadcastReceiver broadcastReceiver;


    //private DatabaseReference RootRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_notices__fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        fetchMessages();
    }
    private void init(View view)
    {
        // lstNotices = view.findViewById(R.id.lstNotices);
        btnSend = view.findViewById(R.id.btnSend);
        sp = this.getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        UIDSP = sp.getString("UID","-1");
        NameSP = sp.getString("Username",null);
        Toast.makeText(getContext(), UIDSP+"-----------"+NameSP, Toast.LENGTH_SHORT).show();
        txtMessage = view.findViewById(R.id.txtMessage);
        messages = new ArrayList<>();
        rq = Volley.newRequestQueue(this.getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                scrollToBottom();
            }
        });
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
                    mc = new MyClass(getContext(),messages,Integer.parseInt(UIDSP));
                    recyclerView.setAdapter(mc);
                    mc.notifyDataSetChanged();
                    scrollToBottom();
                    txtMessage.requestFocus();
                }catch (Exception e){
                    Log.i("Exception Error:- ",e.getMessage());
                    Toast.makeText(getContext(), "Exception error:- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error:- ",error.getMessage());
                Toast.makeText(getContext(), "error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this.getContext(), ""+userId, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
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
                v = LayoutInflater.from(getContext()).inflate(R.layout.ad_chatself,parent,false);
            }
            else{
                v  = LayoutInflater.from(getContext()).inflate(R.layout.ad_chatother,parent,false);
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