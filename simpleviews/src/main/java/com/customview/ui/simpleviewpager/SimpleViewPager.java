package com.customview.ui.simpleviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.customview.ui.R;

/**
 * Created by Muharrem Uğurelli on 15.02.2017.
 * SimpleViewPager
 * To easily switch between containers
 * Swipe Right and Left
 * Programmatically switch by Next and Previous methods
 * If Swipe Left(goNext) on last then SimpleViewPager switch to first containers
 * If Swipe Right(goPrevious) on first then SimpleViewPager switch to last containers
 * Customize style For Active PageDot and Deactive PageDosts
 * * Radius and Color
 */
public class SimpleViewPager extends RelativeLayout {

    // region protected Members
    protected int mContentWidth, mContentHeight;
    protected Canvas mCanvasPager;
    protected BitmapDrawable mDrawable;
    protected Bitmap mBitmap;
    private boolean mNew = true, mPressed, mContinuousMode = true;
    protected float mTouchStartX;

    // endregion protected Members

    // region private Members
    private int b;
    private View mActiveView, mPagerView, mNextView, mPreviousView;
    private int mActiveChildIndex = 0;
    private int mDotColor, mDotColorActive, mSwipeAnimationDuration, mSwipeSensitivity;
    private PageDotsAlignment mPageDotsAlignment;
    private int mPageDotsMargin;
    private int mDotRadius, mActivePageDotsRadius;
    private int mDotsSeparate;
    private boolean mShowPageDots;
    private OnPageChangingListener mOnPageChangingListener;
    private OnPageChangedListener mOnPageChangedListener;
    // endregion private Members

    //region Contractors
    public SimpleViewPager(Context context) {
        super(context);
        init(null, 0);

    }

    public SimpleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SimpleViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    //endregion Contractors

