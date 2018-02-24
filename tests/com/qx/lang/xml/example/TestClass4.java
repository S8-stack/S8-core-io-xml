package com.qx.lang.xml.example;


import com.qx.lang.xml.annotation.XML_GetAttribute;
import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="ee2", sub={})
public class TestClass4 extends TestClass3 {

	private int toto2 = 1;
	
	@XML_SetAttribute(name="toto2")
	public void setToto2(int b){
		this.toto2 = b;
	}
	
	@XML_GetAttribute(name="toto2")
	public int getToto2(){
		return toto2;
	}
}
