package com.test.beziercurve;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.test.beziercurve.view.WaveView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private WaveView waveView;
    private Button button;
    private Button stopBtn;
    private int width = 0;
    private int count;
    private boolean isRunnable = false;

    private int baseLine = 1000;
    private int waveNumber = 6; // visible wave number
    private int waveMaxValue = 50;
    private int hideWaveNumber = 4;// hide wave number
    private int waveSpace;

    private int mHeightDelta = 15;  //height update speed
    private int mWidthDelta = 10;   //width update speed

    private static final int UPDATE_VALUE = 0x10002;
    private static final int COMPLETE_WAVE_FLAG = 0x10003;
    private static final int UPDATE_WAVE_FLAG = 0x10004;
    private static final int THREAD_SLEEP_TIME = 30;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_VALUE:
                    int value = msg.arg1;
                    if (msg.arg2 == COMPLETE_WAVE_FLAG) {
                        baseLine -= mHeightDelta;
                        waveView.setPoints(getInitPoints(baseLine, waveNumber ,waveSpace));
                    } else {
                        List<Point> pointList = waveView.getPoints();
                        for (Point point : pointList) {
                            point.x += value;
                        }
                        waveView.invalidate();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunnable) {
                count ++;
                Message message = mHandler.obtainMessage();
                message.what = UPDATE_VALUE;
                int number = waveSpace * hideWaveNumber / mWidthDelta;
                if (count <= number) {
                    message.arg1 = - mWidthDelta;
                    message.arg2 = UPDATE_WAVE_FLAG;
                } else {
                    count = 0;
                    message.arg1 = 0;
                    message.arg2 = COMPLETE_WAVE_FLAG ;
                }
                mHandler.sendMessage(message);
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getHeight();
        waveSpace = width / waveNumber;
        waveView = (WaveView) findViewById(R.id.wave_view);
        waveView.setPoints(getInitPoints(baseLine , waveNumber ,waveSpace));
        waveView.setHalfWidthStep(waveSpace / 2);
        button = (Button) findViewById(R.id.start_wave_btn);
        button.setOnClickListener(this);
        stopBtn = (Button) findViewById(R.id.stop_wave_btn);
        stopBtn.setOnClickListener(this);
    }

    private  ArrayList<Point> getInitPoints(int baseLine , int waveNumber , int mWaveSpace) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i =0 ; i < waveNumber + hideWaveNumber; i++) {
            points.add(new Point(i * mWaveSpace , i%2 == 0 ? baseLine + waveMaxValue : baseLine - waveMaxValue ));
        }
        return points;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_wave_btn:
                isRunnable = true;
                new Thread(mRunnable).start();
                break;
            case R.id.stop_wave_btn:
                isRunnable = false;
                break;
            default:
                break;
        }
    }
}

