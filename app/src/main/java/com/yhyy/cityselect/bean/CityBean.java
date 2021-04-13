package com.yhyy.cityselect.bean;

public class CityBean{
	private String city_name;
	private String sort_py;
	private String hot_flag;

	public CityBean() {
	}

	public CityBean(String city_name, String sort_py, String hot_flag) {
		this.city_name = city_name;
		this.sort_py = sort_py;
		this.hot_flag = hot_flag;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getSort_py() {
		return sort_py;
	}

	public void setSort_py(String sort_py) {
		this.sort_py = sort_py;
	}

	public String getHot_flag() {
		return hot_flag;
	}

	public void setHot_flag(String hot_flag) {
		this.hot_flag = hot_flag;
	}
}
