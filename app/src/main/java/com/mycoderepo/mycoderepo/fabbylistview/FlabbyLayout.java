package com.mycoderepo.mycoderepo.fabbylistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by win8 on 2016/9/8.
 */
public class FlabbyLayout extends FrameLayout {
    private static final float MAX_CURVATURE = 100f;
    private Path mPath;
    private Paint mPaint;
    private Rect mRect;
    private float mDeltaY = 0;
    private float mCurvature;
    private int mWidth;
    private int mHeight;
    private int mOneFifthWidth;
    private int mFourFifthWidth;
    private boolean isUserTouching = false;
    private boolean isSelectedView = false;
    private float mFingerX = 0;


    public FlabbyLayout(Context context) {
        super(context);
        init(context);
    }

    public FlabbyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlabbyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getWidth();
        mHeight = getHeight();
        mOneFifthWidth = mWidth / 5;
        mFourFifthWidth = mOneFifthWidth * 4;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRect = canvas.getClipBounds();
        mRect.inset(0, -mHeight / 2);
        canvas.clipRect(mRect, Region.Op.REPLACE);

        if (!isUserTouching) {
            if (mDeltaY > -MAX_CURVATURE && mDeltaY < MAX_CURVATURE) {
                mCurvature = mDeltaY * 2;
            }
            topCellPath(mOneFifthWidth, mFourFifthWidth, mCurvature);
            bottomCellPath(mFourFifthWidth, mOneFifthWidth, mHeight + mCurvature);

        } else {
            float curvature = isSelectedView ? -mCurvature : mCurvature;
            topCellPath(mFingerX, mFingerX,curvature);
            curvature = isSelectedView ? mHeight - curvature:mHeight;
            bottomCellPath(mFingerX, mFingerX, curvature);
            
        }

        canvas.drawPath(mPath, mPaint);

    }

    private Path bottomCellPath(float x1, float x2, float curvature) {
        mPath.cubicTo(x1, curvature, x2, curvature, 0, mHeight);
        mPath.lineTo(0,0);
        return null;
    }

    private Path topCellPath(float x1, float x2, float curvature) {
        mPath.reset();
        mPath.moveTo(0,0);
        mPath.cubicTo(x1, curvature, x2, curvature, mWidth, 0);
        mPath.lineTo(mWidth, mHeight);
        return mPath;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;

        }
        return super.onTouchEvent(event);
    }

    private void actionUp(MotionEvent event) {
        isUserTouching = false;
        mCurvature = 0;
        invalidate();
    }

    private void actionMove(MotionEvent event) {
        if (mFingerX != event.getX()) {
            requestLayout();
        }
        mFingerX = event.getX();

    }

    private void actionDown(MotionEvent event) {
        mCurvature  = MAX_CURVATURE;
        mFingerX = event.getX();
        isUserTouching = true;
    }

    public void setAsSelected(boolean b) {
        isSelectedView = b;
    }

    public void updateControlPoints(float deltaY) {
        mDeltaY = deltaY;
        invalidate();
    }

    public void setFlabbyColor(int color) {
        mPaint.setColor(color);
    }
}
