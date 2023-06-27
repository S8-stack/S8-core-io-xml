package com.s8.io.xml.handler.type.elements.getters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.s8.io.xml.composer.ComposableScope;
import com.s8.io.xml.composer.PrimitiveComposableElement.LongComposableElement;
import com.s8.io.xml.composer.XML_ComposingException;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LongElementGetter extends PrimitiveElementGetter {

	public final static Prototype PROTOTYPE = new Prototype() {
		
		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(type==long.class && method.getParameterCount()==0){
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
			return new LongElementGetter(declaredTag, method);
		}
	}
	
	/**
	 * 
	 * @param method
	 */
	public LongElementGetter(String tag, Method method) {
		super(tag, method);
	}

	
	@Override
	public void compose(Object object, List<ComposableScope> scopes) throws XML_ComposingException {
		try {
			long value = (long) method.invoke(object, new Object[]{});
			scopes.add(new LongComposableElement(tag, value));
		} 
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new XML_ComposingException(e.getMessage()+ "for "+method);
		}
		
	}
}
