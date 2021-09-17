package com.example.medical_insurance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hosp_test_result extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv;
    SharedPreferences sp;
    ArrayList<String> pname,pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_test_result);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lv=(ListView)findViewById(R.id.lv);
        String url ="http://"+sp.getString("ip","")+":5000/hosp_test_result";
        RequestQueue queue = Volley.newRequestQueue(Hosp_test_result.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    pname= new ArrayList<>();
                    pid= new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        pname.add(jo.getString("fname")+(jo.getString("lname")));
                        pid.add(jo.getString("login_id"));




                    }
                    ArrayAdapter<String> ad=new ArrayAdapter<>(Hosp_test_result.this,android.R.layout.simple_list_item_1,pname);
                    lv.setAdapter(ad);
                    lv.setOnItemClickListener(Hosp_test_result.this);
                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Hosp_test_result.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                sp.getString("lid","");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(getApplicationContext(), Hosp_test_result1.class);
        i.putExtra("pid",pid.get(position));
        startActivity(i);
    }
}