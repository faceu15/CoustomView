package com.yiwu.coustomview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.yiwu.coustomview.R;
import com.yiwu.coustomview.util.DisplayUtil;

/**
 * @Author:Administrator
 * @Date: Created in 19:48 2019/9/23
 * @Description:
 */
public class LevelView extends View {

    protected Context context;

    private int level;
    //图片的id
    private int starId;
    private int moonId;
    private int sunId;
    private int crownId;

    private int drawableHeight;

    private Paint bitmapPaint;
    //
    private Bitmap starBitmap;
    private Bitmap moonBitmap;
    private Bitmap sunBitmap;
    private Bitmap crownBitmap;

    public LevelView(Context context) {
        super(context);
    }

    public LevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        //获取自定义属性样式列表
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LevelView);
        level = typedArray.getInt(R.styleable.LevelView_level, 1);

        starId = typedArray.getResourceId(R.styleable.LevelView_drawableStar, R.drawable.star);
        moonId = typedArray.getResourceId(R.styleable.LevelView_drawableMoon, R.drawable.moon);
        sunId = typedArray.getResourceId(R.styleable.LevelView_drawableSun, R.drawable.sun);
        crownId = typedArray.getResourceId(R.styleable.LevelView_drawableCrown, R.drawable.crown);

        starBitmap = BitmapFactory.decodeResource(context.getResources(), starId);
        moonBitmap = BitmapFactory.decodeResource(context.getResources(), moonId);
        sunBitmap = BitmapFactory.decodeResource(context.getResources(), sunId);
        crownBitmap = BitmapFactory.decodeResource(context.getResources(), crownId);

        drawableHeight = typedArray.getInt(R.styleable.LevelView_drawableHeight, 32);

        starBitmap = setImgSize(starBitmap, DisplayUtil.dp2px(context, drawableHeight), DisplayUtil.dp2px(context, drawableHeight));
        moonBitmap = setImgSize(moonBitmap, DisplayUtil.dp2px(context, drawableHeight), DisplayUtil.dp2px(context, drawableHeight));
        sunBitmap = setImgSize(sunBitmap, DisplayUtil.dp2px(context, drawableHeight), DisplayUtil.dp2px(context, drawableHeight));
        crownBitmap = setImgSize(crownBitmap, DisplayUtil.dp2px(context, drawableHeight), DisplayUtil.dp2px(context, drawableHeight));

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        typedArray.recycle();

    }

    public LevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Bitmap setImgSize(Bitmap bitmap, int newWidth, int newHeight) {
        //获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //计算缩放比例
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;

        //获得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(starBitmap, 0, 0, width, height, matrix, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getSize(widthMeasureSpec, drawableHeight);
        int height = getSize(heightMeasureSpec, drawableHeight);

        int viewWidth = starBitmap.getWidth() * level + starBitmap.getWidth() / 3 * (level - 1) + getPaddingLeft() + getPaddingEnd();
        int viewHeight = starBitmap.getHeight() + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int bitmapPadding = starBitmap.getWidth() / 3;
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int starNum = level % 4;
        int moonNum = (level % 64) % 16 / 4;
        int sunNum = (level % 64) / 16;
        int crownNum = level / 64;

        int nextLeft = 0;

        for (int i = 0; i < crownNum; i++) {
            Log.d("DDDD", "draw crown" + i);
            canvas.drawBitmap(crownBitmap, (crownBitmap.getWidth() + bitmapPadding) * i + left, top, bitmapPaint);
        }
        nextLeft = (crownBitmap.getWidth() + bitmapPadding) * crownNum + left;

        for (int j = 0; j < sunNum; j++) {
            Log.d("DDDD", "draw sun" + j);
            canvas.drawBitmap(sunBitmap, nextLeft + (sunBitmap.getWidth() + bitmapPadding) * j, top, bitmapPaint);
        }
        nextLeft += (sunBitmap.getWidth() + bitmapPadding) * sunNum;

        for (int m = 0; m < moonNum; m++) {
            Log.d("DDDD", "draw moon " + m);
            canvas.drawBitmap(moonBitmap, nextLeft + (moonBitmap.getWidth() + bitmapPadding) * m, top, bitmapPaint);
        }
        nextLeft += (moonBitmap.getWidth() + bitmapPadding) * moonNum;

        for (int n = 0; n < starNum; n++) {
            Log.d("DDDD", "draw star " + n);
            canvas.drawBitmap(starBitmap, nextLeft + (moonBitmap.getWidth() + bitmapPadding) * n, top, bitmapPaint);
        }

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        requestLayout();
    }

    private int getSize(int measureSpec, int defaultSize) {
        int result = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                result = size;
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
        }
        return result;
    }
}
