package com.example.a23b_11345_l01b;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a23b_11345_l01b.Fragments.ListFragment;
import com.example.a23b_11345_l01b.Fragments.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.example.a23b_11345_l01b.Interfaces.CallBack_SendClick;

public class endgameActivity extends AppCompatActivity{

    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String KEY_LAT = "KEY_LAT";
    public static final String KEY_LON = "KEY_LON";
    private int latestScore;
    private double lat;
    private double lon;
    private ListFragment listFragment;
    private MapFragment mapFragment;

//    CallBack_SendClick callBack_SendClick = new CallBack_SendClick() {
//        @Override
//        public void userNameChosen(String name) {
//            mapFragment.zoomOnUser(name);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        Intent previousIntent = getIntent();
        latestScore = previousIntent.getIntExtra(KEY_SCORE, 0);
        lat = previousIntent.getIntExtra(KEY_LAT, 0);
        lon = previousIntent.getIntExtra(KEY_LON, 0);


        System.out.println("score from endgameActivity: " + latestScore);

        initFragments();
        beginTransactions();

    }
    private void initFragments() {
        listFragment = new ListFragment();
//        listFragment.setCallBack_SendClick(callBack_SendClick);
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
}