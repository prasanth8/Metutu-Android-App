package com.example.aswin.metumetu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.util.HashMap;
import java.util.Map;

public class Sign_Up extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    CircleMenu circleMenu;
    EditText name;
    EditText mobileno;
    EditText linked_in;
    EditText language;
    EditText password;
    Button next;
    Button login;
    String mail_address="";
    String checker="";
    DataBaseConnectivity DB;
    String retrive_data_From_Database[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        //circle menu part open

        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.plus, R.drawable.x);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.drawable.t);
        circleMenu.addSubMenu(Color.parseColor("#30A400"), R.drawable.l);

        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

                                                 @Override
                                                 public void onMenuSelected(int index) {
                                                     switch (index) {
                                                         case 0:
                                                            checker="Teacher";
                                                             break;
                                                         case 1:
                                                             checker="Learner";
                                                             break;

                                                     }
                                                 }
                                             }

        );



        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

                                                     @Override
                                                     public void onMenuOpened() {
                                                         Toast.makeText(Sign_Up.this, "Menu Opend", Toast.LENGTH_SHORT).show();
                                                     }

                                                     @Override
                                                     public void onMenuClosed() {
                                                         Toast.makeText(Sign_Up.this, "Menu Closed", Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
        );

//circle menu part closed


        //data work field start
        name=(EditText) findViewById(R.id.editText);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
            }
        });
        mobileno=(EditText) findViewById(R.id.editText3);
        mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileno.setText("");
            }
        });
        linked_in=(EditText) findViewById(R.id.editText4);
        linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linked_in.setText("");
            }
        });
        language=(EditText) findViewById(R.id.editText7);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.setText("");
            }
        });
        password=(EditText) findViewById(R.id.editText6);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });


        next=(Button) findViewById(R.id.button3);
        login=(Button) findViewById(R.id.button2);

        //data work field end













//CHECKING PART STARTED
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString()!=null){
                    if((mobileno.getText().toString()).length()==10){
                        if((linked_in.getText().toString())!=null){
                            if((language.getText().toString())!=null){
                            if((password.getText().toString()).length()>=6){
                                if((mail_address.length())!=0) {
                                    if (checker.length() > 0) {
                                        Toast.makeText(getApplicationContext(),"Sakthi",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Sign_Up.this, Current_Location.class);
                                    startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Please Choose the option",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Please Check The Google Sign-In Button",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Password Length Not Satisfied,Give Atleast 6 Characters",Toast.LENGTH_SHORT).show();
                            }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Please Enter The Valid Language",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please Enter The Valid LinkedIn-Id",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please Enter The Valid Mobile Number",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Enter The Name",Toast.LENGTH_SHORT).show();
                }
            }
        });
next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        send_data();
retrive_data_From_Database=retrive_data();

    }
});
//CHECKING PART ENDED
//LOGIN PAGE STARTED

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sign_Up.this,Log_In.class);
                startActivity(intent);
            }
        });
        //LOGIN PAGE ENDED






        //Google SignIn Part Start
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mail_address+=(acct.getEmail());
        }




    }

    public void send_data(){
        final GPSTracker getlocation =new GPSTracker(getApplicationContext());



//DATABASE CONNECTIVITY PART STARTED
        String url ="https://superacute-incentiv.000webhostapp.com/database_metumetu.php";
        RequestQueue queue=Volley.newRequestQueue(Sign_Up.this);
// Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String,String>();
                params.put("name",name.getText().toString());
                params.put("password",password.getText().toString());
                params.put("mobileno",mobileno.getText().toString());
                params.put("linked_in",linked_in.getText().toString());
                params.put("language",language.getText().toString());
                params.put("mail_address",mail_address);
                params.put("checker",checker);
                params.put("latitude",String.valueOf(getlocation.getLatitude()));
                params.put("longtitude",String.valueOf(getlocation.getLongitude()));
                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        MyVolley.getInstance(Sign_Up.this).addToRequestQueue(stringRequest);

    }


public String[] retrive_data(){
    final GPSTracker getlocation =new GPSTracker(getApplicationContext());



//DATABASE CONNECTIVITY PART STARTED
    String url ="https://superacute-incentiv.000webhostapp.com/select.php";
    RequestQueue queue=Volley.newRequestQueue(Sign_Up.this);
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

                    Intent intent=new Intent(Sign_Up.this,Current_Location.class);
                    Bundle b=new Bundle();
                    b.putStringArray("value",retrive_data_From_Database);
                    intent.putExtras(b);
                    startActivity(intent);
                    // Display the first 500 characters of the response string.
         }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
// Add the request to the RequestQueue.
    queue.add(stringRequest);
    MyVolley.getInstance(Sign_Up.this).addToRequestQueue(stringRequest);
return retrive_data_From_Database;
}



    @Override
    public void onBackPressed() {
        if (circleMenu.isOpened())
            circleMenu.closeMenu();
        else
            finish();
    }
}
