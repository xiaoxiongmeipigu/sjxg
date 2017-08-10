package com.zjhj.commom.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 市
 * @author brain
 *
 */
public class CityModel implements Serializable {
	private String id;
	private String name;
	private List<DistrictModel> children = new ArrayList<DistrictModel>();
	
	public CityModel() {
		super();
	}

	public CityModel(String region_name, List<DistrictModel> area_list) {
		super();
		this.name = region_name;
		this.children = area_list;
	}

	public List<DistrictModel> getChildren() {
		return children;
	}

	public void setChildren(List<DistrictModel> children) {
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
		return "CityModel [region_name=" + name + ", districtList=" + children
				+ "]";
	}
	
}
