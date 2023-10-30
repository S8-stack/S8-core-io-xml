package com.s8.io.xml.demos.repo01;


import com.s8.core.io.xml.annotations.XML_GetAttribute;
import com.s8.core.io.xml.annotations.XML_SetAttribute;
import com.s8.core.io.xml.annotations.XML_Type;

@XML_Type(name="EeClass3", sub={
		TestClass4.class 
})
public class TestClass3 {

	private int toto = -1;

	@XML_SetAttribute(name="toto")
	public void setToto(int b){
		this.toto = b;
	}

	@XML_GetAttribute(tag="toto")
	public int getToto(){
		return toto;
	}
}
