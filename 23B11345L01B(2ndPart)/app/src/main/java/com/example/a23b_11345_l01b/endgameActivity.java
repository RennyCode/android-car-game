package com.example.a23b_11345_l01b;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.a23b_11345_l01b.Fragments.ListFragment;
import com.example.a23b_11345_l01b.Fragments.MapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class endgameActivity extends AppCompatActivity{

    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String KEY_LAT = "KEY_LAT";
    public static final String KEY_LON = "KEY_LON";
    private int latestScore;
    private double lat;
    private double lon;
    private ListFragment listFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        Intent previousIntent = getIntent();
        latestScore = previousIntent.getIntExtra(KEY_SCORE, -1);
        lat = previousIntent.getIntExtra(KEY_LAT, 200);
        lon = previousIntent.getIntExtra(KEY_LON, 200);

        System.out.println("score from endgameActivity: " + latestScore);
        initFragments();
        beginTransactions();

    }
    private void initFragments() {
        listFragment = new ListFragment();
        mapFragment = new MapFragment();
    }
    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
    }

    public MapFragment getMapFragment(){
        return mapFragment;
    }

    public int getLatestScore() {return latestScore;}

    public double getLat() {return lat;}

    public double getLon() {return lon;}

    public void to_Menu() {
        Intent secondActivityIntent = new Intent(this, MenuActivity.class);
        startActivity(secondActivityIntent);
        finish();
    }
}