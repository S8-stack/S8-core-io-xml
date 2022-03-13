package com.s8.io.xml.tests.example01;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.s8.io.xml.annotations.XML_GetAttribute;
import com.s8.io.xml.annotations.XML_GetElement;
import com.s8.io.xml.annotations.XML_SetAttribute;
import com.s8.io.xml.annotations.XML_SetElement;
import com.s8.io.xml.annotations.XML_Type;

@XML_Type(root=true, name="test", sub={ TestClass2.class })
public class Wrapper {

	private double a = 5.0;
	
	private Module field;
	
	public List<TestClass3> array;
	
	public Wrapper() {
		super();
		this.array = new ArrayList<TestClass3>();
	}
	
	@XML_SetAttribute(name="factor")
	public void setFactor(double a){
		this.a = a;
	}
	
	@XML_GetAttribute(tag="factor")
	public double getFactor(){
		return a;
	}
	
	@XML_SetElement(tag="field")
	public void setField(Module field){
		this.field = field;
	}
	
	@XML_GetElement(tag="field")
	public Module getField(){
		return field;
	}
	
	@XML_SetElement(tag="item")
	public void setItem(TestClass3 item){
		this.array.add(item);
	}
	
	@XML_GetElement(tag="item")
	public void getItems(Consumer<TestClass3> crawler){
		array.forEach(crawler);
	}
	
}
