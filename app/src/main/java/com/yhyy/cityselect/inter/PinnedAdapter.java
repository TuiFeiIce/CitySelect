package com.yhyy.cityselect.inter;

import android.view.View;

public interface PinnedAdapter {
    int PINNED_HEADER_GONE = 0;
    int PINNED_HEADER_VISIBLE = 1;
    int PINNED_HEADER_PUSHED_UP = 2;

    int getPinnedState(int position);

    void configurePinned(View header, int position, int alpha);

}
