package com.mycoderepo.mycoderepo.fabbylistview;

import android.content.Context;
import android.graphics.Rect;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by JockYuan on 2016/9/8.
 */
public class FabbyListView extends ListView {
    private static final float PIXELS_SCROLL_TO_CANCEL_EXPANSION = 100;
    private float mDownXValue;
    private float mDownYValue;
    private FlabbyLayout mDownView;
    private FlabbyLayout mDownBelowView;
    private int mChildCount;
    private int[] mListViewCoords;
    private Rect mRect = new Rect();
    private View mTrackedChild;
    private int mTrackedChildPrevTop;
    private int mTrackedChildPrevPosition;
    private float OldDeltaY;

    public FabbyListView(Context context) {
        super(context);
        init(context);
    }

    public FabbyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FabbyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnScrollListener(mScrollListener);
    }

    public interface ListViewObserverDelegate {
        void onListScroll(View view, float deltaY);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mTrackedChild == null) {
            if (getChildCount() > 0) {
                mTrackedChild = getChildInTheMiddle();
                mTrackedChildPrevTop = mTrackedChild.getTop();
                mTrackedChildPrevPosition = getPositionForView(mTrackedChild);
            }
        } else {

            boolean childIsSafeToTrack = mTrackedChild.getParent() == this
                    && getPositionForView(mTrackedChild) == mTrackedChildPrevPosition;
            if (childIsSafeToTrack) {
                int top = mTrackedChild.getTop();
                float deltaY = top - mTrackedChildPrevPosition;

                if (deltaY == 0) {
                    deltaY = OldDeltaY;
                } else {
                    OldDeltaY = deltaY;
                }

                updateChildrenControlPoints(deltaY);
                mTrackedChildPrevTop = top;
            } else {
                mTrackedChild = null;
            }
        }
    }

    private View getChildInTheMiddle() {
        return getChildAt(getChildCount() / 2);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                sendDownViewEvent(ev);
                sendBelowDownViewEvent(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void sendBelowDownViewEvent(MotionEvent ev) {
        if (mDownBelowView != null) {
            mDownBelowView.onTouchEvent(ev);
        }
    }

    private void sendDownViewEvent(MotionEvent ev) {
        if (mDownView != null) {
            mDownView.onTouchEvent(ev);
        }
    }

    private void actionMove(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        float OffsetX = mDownXValue - currentX;
        float OffsetY = mDownYValue - currentY;
        if (Math.abs(OffsetX) > Math.abs(OffsetY)) {
            sendDownViewEvent(ev);
            sendBelowDownViewEvent(ev);
        } else if (Math.abs(OffsetY) > PIXELS_SCROLL_TO_CANCEL_EXPANSION) {
            sendDownViewEvent(ev);
            sendBelowDownViewEvent(ev);
        }
    }

    private void actionDown(MotionEvent ev) {
        mDownXValue = ev.getX();
        mDownYValue = ev.getY();
        setDownView(ev);
        sendDownViewEvent(ev);
        sendBelowDownViewEvent(ev);
    }

    public void setDownView(MotionEvent ev) {
        mChildCount = getChildCount();
        mListViewCoords = new int[2];
        getLocationOnScreen(mListViewCoords);
        int x = (int) ev.getRawX() - mListViewCoords[0];
        int y = (int) ev.getRawY() - mListViewCoords[1];
        FlabbyLayout child;
        for (int i = 0; i < mChildCount; i++) {
            child = (FlabbyLayout) getChildAt(i);
            child.getHitRect(mRect);
            if (mRect.contains(x, y)) {
                mDownView = child;
                if (mDownView != null) {
                    mDownView.setAsSelected(true);
                }
                mDownBelowView = (FlabbyLayout) getChildAt(i + 1);
            } else {
                child.setAsSelected(false);
            }
        }
    }

    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                updateChildrenControlPoints(0);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    private void updateChildrenControlPoints(float deltaY) {
        View child;
        FlabbyLayout flabbyChild;
        for (int i = 0; i < getLastVisiblePosition() - getFirstVisiblePosition(); i++) {
            child = getChildAt(i);
            if (child instanceof FlabbyLayout) {
                flabbyChild = (FlabbyLayout) child;
                if (child != null) {
                    flabbyChild.updateControlPoints(deltaY);
                }
            }
        }
    }
}
