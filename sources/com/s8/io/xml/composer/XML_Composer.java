package com.s8.io.xml.composer;

import com.s8.io.xml.codebase.XML_Codebase;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class XML_Composer {

	private XML_Codebase context;
	
	private XML_StreamWriter writer;
	
	
	public XML_Composer(XML_Codebase context, XML_StreamWriter writer) {
		super();
		this.context = context;
		this.writer = writer;
	}
	
	public void compose(Object object) throws Exception{
		ComposableScope scope = new DocumentComposableScope(object);
		scope.compose(context, writer);
	}
}
