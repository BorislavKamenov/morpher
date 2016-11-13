package com.kamenov.morphtimer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kamenov.android.morpher.Morpher;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Morpher morpherHours;
    private Morpher morpherMinutes;
    private Morpher morpherSeconds;

    private EditText etInput;
    private Button btnSet;
    private EditText etZeroes;
    private Button btnZeroes;
    private Morpher morpherInput;

    private Calendar mCalendar;
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        morpherHours = (Morpher) findViewById(R.id.morpher_hours);
        morpherMinutes = (Morpher) findViewById(R.id.morpher_minutes);
        morpherSeconds = (Morpher) findViewById(R.id.morpher_seconds);
        morpherInput = (Morpher) findViewById(R.id.morpher_input);

        btnSet = (Button) findViewById(R.id.btn_set);
        etInput = (EditText) findViewById(R.id.et_input);

        btnZeroes = (Button) findViewById(R.id.btn_set_zeroes);
        etZeroes = (EditText) findViewById(R.id.et_input_zeroes);

        mCalendar = Calendar.getInstance();
        startTimer();

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etInput.getText().toString().length() > 0) {
                    morpherInput.setNumber(Integer.valueOf(etInput.getText().toString()));
                }
            }
        });

        btnZeroes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etZeroes.getText().toString().length() > 0) {
                    morpherInput.setZeroFiller(Integer.valueOf(etZeroes.getText().toString()));
                }
            }
        });
    }

    Handler demoHandler = new Handler();

    private void startTimer() {
        demoHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCalendar.setTimeInMillis(System.currentTimeMillis());

                morpherHours.setNumber(mCalendar.get(Calendar.HOUR_OF_DAY));
                morpherMinutes.setNumber(mCalendar.get(Calendar.MINUTE));
                morpherSeconds.setNumber(mCalendar.get(Calendar.SECOND));

                demoHandler.postDelayed(this, 1000);
            }
        }, 0);
    }
}
