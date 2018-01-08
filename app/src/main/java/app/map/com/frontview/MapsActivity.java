package app.map.com.frontview;

import android.Manifest;
import android.app.*;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.provider.ContactsContract;

import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



    public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    double la, lo;
    LatLng my[];
    String name[];
    private DatabaseReference database;
    private List<String> contactCotainer = new ArrayList<String>();
    private int i = 1;
    String mobile, simname;
        protected static final int REQUEST_CHECK_SETTINGS = 0x1;
        private GoogleApiClient googleApiClient;
    Random random = new Random();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        database = FirebaseDatabase.getInstance().getReference("MapBeans");
        mapFragment.getMapAsync(this);



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        cameraUpdate();
    }

    public void geo(double la, double lo) {
        String arul = getIntent().getStringExtra("name");
        String phn = getIntent().getStringExtra("phn");

        database.child(phn).child("name").setValue(arul);
        database.child(phn).child("lat").setValue(la);
        database.child(phn).child("lon").setValue(lo);
        database.child(phn).child("ph").setValue(mobile);


        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String check = "";

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor ph = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?", new String[]{id}, null);
            while (ph.moveToNext()) {
                String phonenumber = ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String dis = ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (phonenumber.substring(0, 1).equals("9") || phonenumber.substring(0, 1).equals("8") || phonenumber.substring(0, 1).equals("7") || phonenumber.substring(0, 1).equals("+")) {
                    phonenumber = filter(phonenumber);
                    if (!check.equals(phonenumber)) {
                        check = phonenumber;
                        if (!phonenumber.substring(0, 3).equals("+91"))
                            phonenumber = "+91" + phonenumber;
                        contactCotainer.add(phonenumber);
                    }
                }
            }
        }
        int size = contactCotainer.size();

        my = new LatLng[size];
        name = new String[size];

        my[0] = new LatLng(la, lo);
        name[0] = arul;//get name from intent
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        mMap.addMarker(new MarkerOptions().position(my[0]).title(name[0]));
        builder.include(my[0]);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my[0], 14.5f));
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    MapBeans ob = data.getValue(MapBeans.class);

                    my[i] = new LatLng(ob.getLat(), ob.getLon());
                    name[i] = ob.getName();
                    i = i + 1;


                }

                mMap.clear();
                //BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.drawable.send);
                for (int j = i - 1; j >= 0; j--) {

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    mMap.addMarker(new MarkerOptions().position(my[j]).title(name[j]));
                    builder.include(my[j]);
                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my[j], 10.5f));


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                maplocation(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        public void cameraUpdate() {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            maplocation(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 9000, 0, locationListener);
        }

        public void maplocation(Location location) {
            if (location != null) {
                la = location.getLatitude();
                lo = location.getLongitude();
                geo(la, lo);
            }
        }



    // public Bitmap resizeMapIcons(String iconName,int width, int height){
    //   Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
    // Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);

    //  return resizedBitmap;
    //}//


    @Override
    protected void onStart() {
        super.onStart();
        String phn = getIntent().getStringExtra("phn");
        database.child("phn").child("check").setValue("online");

        Log.i("lifecycle", "onStart invoked");
    }


    @Override
    protected void onStop() {
        super.onStop();
        String phn = getIntent().getStringExtra("phn");
        database.child(phn).child("check").setValue("offline");


        Log.i("lifecycle", "onStop invoked");
    }

    @Override
    protected void onPause() {
        String phn = getIntent().getStringExtra("phn");
        database.child(phn).child("check").setValue("offline");
        super.onPause();

        Log.i("lifecycle", "onPause invoked");
    }


    public String filter(String filterstr) {
        String phone = "";
        String[] output = filterstr.split("\\s");
        for (int i = 0; i < output.length; i++) {
            phone += output[i];
        }
        return phone;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        Intent add_mem = new Intent(this, SecondActivity.class);
        startActivity(add_mem);


        return super.onOptionsItemSelected(item);
    }

            }







