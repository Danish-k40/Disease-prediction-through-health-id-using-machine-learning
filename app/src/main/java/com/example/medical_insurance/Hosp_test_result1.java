package com.example.medical_insurance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Hosp_test_result1 extends AppCompatActivity {
    EditText e1,e2,e3,e4;
    SharedPreferences sh;
    Button b1;
    DatePickerDialog datepicker;
    String piid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_test_result1);
        e1=(EditText)findViewById(R.id.editTextTextPersonName25);
        e2=(EditText)findViewById(R.id.editTextTextPersonName26);
        e3=(EditText)findViewById(R.id.editTextTextPersonName27);
        e4=(EditText)findViewById(R.id.editTextTextPersonName28);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1=(Button)findViewById(R.id.button7);
        e3.setInputType(InputType.TYPE_NULL);
        piid=getIntent().getStringExtra("pid");
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(Hosp_test_result1.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                e3.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                final String test = e1.getText().toString();
                final String desc = e2.getText().toString();
                final String date = e3.getText().toString();
                final String result = e4.getText().toString();
                if (test.equalsIgnoreCase("")) {
                    e1.setError("Enter Test Name");
                } else if (desc.equalsIgnoreCase("")) {
                    e2.setError("Enter Test Details");
                } else if (date.equalsIgnoreCase("")) {
                    e3.setError("Enter Test Date");
                } else if (result.equalsIgnoreCase("")) {
                    e4.setError("Enter Test Result");
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(Hosp_test_result1.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/upload_result";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("result");

                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(Hosp_test_result1.this, "Success", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Hosp_home.class);
                                    startActivity(ik);

                                } else if(res.equalsIgnoreCase("invalid")) {


                                    Toast.makeText(Hosp_test_result1.this, "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                            params.put("test", test);
                            params.put("desc", desc);
                            params.put("date", date);
                            params.put("result", result);
                            params.put("pid", piid);
                            params.put("lid", sh.getString("lid",""));

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }
}