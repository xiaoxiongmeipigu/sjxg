package com.zjhj.commom.result;

import java.io.Serializable;

/**
 * 区/县
 * @author brain
 *
 */
public class DistrictModel implements Serializable {

	private String id;
	private String name;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String region_name, String region_id) {
		super();
		this.name = region_name;
		this.id = region_id;
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
		return "DistrictModel [region_name=" + name + ", region_id=" + id + "]";
	}

}
