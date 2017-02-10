package com.example.mapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;

import com.google.mapdemo.R;

/**
 * 嵌套Fragment到activity
 */
public class ActivityGoogleMap extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_google_map);

        Fragment mapFragment=new GoogleMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.google_frame_containt,mapFragment).commit();

    }
}
