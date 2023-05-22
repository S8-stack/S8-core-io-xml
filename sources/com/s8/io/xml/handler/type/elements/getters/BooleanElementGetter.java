package com.s8.io.xml.handler.type.elements.getters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.io.xml.composer.ObjectComposableScope;
import com.s8.io.xml.composer.PrimitiveComposableElement.BooleanComposableElement;
import com.s8.io.xml.composer.XML_ComposingException;


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
	public void createComposableElement(ObjectComposableScope scope) throws XML_ComposingException {
		try {
			boolean value = (boolean) method.invoke(scope.getObject(), new Object[]{});
			scope.append(new BooleanComposableElement(tag, value));
		} 
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new XML_ComposingException(e.getMessage()+ "for "+method);
		}
	}

}
