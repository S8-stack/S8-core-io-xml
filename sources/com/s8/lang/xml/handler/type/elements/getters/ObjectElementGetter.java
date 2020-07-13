package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;


/**
 * 
 * @author pierre convert
 *
 */
public class ObjectElementGetter extends ElementGetter {

	
	/**
	 * 
	 */
	public static final Prototype PROTOTYPE = new Prototype() {
		
		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(!type.isPrimitive() && method.getParameterCount()==0){
				return true;
			}
			else {
				return false;
			}
		}
		
		@Override
		public ElementGetter.Builder create(Method method) {
			return new ObjectElementGetter.Builder(method);
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
		public void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {
			Class<?> type = method.getReturnType();
			contextBuilder.register(type);
		}

		@Override
		public void build(TypeBuilder typeBuilder) {
			typeBuilder.putElementGetter(new ObjectElementGetter(method));
		}
	}

	
	/**
	 * 
	 * @param method
	 */
	public ObjectElementGetter(Method method) {
		super(method);
	}


	@Override
	public <T> void createComposableElement(ObjectComposableScope scope) throws Exception {

		// invoke consumer on parent object
		Object subObject = method.invoke(scope.getObject());
		scope.append(new ObjectComposableScope(tag, subObject));
	}
}
