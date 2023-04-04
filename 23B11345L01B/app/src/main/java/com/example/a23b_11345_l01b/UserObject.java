package com.example.a23b_11345_l01b;

import java.util.Random;

public class UserObject {

    private static final int USER_WIDTH = 80;
    private static final int USER_HEIGHT = 80;
    int x_pos;
    int y_pos;

    boolean got_hit;
    public UserObject(){
        super();
        reset_pos();
    }
    public void reset_pos(){
        this.x_pos = 1;
        this.y_pos = 12;
        this.got_hit = false;
    }

    public int get_user_width() {
        return USER_WIDTH;
    }

    public int get_user_height() {
        return USER_HEIGHT;
    }

    public int get_x_pos() {
        return x_pos;
    }

    public int get_y_pos() {
        return y_pos;
    }

    public void set_x_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    public void set_y_pos(int y_pos) {
        this.y_pos = y_pos;
    }


}


