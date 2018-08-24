package com.maxence.tvseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.graphics.Matrix;

/**
 * author  : maxence
 * date    : 2018/8/24.
 * version :  v1.0
 */
@SuppressLint("AppCompatCustomView")
public class TVSeekBar extends SeekBar {

    /**
     * 文本的颜色
     */
    private int mTextColor = getResources().getColor(R.color.color_blue_seekbar);
    /**
     * 文本的大小
     */
    private float mTextSize;
    private String mText = "20:11";//文字的内容
    private Context mContext;


    private Bitmap map;  //游标图片、字体上面的颜色
    //bitmap对应的宽高
    private float img_width, img_height;

    Paint mPaint;
    private Paint.FontMetrics fm;
    private float numTextWidth;
    private Rect rect_seek;
    /**
     * 文本中轴线X坐标
     */
    private float textCenterX;
    /**
     * 文本baseline线Y坐标
     */
    private float textBaselineY;
    /**
     * 文字的方位
     */
    private int textAlign;

    public TVSeekBar(Context context) {
        this(context,null);
    }

    public TVSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TVSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mTextSize = dp2px(20);
        getImgWH();
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);//设置文字大小
        mPaint.setColor(mTextColor);//设置文字颜色
        //设置控件的padding 给提示文字留出位置
        setPadding((int) Math.ceil(img_width) / 2, 0, (int) Math.ceil(img_height) / 2, (int) Math.ceil(img_height) + 10);
    }
    /**
     * 获取图片宽高
     */
    private void getImgWH() {
        map = BitmapFactory.decodeResource(getResources(), R.mipmap.playing_icon_arow);
        img_width = map.getWidth();
        img_height = map.getHeight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect_seek = this.getProgressDrawable().getBounds();

        setTextLocation();//定位文本绘制的位置

        //定位文字背景图片的位置
        float bm_x = rect_seek.width() * getProgress() / getMax();
        float bm_y = rect_seek.height() + 2;
       //计算文字的中心位置在bitmap
        float text_x = rect_seek.width() * getProgress() / getMax() + (img_width - numTextWidth) / 2;
        Matrix matrix = new Matrix();
        //canvas.drawBitmap(map, bm_x, bm_y, paint);//画背景图
        matrix.postScale(0.5f, 0.5f);
        matrix.postTranslate(bm_x + img_width / 4, 15);
        canvas.drawBitmap(map, matrix, mPaint);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //设置锯齿 否则字体会模糊
        mPaint.setAntiAlias(true);
        canvas.drawText(mText, text_x, 50, mPaint);
    }



    /**
     * 不断更新文字的文字
     */
    private void setTextLocation() {
        fm = mPaint.getFontMetrics();
        int progress = getProgress();
        numTextWidth = mPaint.measureText(mText);
        int time=progress * 30 *60 /1000;
        String hour=time/60+"";
        String min=time%60+"";

        if(hour.length()<2){
            hour="0"+hour;
        }
        if(min.length()<2){
            min="0"+min;
        }
        mText=hour+":"+min;
    }


    public int dp2px(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
