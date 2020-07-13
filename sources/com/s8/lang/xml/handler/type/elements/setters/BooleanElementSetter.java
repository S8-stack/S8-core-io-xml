package com.s8.lang.xml.handler.type.elements.setters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.lang.xml.api.XML_SetElement;
import com.s8.lang.xml.parser.PrimitiveParsedScope;
import com.s8.lang.xml.parser.PrimitiveParsedScope.Callback;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader;

public class BooleanElementSetter extends PrimitiveElementSetter {
	
	
	public final static Prototype PROTOTYPE = new Prototype() {
		
		@Override
		public boolean matches(Class<?> fieldType) {
			return fieldType==boolean.class;
		}
		
		@Override
		public ElementSetter.Builder create(Method method) {
			XML_SetElement setElementAnnotation = method.getAnnotation(XML_SetElement.class);
			String tag = setElementAnnotation.tag();
			return new BooleanElementSetter.Builder(tag, method);
		}
	};


	public static class Builder extends PrimitiveElementSetter.Builder {

		public Builder(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementSetter getStandardSetter() {
			return new BooleanElementSetter(tag, method);
		}
	}
	

	public BooleanElementSetter(String tag, Method method) {
		super(tag, method);
	}

	@Override
	protected Callback getCallback(Object object, XML_StreamReader.Point point) {

		return new PrimitiveParsedScope.Callback() {

			@Override
			public void set(String value) throws XML_ParsingException {
				boolean var = Boolean.valueOf(value);
				try {
					method.invoke(object, var);
				} 
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new XML_ParsingException(point, e.getMessage());
				}
			}
		};
	}
}
