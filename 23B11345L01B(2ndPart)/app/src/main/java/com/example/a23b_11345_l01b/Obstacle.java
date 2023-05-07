package com.example.a23b_11345_l01b;
import android.content.Context;

import java.util.Random;

public class Obstacle {

    private int x_pos;
    private int y_pos;
    private Random random;

    private boolean isFuel;

    public Obstacle(){
        super();
        this.random = new Random();
        reset_pos();
    }
    public void reset_pos(){
        this.x_pos = random.nextInt(5);
        this.y_pos = 0;
    }

    public int get_x_pos() {
        return x_pos;
    }
    public int get_y_pos() {
        return y_pos;
    }

    public  boolean getIsFuel() { return this.isFuel; };
    public void set_x_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    public void set_y_pos(int y_pos) {
        this.y_pos = y_pos;
    }

    public void setFuel(boolean isFuel) {this.isFuel = isFuel;} ;

}


