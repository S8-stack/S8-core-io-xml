package com.s8.io.xml.tests.example02;


import com.s8.io.xml.annotations.XML_SetElement;
import com.s8.io.xml.annotations.XML_Type;

@XML_Type(name = "Type02Stage")
public class Type02Stage extends MyStage {

	
	public MyStageDesign design;
	
	@XML_SetElement(tag = "design")
	public void setDesign(Type02StageDesign design) {
		this.design = design;
	}
	
	@XML_Type(name = "Type02StageDesign")
	public static class Type02StageDesign extends MyStageDesign {
		
	}
}
