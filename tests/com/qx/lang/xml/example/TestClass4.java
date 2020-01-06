package com.qx.lang.xml.example;


import com.qx.level0.lang.xml.annotation.XML_GetAttribute;
import com.qx.level0.lang.xml.annotation.XML_GetElement;
import com.qx.level0.lang.xml.annotation.XML_SetAttribute;
import com.qx.level0.lang.xml.annotation.XML_SetElement;
import com.qx.level0.lang.xml.annotation.XML_Type;

@XML_Type(name="ee2", sub={})
public class TestClass4 extends TestClass3 {

	private int b = 1;
	
	public User[] users;
	
	@XML_SetAttribute(name="b")
	public void setToto2(int b){
		this.b = b;
	}
	
	@XML_GetAttribute(tag="b")
	public int getToto2(){
		return b;
	}
	
	@XML_SetElement(tag="users")
	public void setUsers(User[] users){
		this.users = users;
	}
	
	@XML_GetElement(tag="users")
	public User[] getUsers(){
		return users;
	}
	
}
