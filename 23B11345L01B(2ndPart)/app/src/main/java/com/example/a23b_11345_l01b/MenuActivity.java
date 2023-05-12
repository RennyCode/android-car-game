package com.example.a23b_11345_l01b;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import android.content.Intent;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;


public class MenuActivity extends AppCompatActivity {

    private  final static int REQUEST_CODE = 100;
    private double lat;
    private double lon;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AppCompatImageView main_IMG_background;
    private MaterialButton[] menu_BTN_options;
    private ToggleButton menu_TOGGLE;

    private ToggleButton menu_SPEED;
    private int isToggled;

    private int game_speed;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
        isToggled = 0;
        game_speed = 0; // game def normal
        findViews();
        Glide.with(this).load("https://steamuserimages-a.akamaihd.net/ugc/1750182972141229603/784CC6399FCE0644808E556B86067604922924FA/?imw=512&&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false").centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
        setAnswersClickListeners();
    }
    private void setAnswersClickListeners() {
        for (MaterialButton mb : menu_BTN_options) {
            mb.setOnClickListener(v -> clicked(mb.getText().toString()));
        }
        menu_TOGGLE.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonView.setBackground(getDrawable(R.color.blue_200));
                isToggled = 1;
            }
            else {
                buttonView.setBackground(getDrawable(R.color.purple_200));
                isToggled = 0;
            }
        });
        menu_SPEED.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonView.setBackground(getDrawable(R.color.blue_200));
                game_speed = 1;
            }
            else {
                buttonView.setBackground(getDrawable(R.color.purple_200));
                game_speed = 0;
            }
        });

    }
    private void clicked(String button_name){
            if (button_name.compareTo("Play") == 0) {
                // start game , pass flag for btn or movement
                Intent secondActivityIntent = new Intent(this, MainActivity.class);
                secondActivityIntent.putExtra(MainActivity.KEY_BTN, isToggled);
                secondActivityIntent.putExtra(MainActivity.KEY_SPEED, game_speed);
                secondActivityIntent.putExtra(MainActivity.KEY_LAT, lat);
                secondActivityIntent.putExtra(MainActivity.KEY_LON, lon);
                startActivity(secondActivityIntent);
                finish();
            } else{
                // go straight to score board
                Intent secondActivityIntent = new Intent(this, endgameActivity.class);
                startActivity(secondActivityIntent);
                finish();
        }
    }
    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        menu_BTN_options = new MaterialButton[]{
                findViewById(R.id.play_button),
                findViewById(R.id.score_button)};
        menu_TOGGLE = findViewById(R.id.toggleButton);
        menu_SPEED = findViewById(R.id.speed_toggle);
    }
    public void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Geocoder geocoder = new Geocoder(MenuActivity.this, Locale.getDefault());
                        List<Address> addressList;
                        try {
                            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lat = addressList.get(0).getLatitude();
                            lon = addressList.get(0).getLongitude();
                            System.out.println("in try: " + lat +", " + lon);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    else{
                        //def location
                        System.out.println("got null location");
                        lat = 0;
                        lon = 0;
                    }
                }
            });
        }
        else {
            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
            else{
                Toast.makeText(this,"Request Permission", Toast.LENGTH_SHORT).show();
                System.out.println("Still no permission...");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
