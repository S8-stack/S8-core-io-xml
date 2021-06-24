package com.s8.io.xml.tests.example02;


import com.s8.io.xml.tests.example02.Type01Stage.Type01StageDesign;
import com.s8.io.xml.tests.example02.Type02Stage.Type02StageDesign;
import com.s8.io.xml.api.XML_Type;

@XML_Type(name = "MyStage", sub= { Type01Stage.class, Type02Stage.class})
public abstract class MyStage {

	@XML_Type(name = "MyStageDesign", sub= { 
			Type01StageDesign.class,
			Type02StageDesign.class })
	public abstract static class MyStageDesign {
		
	}
}
