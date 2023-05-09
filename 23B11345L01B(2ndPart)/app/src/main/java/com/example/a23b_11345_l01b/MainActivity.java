package com.example.a23b_11345_l01b;




import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import android.os.Handler;
import android.view.View;
import com.bumptech.glide.Glide;
import com.example.a23b_11345_l01b.Interfaces.StepCallback;
import com.example.a23b_11345_l01b.Utilities.StepDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
public class MainActivity extends AppCompatActivity {
    public static final String KEY_BTN = "KEY_BTN";
    public static final String KEY_LAT = "KEY_LAT";
    public static final String KEY_LON = "KEY_LON";
    public static final String KEY_SPEED = "KEY_SPEED";
    private  boolean play_with_motion ;
    private  boolean fast_game;
    private final int y_DIVIDER = 12;
    private final int LANES = 5;
    private final int FUEL1_INDEX = 2;
    private final int FUEL2_INDEX = 6;
    private final int MARG_RIGHT = 70;
    private final int EXP_MARG_LEFT = 35;
    private final int EXP_MARG_top = 30;
    private final int NUM_OF_OBSTACLES = 7;
    private final int NUM_OF_FUELS = 2;
    private final int DELAY = 1500;
    private final int SHORT_DELAY = 500;
    private final int ONE_SEC = 1000;
    private final boolean ENDLESSLY = false;
    private AppCompatImageView main_IMG_background;
    private MaterialTextView main_LBL_score;
    private MaterialButton[] main_BTN_options;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView main_IMG_car;
    private ShapeableImageView[] main_IMG_obstacles;
    private ShapeableImageView main_IMG_explosion;
    private GameManager gameManager;
    private int step_size_x;
    private int step_size_y;
    private int delay;
    private MediaPlayer mediaPlayer;
    private double lat;
    private double lon;
    private StepDetector stepDetector;
    private VibManager vibratorManager;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            refreshUI();
        }
    };
    private final Handler collision_handler = new Handler();
    private final Runnable collision_runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            collisionUI();
        }
    };
    private final Handler score_handler = new Handler();
    private final Runnable score_runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, ONE_SEC);
            scoreUI();
        }
    };
    private Handler hit_handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent previousIntent = getIntent();
        play_with_motion = previousIntent.getIntExtra(KEY_BTN, 0) == 1;
        fast_game = previousIntent.getIntExtra(KEY_SPEED, 0) == 1;
        lat = previousIntent.getIntExtra(KEY_LAT, 0);
        lon = previousIntent.getIntExtra(KEY_LON, 0);
        vibratorManager = VibManager.getInstance(this);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length, NUM_OF_OBSTACLES, NUM_OF_FUELS);
        set_def_pos();
        Glide.with(this).load("https://steamuserimages-a.akamaihd.net/ugc/1750182972141229603/784CC6399FCE0644808E556B86067604922924FA/?imw=512&&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
        delay = DELAY;
        if(fast_game){
            delay = SHORT_DELAY;
        }
        handler.postDelayed(runnable,delay);
        collision_handler.postDelayed(collision_runnable, delay);
        score_handler.postDelayed(score_runnable, ONE_SEC);
        setAnswersClickListeners();
        main_IMG_explosion.setAlpha(1.0f);
    }
    private void set_def_pos() {
        main_IMG_car.setX(gameManager.getUser().get_x_pos() * step_size_x + MARG_RIGHT);
        gameManager.getExplosion().hide_explosion();
        main_IMG_explosion.setY(gameManager.getExplosion().get_y_pos() * step_size_y);
        gameManager.getObstacles()[FUEL1_INDEX].setFuel(true);
        gameManager.getObstacles()[FUEL2_INDEX].setFuel(true);
        for(int i=0; i<NUM_OF_OBSTACLES+NUM_OF_FUELS; i++){
            gameManager.getObstacles()[i].set_y_pos(-3*i);
            gameManager.getObstacles()[i].set_x_pos((i+3)%3);
            main_IMG_obstacles[i].setY(gameManager.getObstacles()[i].get_y_pos() * step_size_y);
            main_IMG_obstacles[i].setX(gameManager.getObstacles()[i].get_x_pos() * step_size_x + MARG_RIGHT);
        }
    }
    private void setAnswersClickListeners() {
        for (MaterialButton mb : main_BTN_options) {
            mb.setOnClickListener(v -> clicked(mb.getText().toString()));
        }
    }
    private void clicked(String button_name) {
        if (button_name.compareTo("Right") == 0) {
            if (gameManager.move_car_right()) {
                main_IMG_car.setX(gameManager.getUser().get_x_pos() * step_size_x + MARG_RIGHT);
            }
        }else {
            if(gameManager.move_car_left()){
                main_IMG_car.setX(gameManager.getUser().get_x_pos() * step_size_x + MARG_RIGHT);
            }
        }
    }
    private void refreshUI() {
        if (gameManager.isLose()) {
            //lost game
            if (ENDLESSLY) {
                //reset
                gameManager.reset_game(main_IMG_hearts.length);
                set_def_pos();
                gameManager.getExplosion().hide_explosion();
                main_IMG_explosion.setY(gameManager.getExplosion().get_y_pos() * step_size_y);
                for (ShapeableImageView main_img_heart : main_IMG_hearts)
                    main_img_heart.setAlpha(1f);
            }
            else
                // end the app
                openScoreScreen(gameManager.getScore());
        } else {
            // update each obs
            main_IMG_explosion.setY(gameManager.getExplosion().get_y_pos() * step_size_y);
            for(int i=0; i<gameManager.getObstacles().length; i++) {
                int i_y_pos = gameManager.getObstacles()[i].get_y_pos();
                if (i_y_pos > 12) {
                    //obs reach to bottom
                    gameManager.getObstacles()[i].reset_pos();
                    if(gameManager.getObstacles()[i].getIsFuel()) {
                        main_IMG_obstacles[i].setAlpha(1.0f);
                    }
                    main_IMG_obstacles[i].setY(gameManager.getObstacles()[i].get_y_pos() * step_size_y);
                    main_IMG_obstacles[i].setX(gameManager.getObstacles()[i].get_x_pos() * step_size_x + MARG_RIGHT);
                } else {
                    //move obj down
                    gameManager.getObstacles()[i].set_y_pos(i_y_pos + 1);
                    main_IMG_obstacles[i].setY(gameManager.getObstacles()[i].get_y_pos() * step_size_y);
                }
            }
        }
    }
    private void openScoreScreen(int score) {
        mediaPlayer.release();
        handler.removeCallbacks(score_runnable);
        handler.removeCallbacks(collision_runnable);
        handler.removeCallbacks(runnable);
        Intent secondActivityIntent = new Intent(this, endgameActivity.class);
        secondActivityIntent.putExtra(endgameActivity.KEY_SCORE, score);
        secondActivityIntent.putExtra(endgameActivity.KEY_LAT, lat);
        secondActivityIntent.putExtra(endgameActivity.KEY_LON, lon);
        startActivity(secondActivityIntent);
        System.out.println("From main to endgame lat = " + lat + ", lon = " + lon);
        finish();
    }
    private void collisionUI(){
        int cur_car_x = gameManager.getUser().get_x_pos();
        int cur_car_y = gameManager.getUser().get_y_pos();
        for(int i=0; i<gameManager.getObstacles().length; i++) {
            if (gameManager.getObstacles()[i].get_x_pos() == cur_car_x
                    && cur_car_y - gameManager.getObstacles()[i].get_y_pos() < 4
                    && cur_car_y - gameManager.getObstacles()[i].get_y_pos() > 1
                    && main_IMG_obstacles[i].getAlpha() != 0.0f
                    && !gameManager.getUser().get_got_hit()) {
                    //detect collision
                    if(gameManager.getObstacles()[i].getIsFuel()){
                        //fuel obj
                        if (gameManager.getLife() != 3) {
                            gameManager.setLife(gameManager.getLife() + 1);
                            main_IMG_hearts[gameManager.getLife()-1].setAlpha(1.0f);
                            main_IMG_obstacles[i].setAlpha(0.0f);
                        }
                    }
                    else{
                        // car obj
                        mediaPlayer.start();
                        gameManager.getExplosion().set_explosion_pos(cur_car_x, cur_car_y);
                        main_IMG_explosion.setX(gameManager.getExplosion().get_x_pos() * step_size_x + MARG_RIGHT - EXP_MARG_LEFT);
                        main_IMG_explosion.setY(main_IMG_car.getY() - EXP_MARG_top);
                        gameManager.getUser().set_got_hit(true);
                        hit_handler = new Handler();
                        hit_handler.postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                gameManager.getUser().set_got_hit(false);
                                gameManager.getExplosion().hide_explosion();
                                main_IMG_explosion.setY(gameManager.getExplosion().get_y_pos() * step_size_y);
                                main_IMG_car.setAlpha(1.0f);
                            }
                        }, 2 * delay);
                        gameManager.setLife(gameManager.getLife() - 1);
                        if(gameManager.getLife() != 0)
                            main_IMG_hearts[gameManager.getLife()].setAlpha(0.2f);
                        main_IMG_car.setAlpha(0.2f);
                        vibratorManager.make_vibration();
                    }
            }
        }
    }
    public void scoreUI(){
        gameManager.setScore(gameManager.getScore() + 1);
        String scr = Integer.toString(gameManager.getScore());
        if (scr.length() == 1){
            scr = "00" + scr;
        } else if (scr.length() == 2) {
            scr = "0" + scr;
        }
        else if (scr.length() > 3){
            //upper score boundary
            scr = "999";
        }
        main_LBL_score.setText(scr);
    }
    private void findViews() {
        main_IMG_explosion = findViewById(R.id.main_IMG_explosion);
        main_IMG_explosion.setAlpha(0.0f);
        mediaPlayer = MediaPlayer.create(this,R.raw.crush_sound);
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_IMG_car = findViewById(R.id.main_IMG_car);
        main_IMG_obstacles = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_obstacle1),
                findViewById(R.id.main_IMG_obstacle2),
                findViewById(R.id.main_IMG_fuel_1),
                findViewById(R.id.main_IMG_obstacle3),
                findViewById(R.id.main_IMG_obstacle4),
                findViewById(R.id.main_IMG_obstacle5),
                findViewById(R.id.main_IMG_fuel_2),
                findViewById(R.id.main_IMG_obstacle6),
                findViewById(R.id.main_IMG_obstacle7),};
        step_size_x = (Resources.getSystem().getDisplayMetrics().widthPixels - MARG_RIGHT)/LANES;
        step_size_y = Resources.getSystem().getDisplayMetrics().heightPixels/y_DIVIDER;
        main_BTN_options = new MaterialButton[]{
                findViewById(R.id.left_button),
                findViewById(R.id.right_button)};
        if(play_with_motion) {
            // act on motion,  remove buttons and init sensors
            for (MaterialButton mb : main_BTN_options) {
                mb.setVisibility(View.GONE);
                initStepDetector();
            }
        }
    }
    private void initStepDetector() {
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void tilted_to_lane(int pos) {
                gameManager.getUser().set_x_pos(pos);
                main_IMG_car.setX(gameManager.getUser().get_x_pos() * step_size_x + MARG_RIGHT);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (stepDetector != null)
            stepDetector.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        if (stepDetector != null)
            stepDetector.stop();
    }
}