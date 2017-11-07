package com.example.aswin.metumetu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Log_In extends AppCompatActivity {

    EditText mail_id;
    EditText password;
    Button getstart;
    String check="";
    Button button;
    String user_password="";
    String user_mail_address="";
    String retrive_data_From_Database[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);

        mail_id=(EditText) findViewById(R.id.editText2);
        password=(EditText) findViewById(R.id.editText5);
        user_mail_address=mail_id.getText().toString();
        user_password=password.getText().toString();

        button=(Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               send_data();
                retrive_data_From_Database=retrive_data();
            }
        });


    }
    public String send_data(){

//DATABASE CONNECTIVITY PART STARTED
        String url ="https://superacute-incentiv.000webhostapp.com/user_validation.php";
        RequestQueue queue= Volley.newRequestQueue(Log_In.this);
// Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
Toast.makeText(getApplicationContext(),response.trim()+" ",Toast.LENGTH_SHORT).show();
                        String str=response.trim();
                        check=str;
                        if(check.equals("true")) {
                            Intent intent = new Intent(Log_In.this, Current_Location.class);
                            Bundle b = new Bundle();
                            b.putStringArray("value", retrive_data_From_Database);
                            intent.putExtras(b);
                            startActivity(intent);
                            // Display the first 500 characters of the response string.
                        }else{
                            Toast.makeText(getApplicationContext(),"user name password incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String,String>();
                params.put("mail_address",mail_id.getText().toString());
                params.put("password",password.getText().toString());
                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        MyVolley.getInstance(Log_In.this).addToRequestQueue(stringRequest);
        return check;
    }

    public String[] retrive_data(){


//DATABASE CONNECTIVITY PART STARTED
        String url ="https://superacute-incentiv.000webhostapp.com/select.php";
        RequestQueue queue= Volley.newRequestQueue(Log_In.this);
// Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str=response;
                        str=str.replaceAll("\\[","");
                        str=str.replaceAll("]","");
                        str=str.replaceAll("\"", "");


                        retrive_data_From_Database=str.split(",");
     }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        MyVolley.getInstance(Log_In.this).addToRequestQueue(stringRequest);


        return retrive_data_From_Database;
    }


}
