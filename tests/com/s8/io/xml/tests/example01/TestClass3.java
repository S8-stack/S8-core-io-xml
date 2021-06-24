package com.s8.io.xml.tests.example01;


import com.s8.io.xml.api.XML_GetAttribute;
import com.s8.io.xml.api.XML_SetAttribute;
import com.s8.io.xml.api.XML_Type;

@XML_Type(name="ee", sub={
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
