package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;

public abstract class PrimitiveElementGetter extends ElementGetter {


	public static abstract class Builder extends ElementGetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {
			// nothing to explore since primitive type
		}

		@Override
		public boolean build0(TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			typeBuilder.putElementGetterTag(fieldTag);
			return false;
		}
	}
	

	public PrimitiveElementGetter(String tag, Method method) {
		super(tag, method);
	}


	@Override
	public Method getMethod() {
		return method;
	}
}
