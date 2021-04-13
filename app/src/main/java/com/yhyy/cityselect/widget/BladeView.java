package com.yhyy.cityselect.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.yhyy.cityselect.R;

public class BladeView extends View {
    private OnItemClickListener mOnItemClickListener;
    private String[] letterArray = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z", ""};
    private int choose = -1;
    private Paint paint = new Paint();
    boolean showBkg = false;

    public BladeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BladeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BladeView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.WHITE);
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / letterArray.length;
        for (int i = 0; i < letterArray.length; i++) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.blue));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            paint.setTextSize(singleHeight - 10); //随机减2
            if (i == choose) {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.blue));
            }
            float xPos = width / 2 - paint.measureText(letterArray[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letterArray[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final int c = (int) (y / getHeight() * letterArray.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c) {
                    if (c > 0 && c < letterArray.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c > 0 && c < letterArray.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(letterArray[item]);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

}
