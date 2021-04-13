package com.yhyy.cityselect;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.yhyy.cityselect.bean.CityBean;
import com.yhyy.cityselect.bean.ProvinceBean;
import com.yhyy.cityselect.config.AppConfig;
import com.yhyy.cityselect.config.CrashHandler;
import com.yhyy.cityselect.config.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplication extends MultiDexApplication {
    private static Context context;
    private static MyApplication myApplication;
    private Thread thread;

    private List<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private static final int PROVINCE_SUCCESS = 0x0001;
    private static final int PROVINCE_FAILED = 0x0002;

    private static boolean isProvince = false;

    private List<CityBean> cityList = new ArrayList<>();
    private List<CityBean> hotCityList = new ArrayList<>();

    private List<String> sections = new ArrayList<>();
    private Map<String, List<CityBean>> map = new HashMap<String, List<CityBean>>();
    private List<Integer> positions = new ArrayList<Integer>();
    private Map<String, Integer> indexer = new HashMap<String, Integer>();

    private static final String FORMAT = "^[a-z,A-Z].*$";

    private static final int CITY_SUCCESS = 0x0003;
    private static final int CITY_FAILED = 0x0004;

    private static boolean isCity = false;

    public static MyApplication getInstance() {
        // 这里不用判断instance是否为空
        return myApplication;
    }

    public List<ProvinceBean> getOptions1Items() {
        return options1Items;
    }

    public ArrayList<ArrayList<String>> getOptions2Items() {
        return options2Items;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getOptions3Items() {
        return options3Items;
    }

    public boolean getIsProvince() {
        return isProvince;
    }

    public List<CityBean> getCityList() {
        return cityList;
    }

    public List<CityBean> getHotCity() {
        return hotCityList;
    }

    public List<String> getSections() {
        return sections;
    }

    public Map<String, List<CityBean>> getMap() {
        return map;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public Map<String, Integer> getIndexer() {
        return indexer;
    }

    public boolean getIsCity() {
        return isCity;
    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROVINCE_SUCCESS:
                    isProvince = true;
                    break;
                case PROVINCE_FAILED:
                    Toast.makeText(context, "弹窗样式数据加载失败", Toast.LENGTH_SHORT).show();
                    break;
                case CITY_SUCCESS:
                    isCity = true;
                    break;
                case CITY_FAILED:
                    Toast.makeText(context, "分类样式数据加载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        myApplication = this;
        context = this.getApplicationContext();
        Stetho.initializeWithDefaults(this);
        CrashHandler.create(context, AppConfig.SAVEFILENAME);
        initProvince();
        initCity();
    }

    private void initCity() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 子线程中解析省市区数据
                initCityData();
            }
        });
        thread.start();
    }

    private void initCityData() {
        String CityData = new JsonUtil().getJson(this, "city.json");//获取assets目录下的json文件数据
        try {
            JSONObject jsonObject = new JSONObject(CityData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                CityBean cityBean = gson.fromJson(jsonArray.optJSONObject(i).toString(), CityBean.class);
                cityList.add(cityBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(CITY_FAILED);
        }
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getHot_flag().equals("1")) {
                hotCityList.add(cityList.get(i));
            }
        }
        for (CityBean cityBean : cityList) {
            String firstName = cityBean.getSort_py();
            if (firstName.matches(FORMAT)) {
                if (sections.contains(firstName)) {
                    map.get(firstName).add(cityBean);
                } else {
                    sections.add(firstName);
                    List<CityBean> list = new ArrayList<CityBean>();
                    list.add(cityBean);
                    map.put(firstName, list);
                }
            } else {
                if (sections.contains("#")) {
                    map.get("#").add(cityBean);
                } else {
                    sections.add("#");
                    List<CityBean> list = new ArrayList<CityBean>();
                    list.add(cityBean);
                    map.put("#", list);
                }
            }
        }
        Collections.sort(sections);
        int position = 0;
        for (int i = 0; i < sections.size(); i++) {
            indexer.put(sections.get(i), position);
            positions.add(position);
            position += map.get(sections.get(i)).size();
        }
        mHandler.sendEmptyMessage(CITY_SUCCESS);
    }

    private void initProvince() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 子线程中解析省市区数据
                initProvinceData();
            }
        });
        thread.start();
    }


    private void initProvinceData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String ProvinceData = new JsonUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<ProvinceBean> provinceBeanArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(ProvinceData);
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                ProvinceBean provinceBean = gson.fromJson(jsonArray.optJSONObject(i).toString(), ProvinceBean.class);
                provinceBeanArrayList.add(provinceBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(PROVINCE_FAILED);
        }

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的ProvinceBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = provinceBeanArrayList;

        for (int i = 0; i < provinceBeanArrayList.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> areaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int j = 0; j < provinceBeanArrayList.get(i).getCityList().size(); j++) {//遍历该省份的所有城市
                String cityName = provinceBeanArrayList.get(i).getCityList().get(j).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                city_AreaList.addAll(provinceBeanArrayList.get(i).getCityList().get(j).getArea());
                areaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(areaList);
        }
        mHandler.sendEmptyMessage(PROVINCE_SUCCESS);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
