package com.test.beziercurve.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lisheng on 2018/4/13.
 */
public class WaveView extends View {

    private static final String TAG = WaveView.class.getSimpleName();
    private Context mContext;
    private int halfWidthStep = 0;
    private List<Point> mPoints = new ArrayList<>();

    public WaveView(Context context) {
        super(context);
        initUI(context ,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI(context ,attrs);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(context ,attrs);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initUI(context ,attrs);
    }

    private void initUI(Context context, AttributeSet set) {
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPoints == null || mPoints.size() == 0) {
            Log.e(TAG , "============>please set Point !");
            return;
        }
        if (halfWidthStep == 0) {
            Log.e(TAG , "============>please set halfWidthStep value !");
            return;
        }
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        Path mPath = new Path();
        canvas.save();
        mPath.moveTo(mPoints.get(0).x, mPoints.get(0).y);
        for (int index = 0; index < mPoints.size() - 1; index++) {
            mPath.cubicTo(mPoints.get(index).x + halfWidthStep, mPoints.get(index).y, mPoints.get(index+1).x - halfWidthStep, mPoints.get(index+1).y, mPoints.get(index+1).x, mPoints.get(index+1).y);
        }
        mPath.lineTo(mPoints.get(mPoints.size() - 1).x , getHeight());
        mPath.lineTo(mPoints.get(0).x , getHeight());
        mPath.lineTo(mPoints.get(0).x , mPoints.get(0).y);
        canvas.drawPath(mPath, paint);
        canvas.restore();
    }

    public void setPoints(List<Point> points) {
        mPoints.clear();
        invalidate();
        if (points == null || points.isEmpty()) {
            return;
        }
        mPoints.addAll(points);
        invalidate();
    }

    public List<Point> getPoints() {
        return mPoints;
    }

    public void setHalfWidthStep(int halfWidthStep) {
        this.halfWidthStep = halfWidthStep;
    }
}
