package com.jc.gamerocker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jc.gamerocker.view.RockerView;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "ivtest";
    TextView t;
    boolean a = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rocker_layout);
        t = (TextView) findViewById(R.id.textview);
        RockerView r = (RockerView) findViewById(R.id.rocker);
        r.setDirListener(new RockerView.onDirTouchListener() {
            @Override
            public void up() {
                a = true;
                Log.i(TAG, "up");
                t.setText("up");
            }

            @Override
            public void right() {
                a = true;
                Log.i(TAG, "right");
                t.setText("right");
            }

            @Override
            public void left() {
                a = true;
                Log.i(TAG, "left");
                t.setText("left");
            }

            @Override
            public void down() {
                a = true;
                Log.i(TAG, "down");
                t.setText("down");

            }

            @Override
            public void stop() {
                if(a){
                    t.setText("stop");
                    Log.i(TAG, "stop");
                    a = false;
                }
            }
        });

    }
}
