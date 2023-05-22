package com.s8.io.xml.handler.type.elements.getters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.s8.io.xml.XML_Syntax;
import com.s8.io.xml.codebase.XML_CodebaseBuilder;
import com.s8.io.xml.composer.ObjectComposableScope;
import com.s8.io.xml.composer.XML_ComposingException;
import com.s8.io.xml.handler.type.TypeBuilder;
import com.s8.io.xml.handler.type.TypeHandler;
import com.s8.io.xml.handler.type.XML_TypeCompilationException;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ObjectsCollectionElementGetter extends ElementGetter {

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
			return new ObjectsCollectionElementGetter.Builder(method);
		}
	};


	public static class Builder extends ElementGetter.Builder {

		public final Class<?> componentType;

		private TypeHandler componentTypeHandler;


		public Builder(Method method) {
			super(method);

			Parameter[] parameters = method.getParameters();
			ParameterizedType argType = (ParameterizedType) parameters[0].getParameterizedType();
			this.componentType = (Class<?>) (argType.getActualTypeArguments())[0];
		}


		@Override
		public void explore(XML_CodebaseBuilder contextBuilder) throws XML_TypeCompilationException {
			contextBuilder.discover(componentType);
		}

		@Override
		public void link(XML_CodebaseBuilder codebaseBuilder) throws XML_TypeCompilationException {
			/* retrieve fieldType builder */
			TypeBuilder typeBuilder = codebaseBuilder.getTypeBuilder(componentType);
			if(typeBuilder == null) {
				throw new XML_TypeCompilationException("failed to retrieve type builder: "+componentType.getName());
			}
			componentTypeHandler= typeBuilder.typeHandler;
		}


		@Override
		public Set<String> getSubstitutionGroup() {
			Set<String> group = new HashSet<>();
			group.add(componentTypeHandler.xml_getTag());
			for(TypeHandler handler : componentTypeHandler.getSubTypes()) { group.add(handler.xml_getTag()); }
			return group;
		}


		@Override
		public boolean isColliding(Set<String> substitutionGroup) {
			if(substitutionGroup.contains(componentTypeHandler.xml_getTag())) {
				return true;
			}
			for(TypeHandler handler : componentTypeHandler.getSubTypes()) {
				if(substitutionGroup.contains(handler.xml_getTag())) {
					return true;
				}
			}
			return false;
		}


		@Override
		public void build(TypeBuilder declaringTypeBuilder, boolean isColliding) throws XML_TypeCompilationException {
			String tag = "";
			if(!componentTypeHandler.hasSubTypes()) {
				tag = declaredTag;
			}
			else if(!isColliding) { /* substitution group non-colliding */
				tag = componentTypeHandler.xml_getTag();
			}
			else {
				tag = declaredTag + XML_Syntax.MAPPING_SEPARATOR + componentTypeHandler.xml_getTag();
			}

			declaringTypeBuilder.addElementGetter(new ObjectElementGetter(tag, method));			
		}
	}



	/**
	 * 
	 * @param method
	 */
	public ObjectsCollectionElementGetter(String fieldTag, Method method) {
		super(fieldTag, method);
	}


	@Override
	public <T> void createComposableElement(ObjectComposableScope scope) throws XML_ComposingException {

		Consumer<T> consumer = new Consumer<T>() {

			@Override
			public void accept(T subObject) {
				if(subObject!=null) {
					scope.append(new ObjectComposableScope(tag, subObject));
				}
			}
		};

		// invoke consumer on parent object
		try {
			method.invoke(scope.getObject(), new Object[]{ consumer });
		} 
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new XML_ComposingException(e.getMessage()+ "for "+method);
		}
	}



	@Override
	public Method getMethod() {
		return method;
	}
}
