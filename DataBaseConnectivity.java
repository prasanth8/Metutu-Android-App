package com.example.aswin.metumetu;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Aswin on 14-10-2017.
 */

    public class DataBaseConnectivity  {
    public void connect(final String name, final String password, final String mobileno, final String linked_in, final String language, final double latitude, final double longitude){

        String url ="https://superacute-incentiv.000webhostapp.com/update_info.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String,String>();
                params.put("user_name",name);
                params.put("pass",password);
                params.put("mobile",mobileno);
                params.put("lik_in",linked_in);
                params.put("lang",language);
                params.put("latitude",String.valueOf(latitude));
                params.put("longitude",String.valueOf(longitude));
                return super.getParams();
            }
        };
// Add the request to the RequestQueue.

    }



}