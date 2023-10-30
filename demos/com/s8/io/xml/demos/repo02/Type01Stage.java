package com.s8.io.xml.demos.repo02;

import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;

@XML_Type(name = "Type01Stage")
public class Type01Stage extends MyStage {
	
	public MyStageDesign design;
	
	@XML_SetElement(tag = "design")
	public void setDesign(Type01StageDesign design) {
		this.design = design;
	}
	

	@XML_Type(name = "Type01StageDesign")
	public static class Type01StageDesign extends MyStageDesign {
		
	}	
	
}
