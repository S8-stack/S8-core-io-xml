package com.s8.io.xml.handler;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.xml.codebase.XML_Codebase;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class DTD_TemplateGenerator {

	//private XML_Context context;
	
	public DTD_TemplateGenerator(XML_Codebase context) {
		super();
		//this.context = context;
	}
	
	/**
	 * 
	 * @param writer
	 * @throws IOException 
	 */
	public void write(Writer writer) throws IOException {
		/*
		writer.append(XML_Syntax.HEADER);
		for(TypeHandler typeHandler : context.mapByTag.values()) {
			typeHandler.DTD_typeGenerator.writeTypeElement(writer);
		}
		*/
	}
}
