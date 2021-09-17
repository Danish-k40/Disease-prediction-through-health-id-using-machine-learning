package com.example.medical_insurance;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Disease_prediction extends AppCompatActivity implements OnClickListener {
    Spinner s1;
    Button b1,b2;
    ListView l1;
    String url="";
    SharedPreferences sh;
    ArrayList<String> result,symptoms;
    TextView t1,t2,t3,t4,t5,t6;
    String info="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_prediction);
        s1=(Spinner) findViewById(R.id.spinner);
        t1=(TextView) findViewById(R.id.textView3);
        t2=(TextView) findViewById(R.id.textView102);
        t3=(TextView) findViewById(R.id.textView104);
        b1=(Button) findViewById(R.id.button19);
        b2=(Button) findViewById(R.id.button20);
        l1=(ListView) findViewById(R.id.list1);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        result=new ArrayList<String>();
        String url1 ="http://"+sh.getString("ip","")+":5000/viewsymptoms";

        RequestQueue queue = Volley.newRequestQueue(Disease_prediction.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    symptoms= new ArrayList<String>();

                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        symptoms.add(jo.getString("Symptoms"));



                    }

                    ArrayAdapter<String> ad=new ArrayAdapter<String>(Disease_prediction.this,android.R.layout.simple_spinner_item,symptoms);
                    s1.setAdapter(ad);

                    // l1.setAdapter(new custom2(Monitoring_signal.this,notification,date));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);







        b1.setOnClickListener(this);
        b2.setOnClickListener(this);




    }

    @Override
    public void onClick(View arg0) {


        if(arg0==b1)
        {




            String val=s1.getSelectedItem().toString();
            result.add(val);
            ArrayAdapter<String>ad=new ArrayAdapter<String>(Disease_prediction.this,android.R.layout.simple_list_item_1,result);
            l1.setAdapter(ad);

        }
        if(arg0==b2) {

            for (int i = 0; i < result.size(); i++)
                info += result.get(i) + "#";
            url = "http://" + sh.getString("ip", "") + ":5000/disease_predict";
            RequestQueue queue = Volley.newRequestQueue(Disease_prediction.this);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the response string.

                    try {
                        JSONObject jo = new JSONObject(response);
                        String result = jo.getString("task");
                        if(result.equals("Normal"))
                        {
                            t1.setText(result);
                            t2.setVisibility(View.INVISIBLE);
                            t3.setVisibility(View.INVISIBLE);

                        }
                        else  if(result.equals("same"))
                        {
                            t1.setText(result);
                            t2.setVisibility(View.INVISIBLE);
                            t3.setVisibility(View.INVISIBLE);
                        }
                        else {
                            String rr[] = result.split("#");
                            String disease = rr[0];
                            String description = rr[1];
                            String medicine = rr[2];
                            Toast.makeText(Disease_prediction.this,description , Toast.LENGTH_SHORT).show();
                            Toast.makeText(Disease_prediction.this, medicine, Toast.LENGTH_SHORT).show();


                            if (!result.equalsIgnoreCase("")) {
                                Toast.makeText(Disease_prediction.this, "Finish", Toast.LENGTH_SHORT).show();
                                t1.setText(disease);
                                t2.setText(description);
                                t3.setText(medicine);
//                            startActivity(new Intent(getApplicationContext(),User_home.class));


                            } else {
                                Toast.makeText(Disease_prediction.this, "Invalid ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Log.d("=========", e.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Disease_prediction.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("symptoms", info);

                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);

        }


    }
}