package com.gean.tttemplate.utils;

public class Param {
	
	private String label;
	private ParamTypeEnum type;
	private Object data;
	
	public Param() {
		
	}
	
	public Param(String label) {
		this();
		this.label = label;
		this.type = ParamTypeEnum.STRING;
	}
	
	public Param(String label, ParamTypeEnum type) {
		this(label);
		this.type = type;
	}
	
	public Param(String label, ParamTypeEnum type, Object data) {
		this(label, type);
		this.data = data;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public ParamTypeEnum getType() {
		return type;
	}
	public void setType(ParamTypeEnum type) {
		this.type = type;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Param) {
			return this.label.equals(obj);
		} else {
			return false;
		}
	}
	
	
}
