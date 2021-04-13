package com.yhyy.cityselect.bean;

public class TypeBean {
	private String title;
	private String city;

	public TypeBean() {
	}

	public TypeBean(String title, String city) {
		this.title = title;
		this.city = city;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
