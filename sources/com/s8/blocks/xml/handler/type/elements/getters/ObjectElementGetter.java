package com.s8.blocks.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.blocks.xml.composer.ObjectComposableScope;
import com.s8.blocks.xml.handler.XML_LexiconBuilder;
import com.s8.blocks.xml.handler.type.TypeBuilder;
import com.s8.blocks.xml.handler.type.XML_TypeCompilationException;


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
		public void explore(XML_LexiconBuilder contextBuilder) throws XML_TypeCompilationException {
			Class<?> type = method.getReturnType();
			contextBuilder.register(type);
		}

		@Override
		public boolean build0(TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			if(!isBuilt0) {
				typeBuilder.putElementGetterTag(fieldTag);
				isBuilt0 = true;
				return false;
			}
			else {
				return false;
			}
		}
		
		@Override
		public boolean build1(XML_LexiconBuilder contextBuilder, TypeBuilder typeBuilder) throws XML_TypeCompilationException {
			if(!isBuilt1) {
				Class<?> fieldType =  method.getReturnType();
				TypeBuilder fieldTypeBuilder = contextBuilder.getTypeBuilder(fieldType);
				if(!fieldTypeBuilder.isInheritanceDiscovered()) {
					return true;
				}
				
				boolean isTypeTagPreferred = !isFieldTypeTagColliding(typeBuilder, fieldTypeBuilder);	
				if(isTypeTagPreferred) {
					fillFieldTypeTags(typeBuilder, fieldTypeBuilder);
				}
				typeBuilder.putElementGetter(new ObjectElementGetter(fieldTag, method, isTypeTagPreferred));
				isBuilt1 = true;
				return false;	
			}
			else {
				return false;
			}
		}
	}

	
	private boolean isTypeTagPreferred;
	
	/**
	 * 
	 * @param method
	 */
	public ObjectElementGetter(String tag, Method method, boolean isTypeTagPreferred) {
		super(tag, method);
		this.isTypeTagPreferred = isTypeTagPreferred;
	}


	@Override
	public <T> void createComposableElement(ObjectComposableScope scope) throws Exception {

		// invoke consumer on parent object
		Object subObject = method.invoke(scope.getObject());
		if(isTypeTagPreferred) {
			scope.append(new ObjectComposableScope.TypeTagged(subObject));
		}
		else {
			scope.append(new ObjectComposableScope.FieldTagged(fieldTag, subObject));
		}
	}

	@Override
	public Method getMethod() {
		return method;
	}
}
