package com.s8.core.io.xml.handler.type.elements.setters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
public class EnumElementSetter extends PrimitiveElementSetter {

	public final static Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Class<?> fieldType) {
			return fieldType.isEnum();
		}

		@Override
		public ElementSetter.Builder create(Method method) {
			return new EnumElementSetter.Builder(method);
		}
	};
	
	public static class Builder extends PrimitiveElementSetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public ElementSetter createSetter() {
			return new EnumElementSetter(method);
		}
	}


	public final Class<?> type;
	
	public final Map<String, Object> map;
	
	public EnumElementSetter(Method method) {
		super(method);
		type = method.getParameterTypes()[0];
		
		Object[] values = type.getEnumConstants();
		
		int n = values.length;
		map = new HashMap<>(n);
		for(int i = 0; i<n; i++) { 
			Enum<?> item = (Enum<?>) values[i];
			map.put(item.name(), values[i]); 
		}
	}
	

	@Override
	protected Callback getCallback(Object object, XML_StreamReader.Point point) {

		return new PrimitiveParsedScope.Callback() {

			@Override
			public void set(String value) throws XML_ParsingException {
				try {
					method.invoke(object, map.get(value));
				} 
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new XML_ParsingException(point, e.getMessage());
				}
			}
		};
	}
}

