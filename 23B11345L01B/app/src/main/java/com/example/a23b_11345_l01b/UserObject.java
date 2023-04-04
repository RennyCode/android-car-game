package com.example.a23b_11345_l01b;

import java.util.Random;

public class UserObject {

    private int x_pos;
    private int y_pos;
    private boolean got_hit;
    public UserObject(){
        super();
        reset_pos();
    }
    public void reset_pos(){
        this.x_pos = 1;
        this.y_pos = 12;
        this.got_hit = false;
    }

    public int get_x_pos() {
        return x_pos;
    }
    public int get_y_pos() {
        return y_pos;
    }
    public boolean get_got_hit() {
        return got_hit;
    }
    public void set_x_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    public void set_y_pos(int y_pos) {
        this.y_pos = y_pos;
    }
    public void set_got_hit(boolean got_hit) { this.got_hit = got_hit; }

}


