package com.s8.core.io.xml.codebase;

import java.io.IOException;
import java.io.Writer;
import java.util.Map.Entry;

import com.s8.core.io.xml.XSD_Syntax;
import com.s8.core.io.xml.handler.type.TypeHandler;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class XSD_SchemaGenerator {

	private XML_Codebase context;
	
	public XSD_SchemaGenerator(XML_Codebase context) {
		super();
		this.context = context;
	}
	
	/**
	 * 
	 * @param writer
	 * @throws IOException 
	 */
	public void write(Writer writer) throws IOException {
		writer.append(XSD_Syntax.HEADER);
		writer.append(XSD_Syntax.OPENING_TAG);
		
		for(Entry<String, TypeHandler> rootElement : context.rootElements.entrySet()) {
			writer.append("\n\t<xs:element name=\""+rootElement.getKey()+"\" type=\"tns:"+rootElement.getValue().xsd_getTypeName()+"\"/>");
		}	
		
		for(TypeHandler typeHandler : context.map.values()) {
			typeHandler.xsd_TypeGenerator.writeDescriptor(writer);
		}
		writer.append(XSD_Syntax.CLOSING_TAG);
	}
}
