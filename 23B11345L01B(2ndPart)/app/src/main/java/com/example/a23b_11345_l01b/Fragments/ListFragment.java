package com.example.a23b_11345_l01b.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a23b_11345_l01b.MenuActivity;
import com.example.a23b_11345_l01b.PastGame;
import com.example.a23b_11345_l01b.R;
import com.example.a23b_11345_l01b.endgameActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.example.a23b_11345_l01b.Interfaces.CallBack_SendClick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ListFragment extends Fragment{

    private final int MAX_LIST_SIZE = 10;
    private final String[] def_top_scores = { "1000", "900", "700", "500", "100", "70", "50", "40", "10"};

    private final String[] def_dates = { "Sat, 1 Apr 2023", "Wed, 3 May 2023", "Mon, 17 Apr 2023", "Sun, 1 Jan 2023", "Tue, 31 Jan 2023", "Sun, 15 Jan 2023", "Tue, 25 Apr 2023", "Fri, 19 May 2023", "Sat, 20 May 2023"};
    private final double[][] def_locations = { {32.10930909326758, 34.83664255250104}, {32.100632334113804, 34.82671798319121},{32.11496060968313, 34.81801806413817}, {32.06589509426418, 34.77705691202571}, {32.81636732919248, 35.00245043305057}, {32.71960618612468, 35.14404560635955}, {32.70845356869808, 35.1768717180803},{32.924720825872704, 35.080533609612814}, {32.609395328322584, 35.29252985656495}};
    private AppCompatEditText list_ET_name;
    private ListView l_view;
    private PastGame[]  pastGames;
    private  int latestScore;
    private double current_lat;
    private double current_lon;

    private MaterialButton back_to_menu_BTN;
    private endgameActivity end_activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        create_past_games();
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        setBTNClickListeners();
        ArrayAdapter<String> arr;

        System.out.println( "in list fragment dates: " + this.pastGames.length);
        for (int i = 0; i < this.pastGames.length; i++)
            System.out.println(this.pastGames[i].getScore());
//            System.out.println(this.pastGames[i].getDate_str());

        String[] lines = make_scores();

        arr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lines);
        l_view.setAdapter(arr);

        l_view.setOnItemClickListener((parent, view1, position, id) -> {
            MapFragment mapFragment = (MapFragment)  getParentFragmentManager().findFragmentById(R.id.main_FRAME_map) ;
            if (mapFragment != null)
                mapFragment.zoomOnUser(pastGames[position], "change!");
        });
        return view;
    }



    private void create_past_games() {

        end_activity = (endgameActivity) getActivity();

        if(end_activity != null) {
            latestScore = end_activity.getLatestScore();
            if(latestScore != 0){
                current_lat = end_activity.getLat();
                current_lon = end_activity.getLon();

                //val for debug, since not getting last game location
                if(current_lat == 200) {
                    current_lat = 32.17478966158038;
                    current_lon = 34.82443302988041;
                }
            }
        }



        double[] location = {current_lat, current_lon};
        System.out.println("latest_game Score: " + latestScore);
        System.out.println("current lat: " + current_lat);
        System.out.println("current lon: " + current_lon);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String currentDateAndTime = df.format(Calendar.getInstance().getTime());

        PastGame latest_game = new PastGame(latestScore, currentDateAndTime, location);


        SharedPreferences pref = end_activity.getSharedPreferences("def_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        System.out.println("in create past games");

        if(pref.contains("past_games")){
            // got previous data

            System.out.println("in if: got previous data");
            String json_past_games = pref.getString("past_games", "");
            Type type = new TypeToken<PastGame[]>(){}.getType();
            this.pastGames = new Gson().fromJson(json_past_games, type);

        }
        else {
            // need to create def pref
            System.out.println("in else, create def pref :" + def_top_scores.length);
            this.pastGames = new PastGame[def_top_scores.length];
            for(int i =0; i<def_top_scores.length; i++)
                this.pastGames[i] = new PastGame(Integer.parseInt(def_top_scores[i]), def_dates[i], def_locations[i]);

        }

        if (latestScore > 0 && current_lat != 200 &&  current_lon != 200) {
            // add latest game

            int index = 0;
            System.out.println("first_game Score: " + this.pastGames[index].getScore());

            while (this.pastGames[index].getScore() > latestScore) {
                index++;
                if(this.pastGames.length <= index)
                    if(MAX_LIST_SIZE == this.pastGames.length)
                        return;
                    else break;
            }

            if (MAX_LIST_SIZE >= this.pastGames.length + 1) {
                // make room for another obj
                System.out.println("make new allocation!");
                PastGame[] updated_pastGames = new PastGame[this.pastGames.length + 1];
                for (int i = 0; i < this.pastGames.length; i++)
                    updated_pastGames[i] = this.pastGames[i];
                this.pastGames = updated_pastGames;
            }
            for (int i = this.pastGames.length - 2; i >= index; i--)
                this.pastGames[i + 1] = this.pastGames[i];
            this.pastGames[index] = latest_game;

            System.out.println("out create past games");
            String json_past_games = new Gson().toJson(this.pastGames);
            editor.putString("past_games", json_past_games);
            editor.apply();
        }
    }


    private String[] make_scores() {

        String[] lines = new String[this.pastGames.length];
        for(int i =0; i<this.pastGames.length; i++)
            lines[i] = this.pastGames[i].getDate_str() + " : " + this.pastGames[i].getScore();
        return lines;
    }

    private void findViews(View view) {
        list_ET_name = view.findViewById(R.id.list_ET_name);
        l_view = view.findViewById(R.id.l_view);
        back_to_menu_BTN = view.findViewById(R.id.back_to_menu_BTN);
    }

    private void setBTNClickListeners() {
        back_to_menu_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(end_activity != null)
                    end_activity.to_Menu();
            }
        });
    }

}