package com.s8.lang.xml.handler;

import java.io.IOException;
import java.io.Writer;

import com.s8.lang.xml.XML_Syntax;
import com.s8.lang.xml.handler.type.TypeHandler;

public class DTD_TemplateGenerator {

	private XML_Context context;
	
	public DTD_TemplateGenerator(XML_Context context) {
		super();
		this.context = context;
	}
	
	/**
	 * 
	 * @param writer
	 * @throws IOException 
	 */
	public void write(Writer writer) throws IOException {
		writer.append(XML_Syntax.HEADER);
		for(TypeHandler typeHandler : context.mapByTag.values()) {
			typeHandler.DTD_typeGenerator.writeTypeElement(writer);
		}
	}
}
