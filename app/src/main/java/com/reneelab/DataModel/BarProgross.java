package com.reneelab.DataModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/11/7.
 */
public class BarProgross extends ProgressBar {
    String text;
    Paint mPaint;

    public BarProgross(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initText();
    }

    public BarProgross(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initText();
    }


    public BarProgross(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        // TODO Auto-generated method stub
        setText(progress);
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //this.setText();
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        this.mPaint.setTextSize(50);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    //初始化，画笔
    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
    }

    private void setText(){
        setText(this.getProgress());
    }

    //设置文字内容
    private void setText(int progress){
        int i = (progress * 100)/this.getMax();
        this.text = String.valueOf(i) + "%";
    }
}
