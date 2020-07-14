package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.composer.PrimitiveComposableElement.BooleanComposableElement;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;

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
		public boolean build1(XML_ContextBuilder contextBuilder, TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			typeBuilder.putElementGetter(new BooleanElementGetter(fieldTag, method));
			return false;
		}
	}
	

	public BooleanElementGetter(String tag, Method method) {
		super(tag, method);
	}

	@Override
	public void createComposableElement(ObjectComposableScope scope) throws Exception {
		boolean value = (boolean) method.invoke(scope.getObject(), new Object[]{});
		scope.append(new BooleanComposableElement(fieldTag, value));
	}

}
