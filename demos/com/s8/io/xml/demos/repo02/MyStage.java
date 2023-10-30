package com.s8.io.xml.demos.repo02;


import com.s8.core.io.xml.annotations.XML_Type;
import com.s8.io.xml.demos.repo02.Type01Stage.Type01StageDesign;
import com.s8.io.xml.demos.repo02.Type02Stage.Type02StageDesign;

@XML_Type(name = "MyStage", sub= { Type01Stage.class, Type02Stage.class})
public abstract class MyStage {

	@XML_Type(name = "MyStageDesign", sub= { 
			Type01StageDesign.class,
			Type02StageDesign.class })
	public abstract static class MyStageDesign {
		
	}
}
