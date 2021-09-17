package com.example.medical_insurance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

public class Hosp_qrscan extends AppCompatActivity {
    Button b1;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        b1=(Button)findViewById(R.id.button10);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


    }
}