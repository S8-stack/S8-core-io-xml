package com.s8.lang.xml.handler.type;

import java.io.IOException;
import java.io.Writer;

import com.s8.lang.xml.handler.type.attributes.setters.AttributeSetter;
import com.s8.lang.xml.handler.type.elements.setters.ElementSetter;

public class XSD_TypeGenerator {

	
private TypeHandler typeHandler;
	
	/**
	 * 
	 * @param typeHandler
	 */
	public XSD_TypeGenerator(TypeHandler typeHandler) {
		super();
		this.typeHandler = typeHandler;
	}
	
	
	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void writeDescriptor(Writer writer) throws IOException {

		// declare element
		// <xs:complexType name="ee">
		writer.append("\n\n\t<xs:complexType name=\"");
		writer.append(typeHandler.xsd_getTypeName());
		writer.append("\">");
		
		
		/* <elements> */
		// MUST BE IN THE FIRST PLACE !!!
		if(!typeHandler.elementSetters.isEmpty()) {
		writer.append("\n\t\t<xs:sequence>");
		writer.append("\n\t\t<xs:choice maxOccurs=\"unbounded\" minOccurs=\"1\">");
		
		 
		for(ElementSetter elementSetter : typeHandler.elementSetters.values()) {
			elementSetter.xsd_write(writer);
		}
		writer.append("\n\t\t</xs:choice>");
		writer.append("\n\t\t</xs:sequence>");
		}
		/* </elements> */
		

		
		/* <attributes> */
		if(!typeHandler.attributeSetters.isEmpty()) {
			for(AttributeSetter attributeSetter : typeHandler.attributeSetters.values()) {
				attributeSetter.XSD_write(writer);
			}
		}

		/* </attributes> */
		
	
		
		// closing element
		writer.append("\n\t</xs:complexType>");
	}
	
}
