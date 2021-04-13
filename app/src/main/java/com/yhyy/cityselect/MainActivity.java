package com.yhyy.cityselect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yhyy.cityselect.adapter.Adapter_Type;
import com.yhyy.cityselect.bean.ProvinceBean;
import com.yhyy.cityselect.bean.TypeBean;
import com.yhyy.cityselect.config.Contracts;
import com.yhyy.cityselect.config.EC;
import com.yhyy.cityselect.eventbus.bean.Event;
import com.yhyy.cityselect.inter.OnItemClickListener;
import com.yhyy.cityselect.widget.Dialog_ProgressBar;
import com.yhyy.cityselect.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.recy_main)
    RecyclerView recyMain;

    Adapter_Type adapterType;

    MyApplication myApplication;

    Dialog_ProgressBar dialogProgressBar;

    OptionsPickerView optionsPickerView;

    List<ProvinceBean> options1Items = new ArrayList<>();
    ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initToolBar();
        initListener();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.Act_City:
                String city = (String) event.getData();
                TypeBean typeBean = Contracts.cityList.get(1);
                typeBean.setCity(city);
                adapterType.notifyDataSetChanged();
                break;
        }
    }

    private void initListener() {
        dialogProgressBar = new Dialog_ProgressBar(context);
        recyMain.setLayoutManager(new LinearLayoutManager(context));
        adapterType = new Adapter_Type(context, Contracts.cityList);
        recyMain.setAdapter(adapterType);
        adapterType.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Integer integer) {
                switch (integer) {
                    case 0:
                        dialogProgressBar.show();
                        initProvince();
                        break;
                    case 1:
                        startActivity(new Intent(context, CityActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initProvince() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                myApplication = MyApplication.getInstance();
                if (myApplication.getIsProvince()) {
                    options1Items = myApplication.getOptions1Items();
                    options2Items = myApplication.getOptions2Items();
                    options3Items = myApplication.getOptions3Items();
                    optionsPickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            //返回的分别是三个级别的选中位置
                            String opt1tx = options1Items.size() > 0 ?
                                    options1Items.get(options1).getPickerViewText() : "";

                            String opt2tx = options2Items.size() > 0
                                    && options2Items.get(options1).size() > 0 ?
                                    options2Items.get(options1).get(options2) : "";

                            String opt3tx = options2Items.size() > 0
                                    && options3Items.get(options1).size() > 0
                                    && options3Items.get(options1).get(options2).size() > 0 ?
                                    options3Items.get(options1).get(options2).get(options3) : "";

                            String tx = opt1tx + opt2tx + opt3tx;
                            TypeBean typeBean = Contracts.cityList.get(0);
                            typeBean.setCity(tx);
                            adapterType.notifyDataSetChanged();
                        }
                    })
                            .setLayoutRes(R.layout.pickerview_custom, new CustomListener() {
                                @Override
                                public void customLayout(View v) {
                                    //自定义布局中的控件初始化及事件处理
                                    TextView tvSubmit = (TextView) v.findViewById(R.id.tv_submit);
                                    TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            optionsPickerView.returnData();
                                            optionsPickerView.dismiss();
                                        }
                                    });
                                    tvCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            optionsPickerView.dismiss();
                                        }
                                    });
                                }
                            })
                            .setDividerColor(ContextCompat.getColor(context, R.color.blue))//设置选中项线条颜色
                            .setTextColorCenter(ContextCompat.getColor(context, R.color.blue)) //设置选中项文字颜色
                            .setContentTextSize(18)//滚轮文字大小
                            .setOutSideCancelable(false)//点击外部dismiss default true
                            .build();
                    optionsPickerView.setPicker(options1Items, options2Items, options3Items);//三级选择器
                    optionsPickerView.show();
                }
                dialogProgressBar.dismiss();
            }
        }, 3000);//3秒后执行Runnable中的run方法
    }

    private void initToolBar() {
    }

    private void initData() {
    }
}