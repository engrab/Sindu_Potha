package com.slsindupotha;

/**
 * Created by RLMAX on 4/21/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends BaseActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL = "ufull";
    private static final String KEY_NAME = "uname";
    private static final String KEY_NUM = "unum";
    private static final String KEY_ARTIST = "uartist";
    private static final String KEY_TITLE = "utitle";
    private static final String KEY_EMPTY = "";
    private EditText etname;
    private EditText etnum;
    private EditText ettile;
    private TextView etext;
    private EditText etartist;
    private EditText etfull;
    private String uname;
    private String unum;
    private String utitle;
    private String uartist;
    private String ufull;
    private String umy;
    private ProgressDialog pDialog;
    private String register_url = "https://sindupotha.me/request_song/request_song.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request_song);

        etext = (TextView) findViewById(R.id.textView);
        etname = (EditText) findViewById(R.id.ename);
        etnum = (EditText) findViewById(R.id.etnumber);
        ettile = (EditText) findViewById(R.id.ettitle);
        etartist = (EditText) findViewById(R.id.artistname);
        etfull = (EditText) findViewById(R.id.etfull);



        Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/malithiweb.ttf");
        etext.setTypeface(tf2);

        Button register = (Button) findViewById(R.id.btnRegister);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                uname = etname.getText().toString().toLowerCase().trim();
                unum = etnum.getText().toString().trim();
                utitle = ettile.getText().toString().trim();
                uartist = etartist.getText().toString().trim();
                ufull = etfull.getText().toString().trim();

                if (validateInputs()) {
                    registerUser();
                }

            }
        });

    }

    /**
     * Display Progress bar
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(Feedback.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }


    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

    }

    private void registerUser() {
        displayLoader();


        StringRequest sr = new StringRequest(Request.Method.POST, register_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                umy=response.toString();

               if(umy== umy){
                   Toast.makeText(getApplicationContext(),
                                      response.toString(), Toast.LENGTH_SHORT).show();
                   loadDashboard();
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               VolleyLog.d("Error: " + error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_NAME, uname);
                params.put(KEY_NUM, unum);
                params.put(KEY_TITLE, utitle);
                params.put(KEY_ARTIST, uartist);
                params.put(KEY_FULL, ufull);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);


    }


        /**
         * Validates inputs and shows error if any
         * @return
         */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(uname)) {
            etname.setError("ඔබේ නම ඇතුලත් කරන්න");
            etname.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(unum)) {
            etnum.setError("ඔබේ දුරකථන අංකය ඇතුලත් කරන්න");
            etnum.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(utitle)) {
            ettile.setError("ගීතයේ මාතෘකාව ඇතුලත් කරන්න");
            ettile.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(uartist)) {
            etartist.setError("ගායකයාගේ නම ඇතුලත් කරන්න");
            etartist.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(ufull)) {
            etfull.setError("ගීතය ඇතුලත් කරන්න");
            etfull.requestFocus();
            return false;
        }

        int mykey =ufull.length();
        if(mykey< 130){

            etfull.setError("කරුණාකර සම්පුර්ණ ගීතය සිංහලෙන් ඇතුලත් කරන්න");
            etfull.requestFocus();
            return false;
        }

        return true;
    }
}