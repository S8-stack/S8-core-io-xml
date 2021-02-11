package com.s8.lang.xml.handler;

import java.io.IOException;
import java.io.Writer;
import java.util.Map.Entry;

import com.s8.lang.xml.XSD_Syntax;
import com.s8.lang.xml.handler.type.TypeHandler;

public class XSD_SchemaGenerator {

	private XML_Context context;
	
	public XSD_SchemaGenerator(XML_Context context) {
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