    //region Events

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initChild(l, t, r, b);
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvasPager = new Canvas(mBitmap);
            mDrawable = new BitmapDrawable(getResources(), mBitmap);
            mPagerView.setBackground(mDrawable);
        }
        if (getShowPageDots()) {
            drawPageDots();
        }
    }

    //region TouchListener
    OnTouchListener _OnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = event.getX();
                    mPressed = true;
                    initContainersRelations();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mPressed) {
                        if (mTouchStartX > event.getX()) {
                            mActiveView.setX(0 - Math.abs(mTouchStartX - event.getX()));
                            if (getChildCount() > 2) {
                                mNextView.setX(getWidth() - Math.abs(mTouchStartX - event.getX()));
                            }
                        } else if (mTouchStartX < event.getX()) {
                            mActiveView.setX(0 + Math.abs(mTouchStartX - event.getX()));
                            if (getChildCount() > 2) {
                                mPreviousView.setX((-1 * getWidth()) + Math.abs(mTouchStartX - event.getX()));
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(event.getX() - mTouchStartX) > getSwipeSensitivity()) {
                        if (mTouchStartX > event.getX()) {
                            goNext();
                        } else if (mTouchStartX < event.getX()) {
                            goPrevious();
                        }
                    } else {
                        if (mTouchStartX > event.getX()) {
                            cancelNext();
                        } else if (mTouchStartX < event.getX()) {
                            cancelPrevious();
                        }
                    }
                    mPressed = false;
                    break;
            }
            return true;
        }
    };
    //endregion TouchListener

    //endregion Events

    //region Methods

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        try {
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.SimpleViewPager, defStyle, 0);

            mDotColor = a.getColor(R.styleable.SimpleViewPager_dotColor, Color.BLACK);
            mDotColorActive = a.getColor(R.styleable.SimpleViewPager_dotColorActive, Color.WHITE);
            mSwipeAnimationDuration = a.getInt(R.styleable.SimpleViewPager_swipeAnimationDuration, 300);
            mSwipeSensitivity = a.getInt(R.styleable.SimpleViewPager_swipeSensitivity, 300);
            mPageDotsAlignment = PageDotsAlignment.values()[(a.getInt(R.styleable.SimpleViewPager_pageDotsAlignment, 1))];
            mPageDotsMargin = (int) a.getDimension(R.styleable.SimpleViewPager_pageDotsMargin, 100);
            mDotRadius = (int) a.getDimension(R.styleable.SimpleViewPager_pageDotsRadius, 20);
            mActivePageDotsRadius = (int) a.getDimension(R.styleable.SimpleViewPager_activePageDotsRadius, mDotRadius);
            mDotsSeparate = (int) a.getDimension(R.styleable.SimpleViewPager_dotsSeperate, 10);
            mShowPageDots = a.getBoolean(R.styleable.SimpleViewPager_showPageDots, true);
            a.recycle();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setOnTouchListener(_OnTouchListener);
        mPagerView = new View(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        mPagerView.setLayoutParams(layoutParams);
        addView(mPagerView);
        mPagerView.bringToFront();

    }

    private void initChild(int l, int t, int r, int b) {
        int clientCount = getChildCount();
        mPagerView.bringToFront();
        mActiveView = getChildAt(mActiveChildIndex);
        for (int i = 0; i < clientCount; i++) {
            View child = getChildAt(i);
            if (child == mPagerView) {
                child.setX(0);
                continue;
            }
            if (mNew) {
                child.setX(child == mActiveView ? 0 : getWidth());
            }
        }
        mNew = false;

    }

    public void next() {
        initContainersRelations();
        goNext();
    }

    public void previous() {
        initContainersRelations();
        goPrevious();
    }

    private void initContainersRelations() {
        mNextView = mActiveChildIndex + 1 <= getChildCount() - 2 ? getChildAt(mActiveChildIndex + 1) : getChildAt(0);
        mNextView.setX(getWidth());
        mPreviousView = mActiveChildIndex - 1 >= 0 ? getChildAt(mActiveChildIndex - 1) : getChildAt(getChildCount() - 2);
        mPreviousView.setX(getWidth() * -1);

    }

    private void goNext() {
        try {
            //Only One Container Control
            if (getChildCount() == 2) {
                cancelNext();
                return;
            }
            //ContinuousMod Control
            if (!isContinuousMode() && mActiveChildIndex + 1 > getChildCount() - 2) {
                cancelNext();
                return;
            }
            int oldActiveViewIndex = mActiveChildIndex;
            int newViewIndex = mActiveChildIndex + 1 <= getChildCount() - 2 ? mActiveChildIndex + 1 : 0;
            if (getOnPageChangingListener() != null && getOnPageChangingListener().onPageChanging(mActiveChildIndex, newViewIndex)) {
                cancelNext();
            } else {
                mActiveChildIndex = newViewIndex;
                mNextView.animate().translationX(0).setDuration(getSwipeAnimationDuration()).start();
                mActiveView.animate().translationX(-1 * getWidth()).setDuration(getSwipeAnimationDuration()).start();
                requestLayout();
                if (getOnPageChangedListener() != null) {
                    getOnPageChangedListener().onPageChanged(oldActiveViewIndex, mActiveChildIndex);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goPrevious() {
        try {
            //Only One Container Control
            if (getChildCount() == 2) {
                cancelNext();
                return;
            }
            if (!isContinuousMode() && mActiveChildIndex - 1 < 0) {
                cancelPrevious();
                return;
            }

            int oldActiveViewIndex = mActiveChildIndex;
            int newViewIndex = mActiveChildIndex - 1 >= 0 ? mActiveChildIndex - 1 : getChildCount() - 2;
            if (getOnPageChangingListener() != null && getOnPageChangingListener().onPageChanging(mActiveChildIndex, newViewIndex)) {
                cancelPrevious();
            } else {

                mActiveChildIndex = newViewIndex;
                mPreviousView.animate().translationX(0).setDuration(getSwipeAnimationDuration()).start();
                mActiveView.animate().translationX(getWidth()).setDuration(getSwipeAnimationDuration()).start();
                requestLayout();
                if (getOnPageChangedListener() != null) {
                    getOnPageChangedListener().onPageChanged(oldActiveViewIndex, mActiveChildIndex);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cancelNext() {
        mNextView.animate().translationX(getWidth()).setDuration(getSwipeAnimationDuration()).start();
        mActiveView.animate().translationX(0).setDuration(getSwipeAnimationDuration()).start();
        requestLayout();
    }

    public void cancelPrevious() {
        mPreviousView.animate().translationX(-1 * (getWidth())).setDuration(getSwipeAnimationDuration()).start();
        mActiveView.animate().translationX(0).setDuration(getSwipeAnimationDuration()).start();
        requestLayout();
    }

    public void addContainer(View view) {
        addView(view);
        mNew = true;
        requestLayout();
    }

    private void drawPageDots() {
        try {

            mContentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            mContentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            int clientCount = getChildCount() - 1;
            mCanvasPager.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            int dotX = (mContentWidth / 2) - (((clientCount * (mDotRadius * 2)) + ((clientCount - 1) * (10))) / 2);
            for (int i = 0; i < clientCount; i++) {
                Paint paint = new Paint();
                paint.setColor(getDotColor());
                if (i == mActiveChildIndex) {
                    paint.setColor(getDotColorActive());
                }
                int top = 0;
                switch (getPageDotsAlignment()) {
                    case BOTTOM:
                        top = mContentHeight - getPageDotsMargin();
                        break;
                    case TOP:
                        top = getPageDotsMargin();
                        break;
                }
                mCanvasPager.drawCircle(dotX + (mDotRadius * 2 * i) + (i * getDotsSeparate()) + mDotRadius, top,
                        (i == mActiveChildIndex ? mActivePageDotsRadius : mDotRadius), paint);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion Methods

    //region Getters & Setters
    public int getDotColor() {
        return mDotColor;
    }

    public void setDotColor(int dotColor) {
        this.mDotColor = dotColor;
        requestLayout();
    }

    public int getDotColorActive() {
        return mDotColorActive;
    }

    public void setActiveDotColor(int dotColorActive) {
        this.mDotColorActive = dotColorActive;
        requestLayout();
    }

    public int getSwipeAnimationDuration() {
        return mSwipeAnimationDuration;
    }

    public void setSwipeAnimationDuration(int swipeAnimationDuration) {
        this.mSwipeAnimationDuration = swipeAnimationDuration;
    }

    public int getSwipeSensitivity() {
        return mSwipeSensitivity;
    }

    public void setSwipeSensitivity(int swipeSensitivity) {
        this.mSwipeSensitivity = swipeSensitivity;
    }

    public PageDotsAlignment getPageDotsAlignment() {
        return mPageDotsAlignment;
    }

    public void setPageDotsAlignment(PageDotsAlignment pageDotsAlignment) {
        this.mPageDotsAlignment = pageDotsAlignment;
        requestLayout();
    }

    public int getPageDotsMargin() {
        return mPageDotsMargin;
    }

    public void setPageDotsMargin(int pageDotsMargin) {
        this.mPageDotsMargin = pageDotsMargin;
        requestLayout();
    }

    public boolean getShowPageDots() {
        return mShowPageDots;
    }

    public void setShowPageDots(boolean showPageDots) {
        this.mShowPageDots = showPageDots;
    }

    public int getDotsSeparate() {
        return mDotsSeparate;
    }

    public void setDotsSeparate(int dotsSeparate) {
        this.mDotsSeparate = dotsSeparate;
    }

    public int getmActiveChildIndex() {
        return mActiveChildIndex;
    }

    public void setmActiveChildIndex(int activeChildIndex) {
        this.mActiveChildIndex = activeChildIndex;
    }

    public OnPageChangingListener getOnPageChangingListener() {
        return mOnPageChangingListener;
    }

    public void setOnPageChangingListener(OnPageChangingListener onPageChangingListener) {
        this.mOnPageChangingListener = onPageChangingListener;
    }

    public OnPageChangedListener getOnPageChangedListener() {
        return mOnPageChangedListener;
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.mOnPageChangedListener = onPageChangedListener;
    }

    public boolean isContinuousMode() {
        return mContinuousMode;
    }

    public void setContinuousMode(boolean continuousMode) {
        this.mContinuousMode = continuousMode;
    }

    public void setDotRadius(int dotRadius)
    {
        mDotRadius =dotRadius;
        drawPageDots();
    }

    public  int getDotRadius()
    {
        return  mDotRadius;
    }

    public void setActiveDotRadius(int activeDotRadius)
    {
        mActivePageDotsRadius =activeDotRadius;
        drawPageDots();
    }

    public  int getmActiveDotsRadius()
    {
        return  mActivePageDotsRadius;
    }



    //endregion Getters & Setters
}
