package com.yhyy.cityselect.config;

import com.yhyy.cityselect.bean.TypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWolf on 2019/9/13.
 */
public class Contracts {
    public static final List<TypeBean> cityList = new ArrayList<TypeBean>() {//设置列表
        {
            add(new TypeBean("弹窗样式",""));
            add(new TypeBean("分类样式",""));
        }
    };
}
