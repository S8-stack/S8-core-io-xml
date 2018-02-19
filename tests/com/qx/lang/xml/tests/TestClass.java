package com.qx.lang.xml.tests;


import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="test", sub={})
public class TestClass {

	private double a;
	
	@XML_SetAttribute(name="factor")
	public void setFactor(double a){
		this.a = a;
	}
}
