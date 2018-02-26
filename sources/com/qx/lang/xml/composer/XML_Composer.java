package com.qx.lang.xml.composer;

import com.qx.lang.xml.context.XML_Context;

public class XML_Composer {

	private XML_Context context;
	
	private XML_StreamWriter writer;

	public XML_Composer(XML_Context context, XML_StreamWriter writer) {
		super();
		this.context = context;
		this.writer = writer;
	}
	
	public void compose(Object object){
		
	}

	
}
