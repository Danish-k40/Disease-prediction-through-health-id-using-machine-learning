package com.example.medical_insurance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class View_policies1 extends AppCompatActivity {
    EditText e1,e2,e3;
    Button b1;
    SharedPreferences sh;
    String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_policies1);
        e1=(EditText)findViewById(R.id.editTextTextPersonName9);
        e2=(EditText)findViewById(R.id.editTextTextPersonName14);
        e3=(EditText)findViewById(R.id.editTextTextPersonName16);
        b1=(Button)findViewById(R.id.button5);
        pid=getIntent().getStringExtra("pid");
        Toast.makeText(this, "pid"+pid, Toast.LENGTH_SHORT).show();
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url= "http://"+sh.getString("ip","")+":5000/view_policy1";
        RequestQueue queue = Volley.newRequestQueue(View_policies1.this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {
                    JSONArray ar=new JSONArray(response);

                    JSONObject jo=ar.getJSONObject(0);

                    e1.setText(jo.getString("name"));
                    e2.setText(jo.getString("description"));
                    e3.setText(jo.getString("percentage"));



                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pid", pid);


                return params;
            }
        };
            queue.add(stringRequest);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(View_policies1.this);
                String url1= "http://"+sh.getString("ip","")+":5000/req_policy";
                Toast.makeText(View_policies1.this, ""+url1, Toast.LENGTH_SHORT).show();
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        try {
                            JSONObject jo=new JSONObject(response);
                            String status=jo.getString("task");
                            if(status.equalsIgnoreCase("success")){
                                Toast.makeText(View_policies1.this, "Success", Toast.LENGTH_SHORT);

                                startActivity(new Intent(getApplicationContext(),Home.class));
                            }

                        }catch (Exception e){}

                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("pid", pid);
                        params.put("lid", sh.getString("lid",""));
                        return params;
                    }
                };
                queue.add(stringRequest);


            }
        });
    }
}