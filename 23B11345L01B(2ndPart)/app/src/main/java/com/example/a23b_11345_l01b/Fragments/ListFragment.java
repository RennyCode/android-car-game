package com.example.a23b_11345_l01b.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a23b_11345_l01b.MainActivity;
import com.example.a23b_11345_l01b.PastGame;
import com.example.a23b_11345_l01b.R;
import com.example.a23b_11345_l01b.endgameActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.example.a23b_11345_l01b.Interfaces.CallBack_SendClick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ListFragment extends Fragment{

    private final int MAX_LIST_SIZE = 5;
    private final String[] def_top_scores = { "100", "70", "50", "20"};
    private final String[] def_names = { "Michael", "Toby", "Creed", "Ann"};
    private final double[][] def_locations = { {32.10930909326758, 34.83664255250104}, {32.100632334113804, 34.82671798319121},{32.11496060968313, 34.81801806413817}, {32.06589509426418, 34.77705691202571} };

    private MaterialButton list_BTN_send;
    private AppCompatEditText list_ET_name;
    private CallBack_SendClick callBack_SendClick;
    private ListView l_view;
    private PastGame[]  pastGames;

    private double current_lat;
    private double current_lon;

    public void setCallBack_SendClick(CallBack_SendClick callBack_SendClick) {
        this.callBack_SendClick = callBack_SendClick;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        create_past_games();

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        ArrayAdapter<String> arr;

        System.out.println( "in main" + this.pastGames.length);
        for (int i = 0; i < this.pastGames.length; i++)
            System.out.println(this.pastGames[i].getName());

        String[] lines = make_scores();

        arr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lines);
        l_view.setAdapter(arr);

//        arr = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
//                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
//                text1.setText(top_scores[position]);
//                text2.setText(names[position]);
//                return view;
//            }
//        };
//        l_view.setAdapter(arr);

        l_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                MapFragment mapFragment = (MapFragment)  getParentFragmentManager().findFragmentById(R.id.main_FRAME_map) ;
                mapFragment.zoomOnUser(pastGames[position], "change!");

            }
        });
//        l_view.performClick();
//
        return view;
    }



    private void create_past_games() {

        endgameActivity activity = (endgameActivity) getActivity();

        int latestScore = (int) activity.getLatestScore();
        double lat = (double) activity.getLat();
        double lon = (double) activity.getLon();
        double[] location = {lat, lon};
        System.out.println("latest_game Score: " + latestScore);
        String some_def_name = "morris";

        PastGame latest_game = new PastGame(latestScore, some_def_name, location);


        SharedPreferences pref = activity.getSharedPreferences("def_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        System.out.println("in create past games");

        if(pref.contains("past_games")){
            // got previous data

            System.out.println("in if");
            String json_past_games = pref.getString("past_games", "");
            Type type = new TypeToken<PastGame[]>(){}.getType();
            this.pastGames = new Gson().fromJson(json_past_games, type);

        }
        else {
            // need to create def pref

            System.out.println("in else :" + def_top_scores.length);

            this.pastGames = new PastGame[def_top_scores.length];

            for(int i =0; i<def_top_scores.length; i++)
                this.pastGames[i] = new PastGame(Integer.parseInt(def_top_scores[i]), def_names[i], def_locations[i]);

            System.out.println("in else 2nd:" + this.pastGames.length);

            String json_past_games = new Gson().toJson(this.pastGames);
            editor.putString("past_games", json_past_games);
            editor.apply();

        }
        int index = 0;
        System.out.println("first_game Score: " + this.pastGames[index].getScore());

        while (this.pastGames[index].getScore() > latestScore){ index++; }

        if(MAX_LIST_SIZE >= this.pastGames.length + 1) {
            // make room for another obj
            PastGame[] updated_pastGames = new PastGame[this.pastGames.length + 1];
            for(int i =0; i < this.pastGames.length; i++)
                updated_pastGames[i] = this.pastGames[i];
            this.pastGames = updated_pastGames;
        }
        for (int i = this.pastGames.length - 2; i >= index; i--)
            this.pastGames[i + 1] = this.pastGames[i];
        this.pastGames[index] = latest_game;

        System.out.println("out create past games");

    }

//    private void insertScore(int latestScore) {
//        int i = 0;
//        while (i < def_top_scores.length && latestScore > Integer.parseInt(def_top_scores[i]))
//            i++;
//        if (i == def_top_scores.length)
//            def_top_scores[]
//
//
//    }

    private String[] make_scores() {

        String[] lines = new String[this.pastGames.length];
        for(int i =0; i<this.pastGames.length; i++)
            lines[i] = this.pastGames[i].getName() + " : " + this.pastGames[i].getScore();
        return lines;
    }

    private void findViews(View view) {
        list_BTN_send = view.findViewById(R.id.list_BTN_send);
        list_ET_name = view.findViewById(R.id.list_ET_name);
        l_view = view.findViewById(R.id.l_view);
    }

    public void sendClicked() {
        if(callBack_SendClick != null)
            callBack_SendClick.userNameChosen(list_ET_name.getText().toString());
    }
}