package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.handler.XML_LexiconBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;

public abstract class PrimitiveElementGetter extends ElementGetter {


	public static abstract class Builder extends ElementGetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public void explore(XML_LexiconBuilder contextBuilder) throws XML_TypeCompilationException {
			// nothing to explore since primitive type
		}

		public abstract PrimitiveElementGetter createGetter();
		
		@Override
		public boolean build0(TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			if(!isBuilt0) {
				typeBuilder.putElementGetterTag(fieldTag);
				isBuilt0 = true;
				return false;		
			}
			else {
				return false;
			}
		}
		
		@Override
		public boolean build1(XML_LexiconBuilder contextBuilder, TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			if(!isBuilt1) {
				typeBuilder.putElementGetter(createGetter());
				isBuilt1 = true;
				return false;
			}
			else {
				return false;
			}
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
