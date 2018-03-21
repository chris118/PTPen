package com.hh.ptpen;

/**
 * Created by Zahid.Ali on 3/24/2015.
 */
public class PTPoint {
    public final float x;
    public final float y;
    public final long time;

    public PTPoint(float x, float y, long time) {
        this.x = x;
        this.y = y;
        this.time = time;
    }

    private float distanceTo(PTPoint start) {
        return (float) (Math.sqrt(Math.pow((x - start.x), 2) + Math.pow((y - start.y), 2)));
    }

    public float velocityFrom(PTPoint start) {
        return distanceTo(start) / (this.time - start.time);
    }
}
