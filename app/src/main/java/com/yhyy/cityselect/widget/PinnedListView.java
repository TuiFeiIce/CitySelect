package com.yhyy.cityselect.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.yhyy.cityselect.inter.PinnedAdapter;

public class PinnedListView extends ListView {
	private static final int MAX_ALPHA = 255;
	private PinnedAdapter pinnedAdapter;
	private View mPinnedView;
	private boolean mPinnedViewVisible;
	private int mPinnedViewWidth;
	private int mPinnedViewHeight;

	public PinnedListView(Context context) {
		super(context);
	}

	public PinnedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PinnedListView(Context context, AttributeSet attrs,
                                int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mPinnedView != null) {
			mPinnedView.layout(0, 0, mPinnedViewWidth, mPinnedViewHeight);
			configureHeaderView(getFirstVisiblePosition());
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mPinnedView != null) {
			measureChild(mPinnedView, widthMeasureSpec, heightMeasureSpec);
			mPinnedViewWidth = mPinnedView.getMeasuredWidth();
			mPinnedViewHeight = mPinnedView.getMeasuredHeight();
		}
	}

	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		pinnedAdapter = (PinnedAdapter) adapter;
		setListViewHeightBasedOnChildren(this);
	}

	public void configureHeaderView(int position) {
		if (mPinnedView == null) {
			return;
		}
		if (position == 0) {
			return;
		}
		int state = pinnedAdapter.getPinnedState(position);
		switch (state) {
		case PinnedAdapter.PINNED_HEADER_GONE: {
			mPinnedViewVisible = false;
			break;
		}

		case PinnedAdapter.PINNED_HEADER_VISIBLE: {
			pinnedAdapter.configurePinned(mPinnedView, position, MAX_ALPHA);
			if (mPinnedView.getTop() != 0) {
				mPinnedView.layout(0, 0, mPinnedViewWidth, mPinnedViewHeight);
			}
			mPinnedViewVisible = true;
			break;
		}

		case PinnedAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();
			int headerHeight = mPinnedView.getHeight();
			int y;
			int alpha;
			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}
			pinnedAdapter.configurePinned(mPinnedView, position, alpha);
			if (mPinnedView.getTop() != y) {
				mPinnedView.layout(0, y, mPinnedViewWidth, mPinnedViewHeight + y);
			}
			mPinnedViewVisible = true;
			break;
		}
		}
	}

	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mPinnedViewVisible) {
			drawChild(canvas, mPinnedView, getDrawingTime());
		}
	}

	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}