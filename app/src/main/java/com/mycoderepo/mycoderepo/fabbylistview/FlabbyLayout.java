package com.mycoderepo.mycoderepo.fabbylistview;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by win8 on 2016/9/8.
 */
public class FlabbyLayout extends FrameLayout {
    private Path mPath;
    private Paint mPaint;
    private Rect mRect;
    private float mDeltaY = 0;
    private float mCurvature;
    private int mWidth;
    private int mHeight;
    private int mOneFifthWidth;
    private int mFourFifthWidth;


    public FlabbyLayout(Context context) {
        super(context);
    }

    public FlabbyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlabbyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setAsSelected(boolean b) {
    }

    public void updateControlPoints(float deltaY) {
    }
}
