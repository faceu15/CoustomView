package com.yiwu.coustomview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.Nullable;

import com.yiwu.coustomview.R;

/**
 * @ Author:yi wu
 * @ Date: Created in 9:36 2020/10/15
 * @ Description:
 */
public class RingGradeView extends View {

    private static final int HIGH_GRADE = 100;
    private static final int CENTER_GRADE = 60;
    private static final int LOW_GRADE = 30;


    private Pair<Integer, Integer> highLevelColor = new Pair<>(Color.parseColor("#1E4128"), Color.parseColor("#20BF71"));
    private Pair<Integer, Integer> middleLevelColor = new Pair<>(Color.parseColor("#423D0F"), Color.parseColor("#C6C82E"));
    private Pair<Integer, Integer> lowLevelColor = new Pair<>(Color.parseColor("#521534"), Color.parseColor("#CA215B"));
    private SparseArray<Pair<Integer, Integer>> mColorArray = new SparseArray<>();

    private int centerX;
    private int centerY;
    private float mViewRadius;


    private int mValue;
    private int mMaxValue;

    private float mTextWidth;

    private RectF mRingRectF;

    private float mTextSize;
    private float mRingWidth;
    private float mPaddingAround;

    private int mBackgroundColor;

    private Paint mBackgroundPaint;
    private Paint mRingPaint;
    private TextPaint mTextPaint;


    public RingGradeView(Context context) {
        super(context);
    }

    public RingGradeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingGradeView);

        mBackgroundColor = typedArray.getColor(R.styleable.RingGradeView_backgroundColor, Color.parseColor("#081C22"));

        mTextSize = typedArray.getDimension(R.styleable.RingGradeView_android_textSize, 50);
        mRingWidth = typedArray.getDimension(R.styleable.RingGradeView_ring_width, 15);
        mPaddingAround = typedArray.getDimension(R.styleable.RingGradeView_paddingAround, 25);

        mValue = typedArray.getInteger(R.styleable.RingGradeView_value, 0);
        mMaxValue = typedArray.getInteger(R.styleable.RingGradeView_maxValue, 100);
        typedArray.recycle();

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAntiAlias(true);

        mRingRectF = new RectF();
        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(mTextSize);

        mColorArray.put(LOW_GRADE, lowLevelColor);
        mColorArray.put(CENTER_GRADE, middleLevelColor);
        mColorArray.put(HIGH_GRADE, highLevelColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        centerX = width / 2;
        centerY = height / 2;
        mTextWidth = mTextPaint.measureText("60");

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            mViewRadius = mTextWidth + mPaddingAround + mRingWidth;
            setMeasuredDimension((int) mViewRadius * 2, (int) mViewRadius * 2);
        } else {
            mViewRadius = width >> 1;
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
        float ringRadius = mViewRadius - mPaddingAround;

        mRingRectF.left = centerX - ringRadius;
        mRingRectF.top = centerY - ringRadius;
        mRingRectF.right = centerX + ringRadius;
        mRingRectF.bottom = centerY + ringRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawNormalRing(canvas);
        drawValueRing(canvas);
        drawText(canvas);
    }

    private void drawBackground(Canvas canvas) {
        mBackgroundPaint.setColor(mBackgroundColor);
        canvas.drawCircle(centerX, centerY, mViewRadius, mBackgroundPaint);
    }

    private void drawNormalRing(Canvas canvas) {
        mRingPaint.setColor(getLevelColor().first);
        mRingPaint.setStrokeWidth(mRingWidth);
        canvas.drawArc(mRingRectF, 0, 360, false, mRingPaint);
    }

    private void drawValueRing(Canvas canvas) {
        mRingPaint.setColor(getLevelColor().second);
        mRingPaint.setStrokeWidth(mRingWidth);
        canvas.drawArc(mRingRectF, -90, (float) mValue * 360 / mMaxValue, false, mRingPaint);
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setColor(getContext().getColor(android.R.color.white));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setDither(true);
        mTextWidth = mTextPaint.measureText(String.valueOf(mValue));

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float offset = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(String.valueOf(mValue), centerX - mTextSize / 12, centerY + offset, mTextPaint);
        mTextPaint.setTextSize(mTextSize / 3);
        canvas.drawText("%", centerX + 10 + mTextWidth / 2, centerY - mTextPaint.getTextSize() / 4, mTextPaint);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        invalidate();
    }

    /**
     * @return 返回不同等级对应的进度条背景色和进度条的颜色
     */
    private Pair<Integer, Integer> getLevelColor() {
        for (int i = 0; i < mColorArray.size(); i++) {
            if (mValue <= mColorArray.keyAt(i)) {
                return mColorArray.valueAt(i);
            }
        }
        return highLevelColor;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        if (value > mMaxValue || value < 0) {
            throw new IllegalArgumentException("Illegal value");
        }
        mValue = value;
        invalidate();
    }

    /*
     * 设置默认等级<=100的color
     * Pair first color is progress background color,second is progress color
     *
     * */
    public void setHighLevelColor(Pair<Integer, Integer> highLevelColor) {
        this.highLevelColor = highLevelColor;
    }

    /**
     * @param middleLevelColor 设置默认等级<=60的color
     */
    public void setMiddleLevelColor(Pair<Integer, Integer> middleLevelColor) {
        this.middleLevelColor = middleLevelColor;
    }

    /**
     * @param lowLevelColor 设置默认等级<=30的color
     */
    public void setLowLevelColor(Pair<Integer, Integer> lowLevelColor) {
        this.lowLevelColor = lowLevelColor;
    }

    public SparseArray<Pair<Integer, Integer>> getColorArray() {
        return mColorArray;
    }

    /**
     * @param colorArray 设置等级不同等级的进度条颜色，key是等级的上限(等于)，value是对应等级的颜色对，SparseArray的key需要升序
     */
    public void setColorArray(SparseArray<Pair<Integer, Integer>> colorArray) {
        mColorArray = colorArray;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        invalidate();
    }
}
