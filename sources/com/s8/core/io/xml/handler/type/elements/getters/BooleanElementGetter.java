package com.s8.core.io.xml.handler.type.elements.getters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.s8.core.io.xml.composer.ComposableScope;
import com.s8.core.io.xml.composer.XML_ComposingException;
import com.s8.core.io.xml.composer.PrimitiveComposableElement.BooleanComposableElement;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class BooleanElementGetter extends PrimitiveElementGetter {

	public final static Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(type==boolean.class && method.getParameterCount()==0){
				return true;
			}
			else {
				return false;
			}
		}

		@Override
		public ElementGetter.Builder create(Method method) {
			return new Builder(method);
		}
	};


	public static class Builder extends PrimitiveElementGetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public PrimitiveElementGetter createGetter() {
			return new BooleanElementGetter(declaredTag, method);
		}
	}


	public BooleanElementGetter(String tag, Method method) {
		super(tag, method);
	}

	@Override
	public void compose(Object object, List<ComposableScope> subScopes) throws XML_ComposingException {
		try {
			boolean value = (boolean) method.invoke(object, new Object[]{});
			subScopes.add(new BooleanComposableElement(tag, value));
		} 
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new XML_ComposingException(e.getMessage()+ "for "+method);
		}
	}

}
