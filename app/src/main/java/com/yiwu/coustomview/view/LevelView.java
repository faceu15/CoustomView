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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
        //获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int crown = level / 64;
        int sun = (level % 64) / 16;
        int moon = (level % 16) / 4;
        int star = level % 4;

        int count = crown + sun + moon + star;

        Log.d("DDDD", "onMeasure ----- crown==" + crown + " sun==" + sun + " moon==" + moon + " star==" + star);

        if (getLayoutParams().width == WRAP_CONTENT && getLayoutParams().height == WRAP_CONTENT) {
            widthSize = count * starBitmap.getWidth() + starBitmap.getWidth() / 3 * (count - 1) + getPaddingLeft() + getPaddingEnd();
            heightSize = starBitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        } else if (getLayoutParams().width == WRAP_CONTENT) {
            widthSize = count * starBitmap.getWidth() + starBitmap.getWidth() / 3 * (count - 1) + getPaddingLeft() + getPaddingEnd();
        } else if (getLayoutParams().height == WRAP_CONTENT) {
            heightSize = starBitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int bitmapPadding = starBitmap.getWidth() / 3;
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int starNum = level % 4;
        int moonNum = (level % 16) / 4;
        int sunNum = (level % 64) / 16;
        int crownNum = level / 64;

        Log.d("DDDD", " onDraw ------ crown=" + crownNum + " sun=" + sunNum + " moon=" + moonNum + " star=" + starNum);
        int nextLeft = 0;

        for (int i = 0; i < crownNum; i++) {
            canvas.drawBitmap(crownBitmap, (crownBitmap.getWidth() + bitmapPadding) * i + left, top, bitmapPaint);
        }
        nextLeft = (crownBitmap.getWidth() + bitmapPadding) * crownNum + left;

        for (int j = 0; j < sunNum; j++) {
            canvas.drawBitmap(sunBitmap, nextLeft + (sunBitmap.getWidth() + bitmapPadding) * j, top, bitmapPaint);
        }
        nextLeft += (sunBitmap.getWidth() + bitmapPadding) * sunNum;

        for (int m = 0; m < moonNum; m++) {
            canvas.drawBitmap(moonBitmap, nextLeft + (moonBitmap.getWidth() + bitmapPadding) * m, top, bitmapPaint);
        }
        nextLeft += (moonBitmap.getWidth() + bitmapPadding) * moonNum;

        for (int n = 0; n < starNum; n++) {
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
}
