package com.example.a23b_11345_l01b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class VibManager extends AppCompatActivity
{
    private final String toast_text = "Bam!";
    private final int vib_time = 500;
    private static VibManager instance = null;
    private Vibrator vib;
    private Toast toast;

    private VibManager(Context context) {
        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        toast = Toast.makeText(context, toast_text, Toast.LENGTH_SHORT);
    }

    public static VibManager getInstance(Context context) {
        if (instance == null) {
            instance = new VibManager(context);
        }
        return instance;
    }

    public void make_vibration(){
        if (vib != null) {
            vib.vibrate(VibrationEffect.createOneShot(vib_time, VibrationEffect.DEFAULT_AMPLITUDE));
            toast.show();
        }
    }


}