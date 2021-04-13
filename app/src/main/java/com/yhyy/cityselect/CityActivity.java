package com.yhyy.cityselect;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yhyy.cityselect.adapter.Adapter_City;
import com.yhyy.cityselect.adapter.Adapter_Hot;
import com.yhyy.cityselect.bean.CityBean;
import com.yhyy.cityselect.config.EC;
import com.yhyy.cityselect.eventbus.EventBusUtil;
import com.yhyy.cityselect.eventbus.bean.Event;
import com.yhyy.cityselect.inter.OnItemClickListener;
import com.yhyy.cityselect.widget.BladeView;
import com.yhyy.cityselect.widget.EmptyLayout;
import com.yhyy.cityselect.widget.PinnedListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityActivity extends BaseActivity {
    @BindView(R.id.pinnedList)
    PinnedListView pinnedList;
    @BindView(R.id.bladeView)
    BladeView bladeView;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    Adapter_City adapterCity;
    Adapter_Hot adapterHot;

    MyApplication myApplication;

    /***定位中的提示信息*/
    TextView tv_city_location;

    List<CityBean> cityList = new ArrayList<>();
    List<CityBean> hotCityList = new ArrayList<>();

    List<String> sections = new ArrayList<>();
    Map<String, List<CityBean>> map = new HashMap<String, List<CityBean>>();
    List<Integer> positions = new ArrayList<Integer>();
    Map<String, Integer> indexer = new HashMap<String, Integer>();

    View headView;
    RecyclerView recyCityHot;
    String cityString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        initData();
        initToolBar();
        initView();
        initListener();
    }

    private void initListener() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                myApplication = MyApplication.getInstance();
                if (myApplication.getIsCity()) {
                    cityList = myApplication.getCityList();
                    hotCityList = myApplication.getHotCity();
                    sections = myApplication.getSections();
                    map = myApplication.getMap();
                    positions = myApplication.getPositions();
                    indexer = myApplication.getIndexer();
                    adapterCity = new Adapter_City(context, cityList, map, sections, positions);
                    pinnedList.setAdapter(adapterCity);
                    pinnedList.setOnScrollListener(adapterCity);
                    recyCityHot.setLayoutManager(new GridLayoutManager(context, 4));
                    adapterHot = new Adapter_Hot(context, hotCityList);
                    recyCityHot.setAdapter(adapterHot);
                    adapterHot.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, Integer integer) {
                            cityString = hotCityList.get(integer).getCity_name();
                            initEvent();
                        }
                    });
                }
                emptyLayout.dismiss();
            }
        }, 3000);//3秒后执行Runnable中的run方法

    }

    private void initView() {
        headView = getLayoutInflater().inflate(R.layout.list_city_head, null);
        recyCityHot = (RecyclerView) headView.findViewById(R.id.recy_city_hot);
        tv_city_location = (TextView) headView.findViewById(R.id.tv_city_location);
        tv_city_location.setText("北上广深");
        pinnedList.addHeaderView(headView);
        bladeView.setOnItemClickListener(new BladeView.OnItemClickListener() {

            @Override
            public void onItemClick(String s) {
                String key = s;
                if (indexer.get(key) != null) {
                    pinnedList.setSelection(indexer.get(key).intValue() + 1);
                }
            }
        });
        pinnedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityString = (adapterCity.getItem(position - 1)).getCity_name();
                initEvent();
            }
        });
        tv_city_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityString = tv_city_location.getText().toString();
                initEvent();
            }
        });
    }

    private void initEvent() {
        EventBusUtil.sendEvent(new Event(EC.EventCode.Act_City, cityString));
        finish();
    }

    private void initToolBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
