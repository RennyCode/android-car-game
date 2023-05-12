package com.example.a23b_11345_l01b.ObjectClasses;

public class Explosion {
    private int x_pos;
    private int y_pos;
    public Explosion(){
        super();
        this.y_pos = -5;
    }
    public void hide_explosion() {
        this.x_pos = 0;
        this.y_pos = -5;
    }
    public void set_explosion_pos(int x, int y) {
        this.x_pos = x;
        this.y_pos = y;
    }
    public int get_x_pos() {return x_pos;}
    public int get_y_pos() {return y_pos;}
}
