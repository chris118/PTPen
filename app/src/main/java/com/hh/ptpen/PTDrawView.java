package com.hh.ptpen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chrisw on 2018/3/20.
 */

public class PTDrawView extends View {
    private static final String TAG = PTDrawView.class.getSimpleName();
    private Context mContext;

    //当前使用的画笔
    private PTBasePen mPen;

    //缓存画布, 不同画笔负责更新
    private Canvas cacheCanvas;
    //缓存图像 onDraw 调用
    private Bitmap cachebBitmap;
    //背景颜色
    private int mBackColor=Color.TRANSPARENT;

    PTBasePen.PTPenType penType;
    public void setPenType(PTBasePen.PTPenType penType) {
        this.penType = penType;
        switch (penType){
            case standard:
                mPen = new PTStandardPen(mContext, cacheCanvas);
                break;
            case mark:
                mPen = new PTMarkPen(mContext, cacheCanvas);
                break;
        }
    }


    public PTDrawView(Context context) {
        super(context);
        initParameters(context);
    }

    public PTDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParameters(context);
    }

    public PTDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParameters(context);
    }

    public void reset(){
        //清除缓存画布
        cacheCanvas.drawColor(mBackColor, PorterDuff.Mode.CLEAR);

        //复位画笔
        mPen.reset();

        //刷新
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画出之前的缓存的图像
        canvas.drawBitmap(cachebBitmap, 0, 0, mPen.getPaint());

        mPen.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        //画笔响应Touch Event
        mPen.onTouchEvent(event);

        // 刷新
        postInvalidate();
        return true;
    }

    private void initParameters(Context context){
        this.mContext = context;

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        cachebBitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(mBackColor);

        //设置默认画笔
        mPen = new PTStandardPen(context, cacheCanvas);
    }
}
