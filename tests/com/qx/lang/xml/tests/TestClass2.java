package com.qx.lang.xml.tests;


import com.qx.lang.xml.annotation.XML_GetAttribute;
import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="test2", sub={})
public class TestClass2 extends TestClass {

	public int hiddenValue;
	
	private double b = 5.0;
	
	@XML_SetAttribute(name="b2")
	public void setFactor2(double b){
		this.b = b;
	}
	
	@XML_GetAttribute(name="b2")
	public double getFactor2(){
		return b;
	}
}
