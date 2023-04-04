package com.example.a23b_11345_l01b;

public class Explosion {


    private static final int explosion_WIDTH = 50;

    private static final int explosion_HEIGHT = 50;

    int x_pos;

    int y_pos;

    public Explosion(){
        super();
        this.y_pos = 5;
    }

    public void hide_explosion() {
        this.x_pos = 0;
        this.y_pos = -5;
    }

    public void set_explosion_pos(int x, int y) {
        this.x_pos = x;
        this.y_pos = y;
    }

}
