package com.qx.level0.lang.xml.tests.examples;


import com.s8.lang.xml.annotation.XML_GetAttribute;
import com.s8.lang.xml.annotation.XML_GetElement;
import com.s8.lang.xml.annotation.XML_SetAttribute;
import com.s8.lang.xml.annotation.XML_SetElement;
import com.s8.lang.xml.annotation.XML_Type;

@XML_Type(name="test", sub={TestClass2.class}, isRoot=true)
public class Wrapper {

	private double a = 5.0;
	
	private TestClass3 field;
	
	public TestClass3[] array;
	
	@XML_SetAttribute(name="factor")
	public void setFactor(double a){
		this.a = a;
	}
	
	@XML_GetAttribute(tag="factor")
	public double getFactor(){
		return a;
	}
	
	@XML_SetElement(tag="field")
	public void setField(TestClass3 field){
		this.field = field;
	}
	
	@XML_GetElement(tag="field")
	public TestClass3 getField(){
		return field;
	}
	
	@XML_SetElement(tag="items")
	public void setArray(TestClass3[] array){
		this.array = array;
	}
	
	@XML_GetElement(tag="items")
	public TestClass3[] getArray(){
		return array;
	}
	
}
