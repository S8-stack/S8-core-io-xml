package com.s8.io.xml.handler.type.elements.getters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.io.xml.composer.ObjectComposableScope;
import com.s8.io.xml.composer.XML_ComposingException;
import com.s8.io.xml.composer.PrimitiveComposableElement.ShortComposableElement;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ShortElementGetter extends PrimitiveElementGetter {

	public final static Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(type==short.class && method.getParameterCount()==0){
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
			return new ShortElementGetter(declaredTag, method);
		}
	}

	public ShortElementGetter(String tag, Method method) {
		super(tag, method);
	}

	@Override
	public void createComposableElement(ObjectComposableScope scope) throws XML_ComposingException {
		try {
			short value = (short) method.invoke(scope.getObject(), new Object[]{});
			scope.append(new ShortComposableElement(tag, value));
		} 
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new XML_ComposingException(e.getMessage()+ "for "+method);
		}
	}



}