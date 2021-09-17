package com.example.medical_insurance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11;
    RadioButton r1,r2;
    SharedPreferences sh;
    String gender;
    Button b1,b2;
    DatePickerDialog datepicker;
    private static final int FILE_SELECT_CODE = 0;
    int res;
    String fileName = "", path = "";
    String fname,lname,place,post,pin,phone,email,uname,psd,dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        e1=(EditText)findViewById(R.id.editTextTextPersonName3);
        e2=(EditText)findViewById(R.id.editTextTextPersonName4);
        e3=(EditText)findViewById(R.id.editTextTextPersonName5);
        e4=(EditText)findViewById(R.id.editTextTextPersonName6);
        e5=(EditText)findViewById(R.id.editTextTextPersonName7);
        e6=(EditText)findViewById(R.id.editTextTextPersonName8);
        e7=(EditText)findViewById(R.id.editTextTextPersonName13);
        e8=(EditText)findViewById(R.id.editTextTextPersonName10);
        e9=(EditText)findViewById(R.id.editTextTextPersonName11);
        e10=(EditText)findViewById(R.id.editTextTextPersonName12);
        e11=(EditText)findViewById(R.id.editTextTextPassword2);
        r1=(RadioButton) findViewById(R.id.radioButton);
        r2=(RadioButton) findViewById(R.id.radioButton2);
        b1=(Button)findViewById(R.id.button3);
        b2=(Button)findViewById(R.id.button4);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e3.setInputType(InputType.TYPE_NULL);
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                e3.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //getting all types of files
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, ""), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = e1.getText().toString();
                lname = e2.getText().toString();
                place = e4.getText().toString();
                post = e5.getText().toString();
                pin = e6.getText().toString();
                phone = e7.getText().toString();
                email = e8.getText().toString();
                uname = e10.getText().toString();
                psd = e11.getText().toString();
                dob=e3.getText().toString();
                if(r1.isChecked()){
                    gender=r1.getText().toString();
                }
                else{
                    gender=r2.getText().toString();
                }
                if (fname.equalsIgnoreCase("")) {
                    e1.setError("Enter First Name");
                } else if (lname.equalsIgnoreCase("")) {
                    e2.setError("Enter Last Name");

                } else if (phone.equalsIgnoreCase("")) {
                    e7.setError("Enter Phone Number");
                } else if (phone.length() != 10) {
                    e7.setError("Enter Valid Phone Number");
                    e7.requestFocus();
                } else if (place.equalsIgnoreCase("")) {
                    e4.setError("Enter Place");
                } else if (post.equalsIgnoreCase("")) {
                    e5.setError("Enter Post");
                } else if (pin.equalsIgnoreCase("")) {
                    e6.setError("Enter PIN");
                } else if (pin.length() != 6) {
                    e6.setError("Enter Valid PIN");
                    e6.requestFocus();
                } else if (uname.equalsIgnoreCase("")) {
                    e10.setError("Enter Username");
                } else if (psd.equalsIgnoreCase("")) {
                    e11.setError("Enter Password");
                } else if (dob.equalsIgnoreCase("")) {
                    e3.setError("Enter DOB");

                } else {

                    res = uploadFile(path);

                    if (res == 1) {
                        Toast.makeText(getApplicationContext(), " Success", Toast.LENGTH_LONG).show();
                        Intent ik = new Intent(getApplicationContext(), Login.class);
                        startActivity(ik);

                    } else {
                        Toast.makeText(getApplicationContext(), " Error", Toast.LENGTH_LONG).show();
                    }

                }
            }

        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("File Uri", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        path = FileUtils.getPath(this, uri);
                        //e4.setText(path1);
                        Log.d("path", path);
                        File fil = new File(path);
                        int fln = (int) fil.length();
                          e9.setText(path);

                        File file = new File(path);

                        byte[] byteArray = null;
                        try {
                            InputStream inputStream = new FileInputStream(fil);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] b = new byte[fln];
                            int bytesRead = 0;

                            while ((bytesRead = inputStream.read(b)) != -1) {
                                bos.write(b, 0, bytesRead);
                            }

                            byteArray = bos.toByteArray();
                            inputStream.close();
                            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            if (bmp != null) {
//
//

                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Please select suitable file", Toast.LENGTH_LONG).show();
                }
                break;


        }


    }



    public int uploadFile(String sourceFileUri) {
        try {
            fileName = sourceFileUri;
            String upLoadServerUri = "http://" + sh.getString("ip", "") + ":5000/user_reg";
            Toast.makeText(getApplicationContext(), upLoadServerUri, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
            FileUpload fp = new FileUpload(fileName);
            Map mp = new HashMap<String, String>();
            mp.put("fname", fname);
            mp.put("lname", lname);
            mp.put("place", place);
            mp.put("post", post);
            mp.put("pin", pin);
            mp.put("gender", gender);
            mp.put("phone", phone);
            mp.put("email", email);
            mp.put("uname", uname);
            mp.put("psd", psd);
            mp.put("dob", dob);

            fp.multipartRequest(upLoadServerUri, mp, fileName, "files", "application/octet-stream");
            return 1;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
            return 0;
        }
    }
}