package com.s8.io.xml.demos.repo01;


import com.s8.core.io.xml.annotations.XML_GetAttribute;
import com.s8.core.io.xml.annotations.XML_SetAttribute;
import com.s8.core.io.xml.annotations.XML_Type;

@XML_Type(name="TestClass2", sub={})
public class TestClass2 extends Wrapper {

	public int hiddenValue;
	
	private double b = 5.0;
	
	@XML_SetAttribute(name="b2")
	public void setFactor2(double b){
		this.b = b;
	}
	
	@XML_GetAttribute(tag="b2")
	public double getFactor2(){
		return b;
	}
}
