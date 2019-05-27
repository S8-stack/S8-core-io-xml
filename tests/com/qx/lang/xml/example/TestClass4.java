package com.qx.lang.xml.example;


import com.qx.back.lang.xml.annotation.XML_GetAttribute;
import com.qx.back.lang.xml.annotation.XML_GetElement;
import com.qx.back.lang.xml.annotation.XML_SetAttribute;
import com.qx.back.lang.xml.annotation.XML_SetElement;
import com.qx.back.lang.xml.annotation.XML_Type;

@XML_Type(name="ee2", sub={})
public class TestClass4 extends TestClass3 {

	private int b = 1;
	
	public User[] users;
	
	@XML_SetAttribute(name="b")
	public void setToto2(int b){
		this.b = b;
	}
	
	@XML_GetAttribute(name="b")
	public int getToto2(){
		return b;
	}
	
	@XML_SetElement(name="users")
	public void setUsers(User[] users){
		this.users = users;
	}
	
	@XML_GetElement(name="users")
	public User[] getUsers(){
		return users;
	}
	
}
