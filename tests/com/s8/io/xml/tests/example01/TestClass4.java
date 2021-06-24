package com.s8.io.xml.tests.example01;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.s8.blocks.xml.annotations.XML_GetAttribute;
import com.s8.blocks.xml.annotations.XML_GetElement;
import com.s8.blocks.xml.annotations.XML_SetAttribute;
import com.s8.blocks.xml.annotations.XML_SetElement;
import com.s8.blocks.xml.annotations.XML_Type;

@XML_Type(name="ee2", sub={})
public class TestClass4 extends TestClass3 {

	private int b = 1;
	
	public List<User> users;
	
	public TestClass4() {
		super();
		users = new ArrayList<User>();
	}
	
	@XML_SetAttribute(name="b")
	public void setToto2(int b){
		this.b = b;
	}
	
	@XML_GetAttribute(tag="b")
	public int getToto2(){
		return b;
	}
	
	@XML_SetElement(tag="user")
	public void appendUser(User user){
		users.add(user);
	}
	
	@XML_GetElement(tag="user")
	public void getUsers(Consumer<User> crawler){
		for(User user : users) {
			crawler.accept(user);
		}
	}
	
}
