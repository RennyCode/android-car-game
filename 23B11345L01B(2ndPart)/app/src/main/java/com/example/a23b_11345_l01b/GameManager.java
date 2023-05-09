package com.example.a23b_11345_l01b;

import com.example.a23b_11345_l01b.ObjectClasses.Explosion;
import com.example.a23b_11345_l01b.ObjectClasses.Obstacle;
import com.example.a23b_11345_l01b.ObjectClasses.UserObject;

public class GameManager {
    private Explosion explosion;
    private UserObject user;
    private Obstacle[] obstacles;
    private int score;
    private int life;

    public GameManager(int life, int num_of_obstacles, int num_of_fuels) {
        this.explosion = new Explosion();
        this.user = new UserObject();
        this.obstacles = new Obstacle[num_of_obstacles + num_of_fuels];
        for(int i=0; i <obstacles.length; i++){
            obstacles[i] = new Obstacle();
        }
        reset_game(life);
    }

    public int getScore() {
        return score;
    }
    public int getLife() {
        return life;
    }
    public Explosion getExplosion() {return explosion;}
    public UserObject getUser() {return user;}
    public Obstacle[] getObstacles() {
        return obstacles;
    }
    public void setScore(int newScore) {
        this.score = newScore;
    }
    public void setLife(int newLife) {
        this.life = newLife;
    }
    public boolean isLose() {
        return life == 0;
    }
    public Boolean move_car_right() {
        if (user.get_x_pos() == 4) {
            return false;
        } else {
            user.set_x_pos(user.get_x_pos() + 1);
            return true;
        }
    }
    public Boolean move_car_left() {
        if (user.get_x_pos() == 0) {
            return false;
        } else {
            user.set_x_pos(user.get_x_pos() - 1);
            return true;
        }
    }
    public void reset_game(int life){
        this.life = life;
        this.score = 0;
        this.getExplosion().set_explosion_pos(0,5);
        this.getUser().reset_pos();
        for (int i=0; i<obstacles.length; i++){
            obstacles[i].reset_pos();
        }
    }
}