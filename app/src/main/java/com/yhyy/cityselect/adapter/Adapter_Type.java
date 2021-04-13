package com.yhyy.cityselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yhyy.cityselect.R;
import com.yhyy.cityselect.bean.TypeBean;
import com.yhyy.cityselect.inter.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Type extends RecyclerView.Adapter {
    public OnItemClickListener onItemClickListener;
    public LayoutInflater layoutInflater;
    private Context context;
    private List<TypeBean> typeList;

    public Adapter_Type(Context context, List<TypeBean> datas) {
        this.context = context;
        this.typeList = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeHolder(layoutInflater.inflate(R.layout.recy_type, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypeHolder) {
            TypeHolder typeHolder = (TypeHolder) holder;
            typeHolder.tvItemName.setText(typeList.get(position).getTitle());
            typeHolder.tvItemRight.setText(typeList.get(position).getCity());
            typeHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(typeHolder.itemView, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    class TypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_item_right)
        TextView tvItemRight;

        public TypeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}