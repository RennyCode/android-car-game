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
    private ListFragment listFragment;
    private MapFragment mapFragment;

    CallBack_SendClick callBack_SendClick = new CallBack_SendClick() {
        @Override
        public void userNameChosen(String name) {
            mapFragment.zoomOnUser(name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        Intent previousIntent = getIntent();
        int score = previousIntent.getIntExtra(KEY_SCORE, 0);

        initFragments(score);
        beginTransactions();

    }
    private void initFragments(int score) {
        listFragment = new ListFragment();
        listFragment.setCallBack_SendClick(callBack_SendClick);
        mapFragment = new MapFragment();
    }
    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
    }

}