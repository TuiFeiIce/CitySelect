package com.yhyy.cityselect.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yhyy.cityselect.R;

public class EmptyLayout extends LinearLayout implements
        View.OnClickListener {
    public static final int CONTENT = 1; // 有内容
    public static final int LOADING = 2; // 加载中
    public static final int HIDE = 3; // 隐藏
    private int status = LOADING;
    private OnClickListener listener;
    private boolean clickEnable = true;
    private TextView textView;
    public ImageView imageView;
    public LinearLayout linearLayout;
    private ProgressBar progressBar;

    public EmptyLayout(Context context) {
        super(context);
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.control_empty, null);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        imageView = (ImageView) view.findViewById(R.id.iv_content);
        textView = (TextView) view.findViewById(R.id.tv_content);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        setBackgroundColor(-1);
        setOnClickListener(this);
        setContentType(LOADING);

        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            }
        });
        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        if (clickEnable && listener != null) {
            listener.onClick(v);
        }
    }

    public void dismiss() {
        status = CONTENT;
        setVisibility(View.GONE);
    }

    public void setContentMessage(String msg) {
        textView.setText(msg);
    }

    public void setContentImage(int img) {
        imageView.setImageResource(img);
    }

    public void setContentClick(boolean click) {
        clickEnable = click;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setContentType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case CONTENT:
                status = CONTENT;
                textView.setText("");
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.mipmap.ic_launcher_round);
                progressBar.setVisibility(View.GONE);
                clickEnable = true;
                break;
            case LOADING:
                status = LOADING;
                textView.setText("");
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                clickEnable = false;
                break;
            case HIDE:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            status = CONTENT;
        }
        super.setVisibility(visibility);
    }
}
