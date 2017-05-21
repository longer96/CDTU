package com.longer.school.modle.bean;

public class PersonClass {

	private int id;
	private String name;
	private String type;
	private String tag;
	private String tel;
	private String qq;
	private String dw;
	private String info;
	
	
	public PersonClass() {
		
	}
	public PersonClass(int id, String name, String type, String tag, String tel, String qq, String dw, String info) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.tag = tag;
		this.tel = tel;
		this.qq = qq;
		this.dw = dw;
		this.info = info;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}
