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
import android.widget.Button;
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

public class Test_history extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv;
    SharedPreferences sp;
    ArrayList<String> test,tid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lv=(ListView)findViewById(R.id.lv);
        String url ="http://"+sp.getString("ip","")+":5000/test_history";
        RequestQueue queue = Volley.newRequestQueue(Test_history.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    test= new ArrayList<>();
                    tid= new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        test.add(jo.getString("test"));
                        tid.add(jo.getString("test_id"));



                    }
                    ArrayAdapter<String> ad=new ArrayAdapter<>(Test_history.this,android.R.layout.simple_list_item_1,test);
                    lv.setAdapter(ad);
                    lv.setOnItemClickListener(Test_history.this);
                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Test_history.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("lid",sp.getString("lid",""));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(getApplicationContext(),Test_history1.class);
        i.putExtra("tid",tid.get(position));
        startActivity(i);
    }

}