package com.yhyy.cityselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yhyy.cityselect.R;
import com.yhyy.cityselect.bean.CityBean;
import com.yhyy.cityselect.inter.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Hot extends RecyclerView.Adapter {
    public OnItemClickListener onItemClickListener;
    public LayoutInflater layoutInflater;
    private Context context;
    private List<CityBean> hotList;

    public Adapter_Hot(Context context, List<CityBean> datas) {
        this.context = context;
        this.hotList = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotHolder(layoutInflater.inflate(R.layout.recy_hot, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotHolder) {
            HotHolder hotHolder = (HotHolder) holder;
            hotHolder.tvItemHot.setText(hotList.get(position).getCity_name());
            hotHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(hotHolder.itemView, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return hotList.size();
    }

    class HotHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_hot)
        TextView tvItemHot;

        public HotHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}