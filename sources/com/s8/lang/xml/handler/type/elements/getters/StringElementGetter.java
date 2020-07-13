package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.composer.PrimitiveComposableElement.StringComposableElement;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;


public class StringElementGetter extends ElementGetter {

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
	
	
	public static class Builder extends ElementGetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public void build(TypeBuilder typeBuilder) {
			typeBuilder.putElementGetter(new StringElementGetter(method));
		}

		/**
		 * 
		 * @param contextBuilder
		 */
		public void explore(XML_ContextBuilder contextBuilder) {
			// nothing to explore
		}
	}
	
	public StringElementGetter(Method method) {
		super(method);
	}

	@Override
	public void createComposableElement(ObjectComposableScope scope) throws Exception {
		String value = (String) method.invoke(scope.getObject(), new Object[]{});
		scope.append(new StringComposableElement(tag, value));
	}
}