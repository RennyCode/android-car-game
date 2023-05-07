package com.example.a23b_11345_l01b;

import android.widget.ArrayAdapter;

public class PastGame {
    private int score;
    private String date_str;
    private double[] location;


    public PastGame(int score, String date_str, double[] location) {
        this.location = new double[2];
        this.location[0] = location[0];
        this.location[1] = location[1];
        this.date_str = date_str;
        this.score = score;
    }

    public void setScore(String score) {this.score =  Integer.parseInt(score);}
    public void setDate_str(String date_str) {this.date_str = date_str;}
    public void setLocation(float[] location) {
        this.location[0] = location[0];
        this.location[1] = location[1];
    }

    public double[] getLocation(){return this.location;}
    public double getLatitude(){return this.location[0];}
    public double getLongitude(){return this.location[1];}
    public int getScore(){return this.score;}
    public String getDate_str(){return this.date_str;}


}
