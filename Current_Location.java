package com.example.aswin.metumetu;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Current_Location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker myMarker;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public GPSTracker gpsTracker;
    public double longitude;
    public double latitude;
    private List<Marker> originMarkers = new ArrayList<>();
    Bundle b;
    String[] retrieve_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current__location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        b=this.getIntent().getExtras();
        retrieve_data=b.getStringArray("value");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (!checkPermission()) {

            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Current_Location.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Current_Location.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (mMap != null) {

            mMap.getUiSettings().setAllGesturesEnabled(true);
            gpsTracker = new GPSTracker(getApplicationContext());

            if (gpsTracker.canGetLocation()) {

                longitude = gpsTracker.getLongitude();
                latitude = gpsTracker.getLatitude();
                LatLng hcmus = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title("location")
                        .position(hcmus)));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    Toast.makeText(getApplicationContext(), "Enable the Apps permission", Toast.LENGTH_LONG).show();
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return;
                }
                mMap.setMyLocationEnabled(true);

            }
        }
        int i=0;
        for ( i = 7; i < retrieve_data.length; i = i + 9) {
            Double lat = Double.parseDouble(retrieve_data[i]);
            Double lon = Double.parseDouble(retrieve_data[i + 1]);
            LatLng sydney = new LatLng(lat, lon);
          myMarker= mMap.addMarker(new MarkerOptions().position(sydney).title("current location").snippet("Name: "+retrieve_data[i-7]+" Mobile No: "+retrieve_data[i-5]+" Mail_address: "+retrieve_data[i-2]+" Linked-In: "+retrieve_data[i-3]));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String smsNumber = "91"+retrieve_data[2];
                Toast.makeText(getApplicationContext(),myMarker.getSnippet()+" ",Toast.LENGTH_SHORT).show();
                boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
                if (isWhatsappInstalled) {

                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

                    startActivity(sendIntent);
                } else {
                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    Toast.makeText(Current_Location.this, "WhatsApp not Installed",
                            Toast.LENGTH_SHORT).show();
                    startActivity(goToMarket);
                }
            }
        });
        }
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
