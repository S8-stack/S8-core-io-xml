package com.s8.core.io.xml.handler.type.elements.setters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.core.io.xml.parser.PrimitiveParsedScope;
import com.s8.core.io.xml.parser.XML_ParsingException;
import com.s8.core.io.xml.parser.XML_StreamReader;
import com.s8.core.io.xml.parser.PrimitiveParsedScope.Callback;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class StringElementSetter extends PrimitiveElementSetter {

	public final static Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Class<?> fieldType) {
			return fieldType==String.class;
		}

		@Override
		public ElementSetter.Builder create(Method method) {
			return new StringElementSetter.Builder(method);
		}
	};
	
	public static class Builder extends PrimitiveElementSetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public ElementSetter createSetter() {
			return new StringElementSetter(method);
		}
	}
	
	public StringElementSetter(Method method) {
		super(method);
	}

	@Override
	protected Callback getCallback(Object object, XML_StreamReader.Point point) {

		return new PrimitiveParsedScope.Callback() {

			@Override
			public void set(String value) throws XML_ParsingException {
				try {
					method.invoke(object, value);
				} 
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new XML_ParsingException(point, e.getMessage());
				}
			}
		};
	}
}

