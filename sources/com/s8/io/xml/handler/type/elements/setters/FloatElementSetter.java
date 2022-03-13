package com.s8.io.xml.handler.type.elements.setters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.io.xml.annotations.XML_SetElement;
import com.s8.io.xml.parser.PrimitiveParsedScope;
import com.s8.io.xml.parser.XML_ParsingException;
import com.s8.io.xml.parser.XML_StreamReader;
import com.s8.io.xml.parser.PrimitiveParsedScope.Callback;


public class FloatElementSetter extends PrimitiveElementSetter {

	public final static Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Class<?> fieldType) {
			return fieldType==float.class;
		}

		@Override
		public ElementSetter.Builder create(Method method) {
			XML_SetElement setElementAnnotation = method.getAnnotation(XML_SetElement.class);
			String tag = setElementAnnotation.tag();
			return new FloatElementSetter.Builder(tag, method);
		}
	};

	public static class Builder extends PrimitiveElementSetter.Builder {

		public Builder(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementSetter getStandardSetter() {
			return new FloatElementSetter(tag, method);
		}


	}

	public FloatElementSetter(String tag, Method method) {
		super(tag, method);
	}

	@Override
	protected Callback getCallback(Object object, XML_StreamReader.Point point) {

		return new PrimitiveParsedScope.Callback() {

			@Override
			public void set(String value) throws XML_ParsingException {
				float var = Float.valueOf(value);
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
