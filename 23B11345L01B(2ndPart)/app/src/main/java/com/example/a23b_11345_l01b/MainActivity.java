package com.example.a23b_11345_l01b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.a23b_11345_l01b.Interfaces.StepCallback;
import com.example.a23b_11345_l01b.Utilities.StepDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_BTN = "KEY_BTN";
    private  boolean play_with_motion ;
    private  final static int REQUEST_CODE = 100;
    private final int y_DIVIDER = 12;
    private final int LANES = 3;
    private final int MARG_RIGHT = 40;
    private final int EXP_MARG_LEFT = 45;
    private final int EXP_MARG_top = 40;
    private final int NUM_OF_OBSTACLES = 5;
    private final int DELAY = 500;
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

    private double lat;
    private double lon;

    private MediaPlayer mediaPlayer;
    private StepDetector stepDetector;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private VibManager vibratorManager;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            refreshUI();
        }
    };
    private Handler collision_handler = new Handler();
    private Runnable collision_runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            collisionUI();
        }
    };

    private Handler score_handler = new Handler();
    private Runnable score_runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, ONE_SEC);
            scoreUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent previousIntent = getIntent();
        play_with_motion = (previousIntent.getIntExtra(KEY_BTN, 0) == 1)? true : false;


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        vibratorManager = VibManager.getInstance(this);
        findViews();


        gameManager = new GameManager(main_IMG_hearts.length, NUM_OF_OBSTACLES);

        Glide.with(this).load("https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/17b8ffc08b8a20b.png").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);

        set_def_pos();

        handler.postDelayed(runnable,DELAY);
        collision_handler.postDelayed(collision_runnable, DELAY);
        score_handler.postDelayed(score_runnable, ONE_SEC);

        setAnswersClickListeners();


        //from debugging
        openScoreScreen(1000);
    }

    private void set_def_pos() {

        main_IMG_car.setX(gameManager.getUser().get_x_pos() * step_size_x + MARG_RIGHT);
        main_IMG_explosion.setY(gameManager.getExplosion().get_y_pos() * step_size_y);

        for(int i=0; i<NUM_OF_OBSTACLES; i++){
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

    private void phone_motion(boolean right_motion) {

        if (right_motion) {
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
                //for(int i =0; i<main_IMG_hearts.length; i++)
                //                    main_IMG_hearts[i].setAlpha(1f);
            }
            else
                // end the app
                openScoreScreen(gameManager.getScore());
        } else {
            // update

            //each obs
            for(int i=0; i<gameManager.getObstacles().length; i++) {

                int i_y_pos = gameManager.getObstacles()[i].get_y_pos();
                if (i_y_pos > 12) {
                    //obs reach to bottom
                    gameManager.getObstacles()[i].reset_pos();
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

        handler.removeCallbacks(score_runnable);
        handler.removeCallbacks(collision_runnable);
        handler.removeCallbacks(runnable);

        getCurrentLocation();
        System.out.println("from main: after getCurrentLocation - " + lat +", " + lon);

        Intent secondActivityIntent = new Intent(this, endgameActivity.class);
        secondActivityIntent.putExtra(endgameActivity.KEY_SCORE, score);
        secondActivityIntent.putExtra(endgameActivity.KEY_LAT, lat);
        secondActivityIntent.putExtra(endgameActivity.KEY_LON, lon);
        startActivity(secondActivityIntent);
        finish();
    }

    private void getCurrentLocation() {
        System.out.println("start get location:");
        System.out.println(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addressList = null;
                        try {
                            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lat = addressList.get(0).getLatitude();
                            lon = addressList.get(0).getLongitude();
                            System.out.println("from main in if in try: " + lat +", " + lon);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

            });

        }
        else {
            System.out.println("from main else:");
            ActivityCompat.requestPermissions(MainActivity.this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            System.out.println("after: ActivityCompat.requestPermissions");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("request code :  " + requestCode);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                System.out.println("now calling getCurrentLocation again!");
                getCurrentLocation();
            }
            else{
                Toast.makeText(this,"Request Permission", Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void collisionUI(){
        int cur_car_x = gameManager.getUser().get_x_pos();
        int cur_car_y = gameManager.getUser().get_y_pos();

        for(int i=0; i<gameManager.getObstacles().length; i++) {

            if (gameManager.getObstacles()[i].get_x_pos() == cur_car_x
                    && cur_car_y - gameManager.getObstacles()[i].get_y_pos() < 5
                    && cur_car_y - gameManager.getObstacles()[i].get_y_pos() > 2) {

                if (gameManager.getUser().get_got_hit()) {
                    //ignore collision this time
                    gameManager.getUser().set_got_hit(false);
                    gameManager.getExplosion().hide_explosion();
                    main_IMG_explosion.setY(gameManager.getExplosion().get_y_pos() * step_size_y);
                } else {
                    //detect collision
                    System.out.println("got hit is true");
                    gameManager.getExplosion().set_explosion_pos(cur_car_x, cur_car_y);
                    main_IMG_explosion.setX(gameManager.getExplosion().get_x_pos() * step_size_x + MARG_RIGHT - EXP_MARG_LEFT);
                    main_IMG_explosion.setY(main_IMG_car.getY() - EXP_MARG_top);
                    gameManager.getUser().set_got_hit(true);

                    gameManager.setLife(gameManager.getLife() - 1);
                    if(gameManager.getLife() != 0)
                        main_IMG_hearts[gameManager.getLife()].setAlpha(0.2f);

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


//    private void openScoreScreen(String status, int score) {
//        Intent scoreIntent = new Intent(this, ScoreActivity.class);
//        scoreIntent.putExtra(ScoreActivity.KEY_SCORE, score);
//        scoreIntent.putExtra(ScoreActivity.KEY_STATUS, status);
//        startActivity(scoreIntent);
//        finish();
//    }


    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_IMG_car = findViewById(R.id.main_IMG_car);
        main_IMG_explosion = findViewById(R.id.main_IMG_explosion);
        main_IMG_obstacles = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_obstacle1),
                findViewById(R.id.main_IMG_obstacle2),
                findViewById(R.id.main_IMG_obstacle3),
                findViewById(R.id.main_IMG_obstacle4),
                findViewById(R.id.main_IMG_obstacle5)};
        step_size_x = (int) (Resources.getSystem().getDisplayMetrics().widthPixels/LANES);
        step_size_y = (int) (Resources.getSystem().getDisplayMetrics().heightPixels/y_DIVIDER);

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
            public void tilted_to_right() {
                phone_motion(true);
            }

            @Override
            public void tilted_to_left() {
                phone_motion(false);
            }

        });

    }

}