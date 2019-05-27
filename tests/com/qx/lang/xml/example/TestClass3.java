package com.qx.lang.xml.example;


import com.qx.back.lang.xml.annotation.XML_GetAttribute;
import com.qx.back.lang.xml.annotation.XML_SetAttribute;
import com.qx.back.lang.xml.annotation.XML_Type;

@XML_Type(name="ee", sub={TestClass4.class})
public class TestClass3 {

	private int toto = -1;
	
	@XML_SetAttribute(name="toto")
	public void setToto(int b){
		this.toto = b;
	}
	
	@XML_GetAttribute(name="toto")
	public int getToto(){
		return toto;
	}
	
	
}
