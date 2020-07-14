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
			contextBuilder.register(getTargetType(contextBuilder));
		}
		
		@Override
		public boolean build0(TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			typeBuilder.putElementGetterTag(fieldTag);
			return false;
		}

		@Override
		public boolean build1(XML_ContextBuilder contextBuilder, TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			Class<?> fieldType =  getTargetType(contextBuilder);
			TypeBuilder fieldTypeBuilder = contextBuilder.getTypeBuilder(fieldType);
			if(!fieldTypeBuilder.isInheritanceDiscovered()) {
				return true;
			}
			
			boolean isTypeTagPreferred = !isFieldTypeTagColliding(typeBuilder, fieldTypeBuilder);
			if(isTypeTagPreferred) {
				fillFieldTypeTags(typeBuilder, fieldTypeBuilder);
			}
			typeBuilder.putElementGetter(new ObjectListElementGetter(fieldTag, method, isTypeTagPreferred));
			return false;
		}
		
		
		/**
		 * 
		 * @param contextBuilder
		 * @return
		 */
		public Class<?> getTargetType(XML_ContextBuilder contextBuilder) {
			Parameter[] parameters = method.getParameters();
			ParameterizedType argType = (ParameterizedType) parameters[0].getParameterizedType();
			Class<?> componentType = (Class<?>) (argType.getActualTypeArguments())[0];
			return componentType;
		}
	}


	
	private final boolean isTypeTagPreferred;
	
	/**
	 * 
	 * @param method
	 */
	public ObjectListElementGetter(String fieldTag, Method method, boolean isTypeTagPreferred) {
		super(fieldTag, method);
		this.isTypeTagPreferred = isTypeTagPreferred;
	}


	@Override
	public <T> void createComposableElement(ObjectComposableScope scope) throws Exception {

		Consumer<T> consumer = new Consumer<T>() {

			@Override
			public void accept(T subObject) {
				if(subObject!=null) {
					if(isTypeTagPreferred) {
						scope.append(new ObjectComposableScope.TypeTagged(subObject));
					}
					else {
						scope.append(new ObjectComposableScope.FieldTagged(fieldTag, subObject));
					}
				}
			}
		};

		// invoke consumer on parent object
		method.invoke(scope.getObject(), new Object[]{ consumer });
	}
	
	

	@Override
	public Method getMethod() {
		return method;
	}
}
