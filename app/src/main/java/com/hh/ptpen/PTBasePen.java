package com.hh.ptpen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by chrisw on 2018/3/20.
 */

abstract class PTBasePen {

    enum PTPenType{
        standard,
        mark
    }
    protected Paint mPaint;
    public Paint getPaint() {
        return mPaint;
    }

    protected int mPaintWidth = 30;
    protected int mPenColor = Color.BLACK;

    public PTBasePen(Context context, Canvas cacheCanvas){
        initPaint();
    }

    protected void initPaint(){
        //init mPaint
        mPaint = new Paint();
        mPaint.setColor(mPenColor);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//结束的笔画为圆心
        mPaint.setStrokeJoin(Paint.Join.ROUND);//连接处
        mPaint.setAlpha(0xFF);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeMiter(1.0f);
    }

    abstract boolean onTouchEvent(MotionEvent event);

    abstract void onDraw(Canvas canvas);

    abstract void reset();
}
