package com.hh.ptpen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by chrisw on 2018/3/20.
 */

public class PTStandardPen extends PTBasePen {
    private static final String TAG = PTStandardPen.class.getSimpleName();

    private float mPreviousX = 0.0f;
    private float mPreviousY = 0.0f;
    private Path mPath = new Path();
    private Canvas mCacheCanvas;

    public PTStandardPen(Context context, Canvas cacheCanvas) {
        super(context, cacheCanvas);
        mCacheCanvas = cacheCanvas;
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
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void reset() {
        mPath.reset();
    }

    private void touchDown(MotionEvent event){
        Log.d(TAG, "touchDown");
        mPreviousX = event.getX();
        mPreviousY = event.getY();
        mPath.moveTo(mPreviousX, mPreviousY);
    }

    private void touchMove(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();
        final float dx = Math.abs(x - mPreviousX);
        final float dy = Math.abs(y - mPreviousY);
        // 两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            // 设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + mPreviousX) / 2;
            float cY = (y + mPreviousY) / 2;

            Log.d(TAG, "mPreviousX: " + String.valueOf(mPreviousX));
            Log.d(TAG, "mPreviousY: " + String.valueOf(mPreviousY));
            Log.d(TAG, "cX: " + String.valueOf(cX));
            Log.d(TAG, "cY: " + String.valueOf(cY));

            // 贝塞尔曲线: mPreviousX, mPreviousY为操作点，cX, cY为终点
            mPath.quadTo(mPreviousX, mPreviousY, cX, cY);
            // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mPreviousX = x;
            mPreviousY = y;
        }
    }

    private void touchUp(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();

        mPath.moveTo(x, y);

        // 用户完成一次绘制, 保存mCacheCanvas
        mCacheCanvas.drawPath(mPath, mPaint);
    }
}
