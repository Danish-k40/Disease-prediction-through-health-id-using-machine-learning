package com.example.medical_insurance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class Test_history1 extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5;
    SharedPreferences sh;
    String tiid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history1);
        e1=(EditText)findViewById(R.id.editTextTextPersonName15);
        e2=(EditText)findViewById(R.id.editTextTextPersonName17);
        e3=(EditText)findViewById(R.id.editTextTextPersonName18);
        e4=(EditText)findViewById(R.id.editTextTextPersonName19);
        e5=(EditText)findViewById(R.id.editTextTextPersonName20);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tiid=getIntent().getStringExtra("tid");
        Toast.makeText(this, "tid"+tiid, Toast.LENGTH_SHORT).show();
        String url= "http://"+sh.getString("ip","")+":5000/test_history1";
        RequestQueue queue = Volley.newRequestQueue(Test_history1.this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                JSONArray json=null;
                try {
                    json=new JSONArray(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jo=json.getJSONObject(0);
                    e1.setText(jo.getString("name"));
                    e2.setText(jo.getString("test"));
                    e3.setText(jo.getString("description"));
                    e4.setText(jo.getString("date"));
                    e5.setText(jo.getString("result"));


                }catch (Exception e){}


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
                params.put("tid", tiid);
                params.put("lid", sh.getString("lid",""));


                return params;
            }
        };
        queue.add(stringRequest);
    }
}