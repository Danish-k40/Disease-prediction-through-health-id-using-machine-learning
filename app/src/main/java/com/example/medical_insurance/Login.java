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
import android.widget.TextView;
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

public class Login extends AppCompatActivity {
    EditText e1,e2;
    Button b;
    SharedPreferences sp;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = findViewById(R.id.editTextTextPersonName2);
        e2 = findViewById(R.id.editTextTextPassword);
        tv1 = findViewById(R.id.textView);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b = findViewById(R.id.button2);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RequestQueue queue = Volley.newRequestQueue(Login.this);
                String url = "http://" + sp.getString("ip", "") + ":5000/login";
                final String username = e1.getText().toString();
                final String password = e2.getText().toString();
                if (username.equalsIgnoreCase("")) {
                    e1.setError("Enter Username");
                } else if (password.equalsIgnoreCase("")) {
                    e2.setError("Enter Password ");
                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("result");
                                Toast.makeText(getApplicationContext(), "res"+res, Toast.LENGTH_LONG).show();
                                String arr[]=res.split("#");
                                String task=arr[0];
                                if (res.equalsIgnoreCase("error")) {
                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                                } else if (task.equalsIgnoreCase("success")) {


                                    String type=arr[1];
                                    String lid=arr[2];
                                    Log.e("+++++++++++++++++", type);
                                    if(type.equals("hospital")){
                                        SharedPreferences.Editor ed = sp.edit();
                                        ed.putString("lid", lid);
                                        ed.commit();
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),Hosp_home.class));
                                    }
                                    else if(type.equals("user")){
                                        SharedPreferences.Editor ed = sp.edit();
                                        ed.putString("lid", lid);
                                        ed.commit();
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), Home.class));
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();

                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "In", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error" + e, Toast.LENGTH_LONG).show();

                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("uname", username);
                            params.put("psd", password);

                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Reg.class));
            }
        });
    }
}