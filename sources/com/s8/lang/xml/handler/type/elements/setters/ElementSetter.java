package com.s8.lang.xml.handler.type.elements.setters;

import java.lang.reflect.Method;

import com.s8.lang.xml.handler.XML_Context;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;
import com.s8.lang.xml.parser.ObjectParsedScope;
import com.s8.lang.xml.parser.ParsedScope;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader;


public abstract class ElementSetter {

	public static abstract class Prototype {

		public abstract boolean matches(Class<?> fieldType);

		public abstract ElementSetter.Builder create(Method method);

	}

	public static abstract class Builder {
		
		

		protected String tag;

		/**
		 * 
		 * @param method
		 * @param tag
		 */
		public Builder(String tag) {
			super();
			this.tag = tag;
		}

		public String getFieldTag() {
			return tag;
		}
		
		
		/**
		 * explore subTypes
		 * 
		 * @param contextBuilder
		 * @throws XML_TypeCompilationException
		 */
		public abstract void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException;
		
		
		/**
		 * 
		 * @param context
		 * 
		 * @return false if the initialization has been successful, true if build need to re-launched.
		 * 
		 * @throws XML_TypeCompilationException 
		 */
		public abstract boolean build0(XML_ContextBuilder contextBuilder, TypeBuilder builder) throws XML_TypeCompilationException;
		

		public abstract boolean build1(XML_ContextBuilder contextBuilder, TypeBuilder builder) throws XML_TypeCompilationException;

		
	}




	/**
	 *  the XML tag for mapping purposes
	 */
	protected String tag;


	public ElementSetter(String tag) {
		super();
		this.tag = tag;
	}


	public abstract ParsedScope createParsedElement(XML_Context context, 
			ObjectParsedScope parent, XML_StreamReader.Point point) throws XML_ParsingException;


	public String getTag() {
		return tag;
	}
	
	
	public final static ElementSetter.Prototype[] PROTOTYPES = new ElementSetter.Prototype[] {
			BooleanElementSetter.PROTOTYPE,
			ShortElementSetter.PROTOTYPE,
			IntegerElementSetter.PROTOTYPE,
			LongElementSetter.PROTOTYPE,
			FloatElementSetter.PROTOTYPE,
			DoubleElementSetter.PROTOTYPE,
			StringElementSetter.PROTOTYPE,
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
}
