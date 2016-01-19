package com.eleks.tesla.mainApp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.eleks.tesla.R;

/**
 * Created by Ihor.Demedyuk on 19.02.2015.
 */
public class RadialPicker extends View {

    private int mMinValue = 16;
    private int mMaxValue = 30;
    private int mValue;
    private float mSensivity = 1f;

    private int[] mColors = new int[]{999};

    private float mValueStep;

    private Paint mRadialPaint;
    private Bitmap mSieveBitmap;
    private Bitmap mSieveScaledBitmap;

    private ValueChangeListener mValueChangeListener;

    private float yTouchDown;
    private int touchDownValue;

    public void setValuechangeListener(ValueChangeListener valueChangeListener) {
        this.mValueChangeListener = valueChangeListener;
        mValueChangeListener.onValueChanged(mValue);
    }

    public RadialPicker(Context context) {
        super(context);
        init();
    }

    public RadialPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadialPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init() {
        mRadialPaint = new Paint();
        mRadialPaint.setAntiAlias(true);

        Drawable chargingDrawable = getContext().getResources().getDrawable(R.drawable.sieve);
        mSieveBitmap = ((BitmapDrawable) chargingDrawable).getBitmap();

    }

    private void scaleSieveIfNeeded(int sideSize) {
        if (mSieveScaledBitmap == null
                || mSieveScaledBitmap.getWidth() != sideSize) {
            mSieveScaledBitmap = Bitmap.createScaledBitmap(mSieveBitmap,
                    sideSize, sideSize, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        float height = canvas.getHeight();

        int maxValueStepsCount = mMaxValue - mMinValue + 1;

        mValueStep = (height / maxValueStepsCount);

        mRadialPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL));
        updatePaintColorByValue();

        canvas.drawCircle(width / 2,
                height * 0.65f,
                width / 3.5f + width * 0.38f * (((float) (mValue) - mMinValue) / (mMaxValue - mMinValue)),
                mRadialPaint);

        scaleSieveIfNeeded(width);
        Paint p2 = new Paint();
        p2.setAntiAlias(true);
        canvas.drawBitmap(mSieveScaledBitmap, 0, 0, p2);

    }

    private void updatePaintColorByValue() {
        if (mColors.length == 1) {
            return;
        }

        int step = mValue - mMinValue;
        int stepsCount = mMaxValue - mMinValue + 1;
        int sectorsCount = mColors.length - 1;
        int stepsPerSector = stepsCount / sectorsCount;

        int currentSector = step / stepsPerSector;

        int startColor;
        int endColor;
        if (currentSector == mColors.length - 1) {
            startColor = mColors[mColors.length - 1];
            endColor = mColors[mColors.length - 1];
        } else {
            startColor = mColors[currentSector];
            endColor = mColors[currentSector + 1];
        }

        float sectorSteps = step - currentSector * stepsPerSector;

        float r1 = Color.red(startColor);
        float g1 = Color.green(startColor);
        float b1 = Color.blue(startColor);

        float r2 = Color.red(endColor);
        float g2 = Color.green(endColor);
        float b2 = Color.blue(endColor);

        float redStep = (r2 - r1) / stepsPerSector;
        float greenStep = (g2 - g1) / stepsPerSector;
        float blueStep = (b2 - b1) / stepsPerSector;


        mRadialPaint.setARGB(255,
                Math.round(r1 + redStep * sectorSteps),
                Math.round(g1 + greenStep * sectorSteps),
                Math.round(b1 + blueStep * sectorSteps));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                yTouchDown = event.getY();
                touchDownValue = mValue;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaValue = Math.round((event.getY() - yTouchDown) / mValueStep * mSensivity);
                if (deltaValue != 0 && mValue != touchDownValue - deltaValue) {
                    mValue = touchDownValue - deltaValue;
                    checkValueRange();
                    if (mValueChangeListener != null) {
                        mValueChangeListener.onValueChanged(mValue);
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void checkValueRange() {
        mValue = Math.min(mValue, mMaxValue);
        mValue = Math.max(mValue, mMinValue);
    }

    public interface ValueChangeListener {
        public void onValueChanged(int value);
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int mMinValue) {
        this.mMinValue = mMinValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int mValue) {
        this.mValue = mValue;
    }

    public int[] getColors() {
        return mColors;
    }

    public void setColors(int mColors[]) {
        this.mColors = mColors;
        if (mColors.length > 0) {
            mRadialPaint.setColor(mColors[0]);
        }
    }
}

