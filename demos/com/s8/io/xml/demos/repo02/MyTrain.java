package com.s8.io.xml.demos.repo02;

import java.util.ArrayList;
import java.util.List;

import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;

@XML_Type(name = "MyTrain")
public class MyTrain {

	public List<MyStage> stages;
	
	public MyTrain() {
		super();
		stages = new ArrayList<MyStage>();
	}
	
	@XML_SetElement(tag = "stage")
	public void appendStage(MyStage stage) {
		stages.add(stage);
	}
	
}
