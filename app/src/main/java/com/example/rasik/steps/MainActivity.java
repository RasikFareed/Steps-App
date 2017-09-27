
package com.example.rasik.steps;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener , StepListener{

    private CircularProgressBar circularProgressBar;
    private Button BtnStart,BtnStop;
    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressbar);

        textView = (TextView)findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                ImageView mImageViewFilling = (ImageView) findViewById(R.id.imageview_animation_list_filling);
                mImageViewFilling.setBackgroundResource(R.drawable.animation_list_filling);
                ((AnimationDrawable) mImageViewFilling.getBackground()).start();
                textView.setText("");
                numSteps = 0;
                circularProgressBar.setProgressWithAnimation(numSteps);
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                Toast.makeText(MainActivity.this,"Keep the phone in your pocket and Start Walking!",Toast.LENGTH_SHORT).show();

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View arg0) {
                ImageView mImageViewEmptying = (ImageView) findViewById(R.id.imageview_animation_list_filling);
                mImageViewEmptying.setBackgroundResource(R.drawable.animation_list_emptying);
                ((AnimationDrawable) mImageViewEmptying.getBackground()).start();
                sensorManager.unregisterListener(MainActivity.this);
                circularProgressBar.setProgressWithAnimation(numSteps);
                textView.setTextSize(32);
                textView.setText("Steps Count "+"\n"+numSteps);

            }
        });

 }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        textView.setTextColor(Color.parseColor("#4fb3bf"));
        textView.setTextSize(85);
        textView.setText(""+numSteps);
    }

}