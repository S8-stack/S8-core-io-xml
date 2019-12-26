package com.qx.lang.xml.example;


import com.qx.level0.lang.xml.annotation.XML_GetAttribute;
import com.qx.level0.lang.xml.annotation.XML_GetElement;
import com.qx.level0.lang.xml.annotation.XML_SetAttribute;
import com.qx.level0.lang.xml.annotation.XML_SetElement;
import com.qx.level0.lang.xml.annotation.XML_Type;

@XML_Type(name="test", sub={TestClass2.class})
public class Wrapper {

	private double a = 5.0;
	
	private TestClass3 field;
	
	public TestClass3[] array;
	
	@XML_SetAttribute(name="factor")
	public void setFactor(double a){
		this.a = a;
	}
	
	@XML_GetAttribute(name="factor")
	public double getFactor(){
		return a;
	}
	
	@XML_SetElement(tag="field")
	public void setField(TestClass3 field){
		this.field = field;
	}
	
	@XML_GetElement(name="field")
	public TestClass3 getField(){
		return field;
	}
	
	@XML_SetElement(tag="items")
	public void setArray(TestClass3[] array){
		this.array = array;
	}
	
	@XML_GetElement(name="items")
	public TestClass3[] getArray(){
		return array;
	}
	
}
