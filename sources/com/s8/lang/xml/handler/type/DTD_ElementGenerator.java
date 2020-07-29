package com.s8.lang.xml.handler.type;

import java.io.IOException;
import java.io.Writer;

import com.s8.lang.xml.handler.type.attributes.setters.AttributeSetter;
import com.s8.lang.xml.handler.type.elements.setters.ElementSetter;

public class DTD_ElementGenerator {

	private TypeHandler typeHandler;
	
	/**
	 * 
	 * @param typeHandler
	 */
	public DTD_ElementGenerator(TypeHandler typeHandler) {
		super();
		this.typeHandler = typeHandler;
	}
	
	
	public void writeFieldElement(String fieldTag, Writer writer) throws IOException {
		writeElementDescriptor(fieldTag, writer);
	}
	
	public void writeTypeElement(Writer writer) throws IOException {
		writeElementDescriptor(typeHandler.getXmlTag(), writer);
	}
	
	
	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	private void writeElementDescriptor(String tag, Writer writer) throws IOException {

		// declare element
		writer.append("\n<!ELEMENT ");

		// specify tag name
		writer.append(tag);

		// add all fields
		writer.append(' ');
		boolean hasPrevious = false;
		for(ElementSetter elementSetter : typeHandler.elementSetters.values()) {
			if(hasPrevious) {
				writer.append(", ");
			}
			else {
				writer.append('(');
				hasPrevious = true;
			}
			elementSetter.DTD_writeHeader(writer);
		}
		
		if(hasPrevious) {
			writer.append(")*>");	
		}
		else {
			writer.append("EMPTY>");
		}
		
		// declare attributes

		/*
		<!ATTLIST  nom_élément
	     nom_attribut_1  type_attribut_1  déclaration_de_défaut_1
	     nom_attribut_2  type_attribut_2  déclaration_de_défaut_2
	     ...
	     >
		 */
		if(!typeHandler.attributeSetters.isEmpty()) {
			writer.append("\n<!ATTLIST ");

			// specify tag name
			writer.append(tag);

			hasPrevious = false;
			for(AttributeSetter attributeSetter : typeHandler.attributeSetters.values()) {
				if(hasPrevious) {
					writer.append(" ");
				}
				else {
					writer.append(" ");
					hasPrevious = true;
				}
				attributeSetter.writeDTD(writer);
			}
			writer.append(">");		
		}
		
		
		/**
		 * write fields
		 */
		for(ElementSetter elementSetter : typeHandler.elementSetters.values()) {
			elementSetter.DTD_writeFieldDefinition(typeHandler, writer);
		}
	}
	
	
}
