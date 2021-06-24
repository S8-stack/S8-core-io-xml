package com.s8.blocks.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.blocks.xml.composer.ObjectComposableScope;
import com.s8.blocks.xml.composer.PrimitiveComposableElement.IntegerComposableElement;


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
		public PrimitiveElementGetter createGetter() {
			return new IntegerElementGetter(fieldTag, method);
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