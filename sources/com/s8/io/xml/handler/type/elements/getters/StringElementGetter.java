package com.s8.io.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.io.xml.composer.ObjectComposableScope;
import com.s8.io.xml.composer.PrimitiveComposableElement.StringComposableElement;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class StringElementGetter extends PrimitiveElementGetter {

	public final static Prototype PROTOTYPE = new Prototype() {
		
		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(type==String.class && method.getParameterCount()==0){
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
			return new StringElementGetter(fieldTag, method);
		}

	}
	
	public StringElementGetter(String tag, Method method) {
		super(tag, method);
	}

	@Override
	public void createComposableElement(ObjectComposableScope scope) throws Exception {
		String value = (String) method.invoke(scope.getObject(), new Object[]{});
		if(value!=null) {
			scope.append(new StringComposableElement(fieldTag, value));	
		}
	}
}