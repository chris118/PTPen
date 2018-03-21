package com.hh.ptpen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by chrisw on 2018/3/20.
 */

public class PTMarkPen extends PTBasePen {
    private static final String TAG = PTMarkPen.class.getSimpleName();

    private static final float VELOCITY_FILTER_WEIGHT = 0.2f;
    public static final float MAX_VELOCITY_BOUND = 15f;
    private static final float MIN_VELOCITY_BOUND = 1.6f;
    private static final float INCREMENT_CONSTANT = 0.0005f;
    private static final float DRAWING_CONSTANT = 0.0085f;
    private static final float MIN_INCREMENT = 0.01f;

    private PTPoint previousPoint, startPoint, currentPoint;
    private float lastVelocity, lastWidth;

    private Canvas mCacheCanvas;

    public PTMarkPen(Context context, Canvas cacheCanvas) {
        super(context, cacheCanvas);

        mCacheCanvas = cacheCanvas;
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        mPaint.setColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchUp(event);
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
    }

    @Override
    public void reset() {
    }

    private void touchDown(MotionEvent event){
        Log.d(TAG, "touchDown");
        final float x = event.getX();
        final float y = event.getY();

        previousPoint = null;
        startPoint = null;
        currentPoint = null;
        lastVelocity = 0;
        lastWidth = mPaintWidth;

        currentPoint = new PTPoint(x, y, System.currentTimeMillis());
        previousPoint = currentPoint;
        startPoint = previousPoint;
    }

    private void touchMove(MotionEvent event){
        Log.d(TAG, "touchMove");
        final float x = event.getX();
        final float y = event.getY();

        if (previousPoint == null) {
            return;
        }
        startPoint = previousPoint;
        previousPoint = currentPoint;
        currentPoint = new PTPoint(x, y, System.currentTimeMillis());

        float velocity = currentPoint.velocityFrom(previousPoint);
        velocity = VELOCITY_FILTER_WEIGHT * velocity + (1 - VELOCITY_FILTER_WEIGHT) * lastVelocity;

        float strokeWidth = getStrokeWidth(velocity);
        drawLine(lastWidth, strokeWidth, velocity);

        lastVelocity = velocity;
        lastWidth = strokeWidth;
    }

    private void touchUp(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();
    }

    private float getStrokeWidth(float velocity) {
        // des_velocity 需要测试出一个合适的算法 TODO
        float des_velocity = mPaintWidth < 5 ? 1 : mPaintWidth/5;
        return mPaintWidth - (velocity * des_velocity);
    }

    private PTPoint midPoint(PTPoint p1, PTPoint p2) {
        return new PTPoint((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2, (p1.time + p2.time) / 2);
    }

    private float getPt(float n1, float n2, float perc) {
        float diff = n2 - n1;
        return n1 + (diff * perc);
    }

    private void drawLine(final float lastWidth,
                          final float currentWidth,
                          final float velocity){
        final PTPoint mid1 = midPoint(previousPoint, startPoint);
        final PTPoint mid2 = midPoint(currentPoint, previousPoint);
        float xa, xb, ya, yb, x, y;
        float increment;
        if (velocity > MIN_VELOCITY_BOUND && velocity < MAX_VELOCITY_BOUND) {
            increment = DRAWING_CONSTANT - (velocity * INCREMENT_CONSTANT);
        } else {
            increment = MIN_INCREMENT;
        }

        for (float i = 0f; i < 1f; i += increment) {
            xa = getPt(mid1.x, previousPoint.x, i);
            ya = getPt(mid1.y, previousPoint.y, i);
            xb = getPt(previousPoint.x, mid2.x, i);
            yb = getPt(previousPoint.y, mid2.y, i);

            x = getPt(xa, xb, i);
            y = getPt(ya, yb, i);

            float strokeVal = lastWidth + (currentWidth - lastWidth) * (i);
            mPaint.setStrokeWidth(strokeVal);
            mCacheCanvas.drawPoint(x, y, mPaint);
        }
    }
}
