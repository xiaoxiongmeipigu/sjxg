package com.zjhj.commom.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 省
 * @author brain
 *
 */
public class ProvinceModel implements Serializable{
	private String id;
	private String name;
	private List<CityModel> children = new ArrayList<CityModel>();
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> city_list) {
		super();
		this.name = name;
		this.children = city_list;
	}

	public List<CityModel> getChildren() {
		return children;
	}

	public void setChildren(List<CityModel> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ProvinceModel [region_name=" + name + ", cityList=" + children + "]";
	}
	
}
