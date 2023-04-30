package com.example.a23b_11345_l01b.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a23b_11345_l01b.PastGame;
import com.example.a23b_11345_l01b.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textview.MaterialTextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
// extends AppCompatActivity
// implements OnMapReadyCallback
public class MapFragment extends Fragment{

    private final float ZOOM = 14f;
    private MaterialTextView map_LBL_title;
    private GoogleMap map;
    private Marker mMarker;
    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded
                map = googleMap;
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });

        findViews(view);
        return view;
    }

    private void findViews(View view) {
        map_LBL_title = view.findViewById(R.id.map_LBL_title);
    }

    public void zoomOnUser(PastGame clicked_game, String name) {

        map_LBL_title.setText(name);
        System.out.println(name);
        System.out.println(clicked_game.getName() + " : " + clicked_game.getScore());

        if (map != null) {
            //latitude, longitude
            System.out.println("lat: " + clicked_game.getLatitude() + "lon: " + clicked_game.getLongitude());
            LatLng location = new LatLng(clicked_game.getLatitude(), clicked_game.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM));
            // Check if marker already exists
            if (mMarker == null) {// Create a new marker if it doesn't exist
                MarkerOptions options = new MarkerOptions()
                        .position(location).title("Flag");
                mMarker = map.addMarker(options);
            } else {
                // Move the marker if it already exists
                mMarker.setPosition(location);
            }



        }

    }


//    public void moveMapToLocation(double latitude, double longitude) {
//        if (map != null) {
//            LatLng location = new LatLng(latitude, longitude);
//            map.moveCamera(CameraUpdateFactory.newLatLng(location));
//        }
//    }


}
