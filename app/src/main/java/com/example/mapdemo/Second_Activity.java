package com.example.mapdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.mapdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class Second_Activity extends Activity implements OnMapReadyCallback{
    public GoogleMap mMap;
    private TextView text_title;
    private Context context = Second_Activity.this;
    private EditText et_addressName;
    private Button btn_search;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_);

        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.frame_containt, mapFragment).commit();
        mapFragment.getMapAsync(this);

        text_title = (TextView) findViewById(R.id.text_title);
        et_addressName = (EditText) findViewById(R.id.et_addressName);

        //代码实现地理编码,有时地址无法解析，并且解析很慢
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressName = et_addressName.getText().toString().trim();
                if(addressName.equals("")){
                    Toast.makeText(context,"error---Enter address first",Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE );
                inputMethodManager.hideSoftInputFromWindow(et_addressName.getWindowToken(),0);

                et_addressName.setText("");
                et_addressName.clearFocus();

                List<Address>list=null;
                if(addressName.equals("")||addressName!=null){
                    Geocoder geocoder=new Geocoder(context);
                    try {
                        list=geocoder.getFromLocationName(addressName,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(list.get(0)!=null){
                        Address address=list.get(0);
                        latLng=new LatLng(address.getLatitude(),address.getLongitude());

                        Marker destination = mMap.addMarker(new MarkerOptions().position(latLng).title("destination"));
                        if(destination.isInfoWindowShown()){
                            destination.hideInfoWindow();
                        }else{
                            destination.showInfoWindow();
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
                    }else{
                        Log.d("error","addresslist[0]==null");
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;


        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {

            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.common_full_open_on_phone);
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title("this test。。。"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

                CircleOptions circleOptions=new CircleOptions();
                circleOptions.strokeColor(Color.GRAY);
                circleOptions.fillColor(Color.GRAY);
                circleOptions.strokeWidth(2);
                circleOptions.center(latLng);
                circleOptions.radius(200);
                mMap.addCircle(circleOptions);
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
               Log.i("jiapeihui","onCameraMove");
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                Geocoder geocoder=new Geocoder(context);
                text_title.setText(cameraPosition.toString());
            }
        });

        MarkerOptions markerOptions=new MarkerOptions();

        LatLng latLng=new LatLng(-33.867,151.206);
//      LatLng latLng=new LatLng(31.214566954482,121.617050623806);
//      LatLng latLng=new LatLng(31.25430117853825,121.64912626147272);

        getAddressFrom(latLng);

    }


    /**
     * 反地理编码
     * @param latLng
     */
    public void getAddressFrom(final LatLng latLng){
        final String[] address = {null};

        //反地理编码操作
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                Geocoder geocoder = new Geocoder(context);
                List<Address> fromLocation = null;
                try {
                    fromLocation = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    address[0] =fromLocation.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return address[0];
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                text_title.setText(s);
            }
        }.execute();
    }

    //测试代码
    public void getContext(){
        mMap.setMyLocationEnabled(true);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = manager.getLastKnownLocation(manager.NETWORK_PROVIDER);
        if(location!=null){
            Log.d("test","error");
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        double longitude=location.getLongitude();
        double latitude=location.getLatitude();

        LatLng latLng=new LatLng(latitude,longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
    }
}
