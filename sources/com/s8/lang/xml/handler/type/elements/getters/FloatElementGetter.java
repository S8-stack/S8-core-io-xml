package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.composer.PrimitiveComposableElement.FloatComposableElement;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;

public class FloatElementGetter extends ElementGetter {

	public final static Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(type==float.class && method.getParameterCount()==0){
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

	
	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class Builder extends ElementGetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public void build(TypeBuilder typeBuilder) {
			typeBuilder.putElementGetter(new FloatElementGetter(method));
		}

		/**
		 * 
		 * @param contextBuilder
		 */
		public void explore(XML_ContextBuilder contextBuilder) {
			// nothing to explore
		}
	}


	public FloatElementGetter(Method method) {
		super(method);
	}

	@Override
	public void createComposableElement(ObjectComposableScope scope) throws Exception {
		float value = (float) method.invoke(scope.getObject(), new Object[]{});
		scope.append(new FloatComposableElement(tag, value));
	}
}
