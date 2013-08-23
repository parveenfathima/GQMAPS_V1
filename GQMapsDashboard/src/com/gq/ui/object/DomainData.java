package com.gq.ui.object;

public class DomainData {
	private String id;
	private String desc;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return ("ID: " + this.id + ", Desc: " + this.desc + ", Type: " + this.type);
	}
}
