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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Hosp_registration extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5;
    SharedPreferences sh;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_registration);
        e1=(EditText)findViewById(R.id.editTextTextPersonName21);
        e2=(EditText)findViewById(R.id.editTextTextPersonName22);
        e3=(EditText)findViewById(R.id.editTextTextPersonName23);
        e4=(EditText)findViewById(R.id.editTextTextPersonName24);
        e5=(EditText)findViewById(R.id.editTextTextPassword3);
        b=(Button)findViewById(R.id.button6);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                final String hname = e1.getText().toString();
                final String address = e3.getText().toString();
                final String phone = e2.getText().toString();
                final String username = e4.getText().toString();
                final String password = e5.getText().toString();
                if (phone.equalsIgnoreCase("")) {
                    e2.setError("Enter Phone Number");
                } else if (phone.length() < 10) {
                    e2.setError("Enter Valid Phone Number");
                    e2.requestFocus();
                } else if (address.equalsIgnoreCase("")) {
                    e3.setError("Enter Address");
                } else if (hname.equalsIgnoreCase("")) {
                    e1.setError("Enter Hospital Name");
                } else if (password.equalsIgnoreCase("")) {
                    e4.setError("Enter Password");
                } else if (username.equalsIgnoreCase("")) {
                    e5.setError("Enter Username");
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(Hosp_registration.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/hosp_reg";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(Hosp_registration.this, "Success", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(ik);

                                } else if(res.equalsIgnoreCase("invalid")) {


                                    Toast.makeText(Hosp_registration.this, "Username already exists", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), "Error" + url, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("phone", phone);
                            params.put("address", address);
                            params.put("hname", hname);
                            params.put("password", password);
                            params.put("uname", username);

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }
}