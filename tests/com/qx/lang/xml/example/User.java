package com.qx.lang.xml.example;


import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="user", sub={})
public class User extends TestClass3 {

	private String name = "none";
	
	private String password = "none";

	public String getName() {
		return name;
	}

	@XML_SetAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}


	@XML_SetAttribute(name="password")
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
