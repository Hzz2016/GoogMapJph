package com.example.mapdemo;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.mapdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap googleMap;
    private MapView mapView;

    public GoogleMapFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView=inflater.inflate(R.layout.fragment_google_map, container,false);

        mapView= (MapView)mView.findViewById(R.id.google_map);
        mapView.onCreate(savedInstanceState);

        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }
}
