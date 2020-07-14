package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.composer.PrimitiveComposableElement.IntegerComposableElement;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;


/**
 * 
 * @author pierreconvert
 *
 */
public class IntegerElementGetter extends PrimitiveElementGetter {

	public final static Prototype PROTOTYPE = new Prototype() {
		
		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(type==int.class && method.getParameterCount()==0){
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
			typeBuilder.putElementGetter(new IntegerElementGetter(fieldTag, method));
			return false;
		}
	}
	
	public IntegerElementGetter(String tag, Method method) {
		super(tag, method);
	}

	@Override
	public void createComposableElement(ObjectComposableScope scope) throws Exception {
		int value = (int) method.invoke(scope.getObject(), new Object[]{});
		scope.append(new IntegerComposableElement(fieldTag, value));
	}
}