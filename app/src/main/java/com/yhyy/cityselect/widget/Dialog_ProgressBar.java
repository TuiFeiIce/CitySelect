package com.yhyy.cityselect.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yhyy.cityselect.R;

/**
 * Created by Wolf on 2017/7/15.
 */

public class Dialog_ProgressBar extends Dialog {
    public Dialog_ProgressBar(@NonNull Context context) {
        super(context, R.style.CustomDialogT);
    }

    public Dialog_ProgressBar(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Dialog_ProgressBar(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progressbar);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }
}
