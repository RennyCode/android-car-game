package com.example.a23b_11345_l01b;

public class PastGame {
    private int score;
    private String name;
    private double[] location;


    public PastGame(int score, String name, double[] location) {
        this.location = new double[2];
        this.location[0] = location[0];
        this.location[1] = location[1];
        this.name = name;
        this.score = score;
    }

    public void setScore(String score) {this.score =  Integer.parseInt(score);}
    public void setName(String name) {this.name = name;}
    public void setLocation(float[] location) {
        this.location[0] = location[0];
        this.location[1] = location[1];
    }

    public double[] getLocation(){return this.location;}
    public double getLatitude(){return this.location[0];}
    public double getLongitude(){return this.location[1];}
    public int getScore(){return this.score;}

    public String getName(){return this.name;}
}
