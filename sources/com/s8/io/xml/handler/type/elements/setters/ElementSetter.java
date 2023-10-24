package com.s8.io.xml.handler.type.elements.setters;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Set;

import com.s8.io.xml.annotations.XML_SetElement;
import com.s8.io.xml.codebase.XML_CodebaseBuilder;
import com.s8.io.xml.handler.type.TypeBuilder;
import com.s8.io.xml.handler.type.XML_TypeCompilationException;
import com.s8.io.xml.parser.ObjectParsedScope;
import com.s8.io.xml.parser.ParsedScope;
import com.s8.io.xml.parser.XML_ParsingException;
import com.s8.io.xml.parser.XML_StreamReader;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ElementSetter {

	public static abstract class Prototype {

		public abstract boolean matches(Class<?> fieldType);

		public abstract ElementSetter.Builder create(Method method);

	}

	public static abstract class Builder {



		public final String declaredTag;

		protected final Method method;

		protected boolean isBuilt0 = false;

		protected boolean isBuilt1 = false;



		/**
		 * 
		 * @param method
		 * @param tag
		 */
		public Builder(Method method) {
			super();
			
			
			XML_SetElement elementAnnotation = method.getAnnotation(XML_SetElement.class);
			declaredTag = elementAnnotation.tag();
			
			this.method = method;
		}



		/**
		 * explore subTypes
		 * 
		 * @param contextBuilder
		 * @throws XML_TypeCompilationException
		 */
		public abstract void explore(XML_CodebaseBuilder contextBuilder) throws XML_TypeCompilationException;


		
		public abstract void link(XML_CodebaseBuilder contextBuilder) throws XML_TypeCompilationException;
		
		
		public abstract boolean hasSubstitutionGroup();
		
		public abstract Set<String> getSubstitutionGroup();
		
		public abstract boolean isColliding(Set<String> substitutionGroup);
		
		
		/**
		 * 
		 * @param context
		 * 
		 * @return false if the initialization has been successful, true if build need to re-launched.
		 * 
		 * @throws XML_TypeCompilationException 
		 */
		public abstract void build(TypeBuilder declaringTypeBuilder, boolean isColliding) throws XML_TypeCompilationException;
		
		



	}

	
	
	
	public final Method method;



	public ElementSetter(Method method) {
		super();
		this.method = method;
	}

	/*
	public String getTag() {
		return tag;
	}
	*/


	/**
	 * Tells whether tag is the name of field or the name of a type (implying a specific field).
	 * @return true if tag is a field name, false otherwise.
	 */
	/*
	public boolean isFieldTag() {
		return isFieldTag;
	}
	*/



	public abstract ParsedScope createParsedElement(String tag, ObjectParsedScope parent, XML_StreamReader.Point point) throws XML_ParsingException;



	public final static ElementSetter.Prototype[] PROTOTYPES = new ElementSetter.Prototype[] {
			BooleanElementSetter.PROTOTYPE,
			ShortElementSetter.PROTOTYPE,
			IntegerElementSetter.PROTOTYPE,
			LongElementSetter.PROTOTYPE,
			FloatElementSetter.PROTOTYPE,
			DoubleElementSetter.PROTOTYPE,
			StringElementSetter.PROTOTYPE,
			EnumElementSetter.PROTOTYPE,
			ObjectElementSetter.PROTOTYPE
	};

	/**
	 * <p>We assume that all dependencies has been resolved at this point</p>
	 * 
	 * @param context
	 * @param method
	 * @param factory
	 * @return
	 * @throws XML_TypeCompilationException
	 */
	public static ElementSetter.Builder create(Method method) 
			throws XML_TypeCompilationException {

		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=1){
			throw new XML_TypeCompilationException("Illegal number of parameters for a setter");
		}

		Class<?> fieldType = parameters[0];

		for(ElementSetter.Prototype prototype : PROTOTYPES) {
			if(prototype.matches(fieldType)) {
				return prototype.create(method);
			}
		}

		throw new XML_TypeCompilationException("Failed to match setter for: "+method);
	}


	public abstract Method getMethod();

	public abstract void xsd_write(String tag, Writer writer) throws IOException;

	//public abstract void DTD_writeHeader(Writer writer) throws IOException;

	//public abstract void DTD_writeFieldDefinition(TypeHandler typeHandler, Writer writer) throws IOException;


}
