package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;

import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;


/**
 * 
 * @author pierre convert
 *
 */
public class ObjectListElementGetter extends ElementGetter {

	public static final Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Method method) {
			Class<?> type = method.getReturnType();
			if(type!=void.class) {
				return false;
			}

			Parameter[] parameters = method.getParameters();
			if(parameters.length!=1) {
				return false;
			}

			Class<?> parameterType = parameters[0].getType();
			return parameterType.equals(Consumer.class);
		}

		@Override
		public ElementGetter.Builder create(Method method) {
			return new ObjectListElementGetter.Builder(method);
		}
	};
	
	
	public static class Builder extends ElementGetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {
			Parameter[] parameters = method.getParameters();
			ParameterizedType argType = (ParameterizedType) parameters[0].getParameterizedType();
			Class<?> componentType = (Class<?>) (argType.getActualTypeArguments())[0];
			contextBuilder.register(componentType);
		}

		@Override
		public void build(TypeBuilder typeBuilder) {
			typeBuilder.putElementGetter(new ObjectListElementGetter(method));
		}
	}


	/**
	 * 
	 * @param method
	 */
	public ObjectListElementGetter(Method method) {
		super(method);
	}


	@Override
	public <T> void createComposableElement(ObjectComposableScope scope) throws Exception {

		Consumer<T> consumer = new Consumer<T>() {

			@Override
			public void accept(T subObject) {
				if(subObject!=null) {
					scope.append(new ObjectComposableScope(tag, subObject));
				}
			}
		};

		// invoke consumer on parent object
		method.invoke(scope.getObject(), new Object[]{ consumer });
	}
}
