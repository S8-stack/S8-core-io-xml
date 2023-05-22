package com.s8.io.xml.handler.type.elements.getters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

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

		public final Class<?> type;

		private TypeHandler typeHandler;

		public Builder(Method method) {
			super(method);
			type = method.getReturnType();
		}

		@Override
		public void explore(XML_CodebaseBuilder codebaseBuilder) throws XML_TypeCompilationException {
			codebaseBuilder.discover(type);
		}


		@Override
		public void link(XML_CodebaseBuilder codebaseBuilder) throws XML_TypeCompilationException {
			/* retrieve fieldType builder */
			TypeBuilder typeBuilder = codebaseBuilder.getTypeBuilder(type);
			if(typeBuilder == null) {
				throw new XML_TypeCompilationException("failed to retrieve type builder: "+type.getName());
			}
			typeHandler= typeBuilder.typeHandler;
		}


		@Override
		public Set<String> getSubstitutionGroup() {
			Set<String> group = new HashSet<>();
			group.add(typeHandler.xml_getTag());
			for(TypeHandler handler : typeHandler.getSubTypes()) { group.add(handler.xml_getTag()); }
			return group;
		}


		@Override
		public boolean isColliding(Set<String> substitutionGroup) {
			if(substitutionGroup.contains(typeHandler.xml_getTag())) {
				return true;
			}
			for(TypeHandler handler : typeHandler.getSubTypes()) {
				if(substitutionGroup.contains(handler.xml_getTag())) {
					return true;
				}
			}
			return false;
		}




		@Override
		public void build(TypeBuilder declaringTypeBuilder, boolean isColliding) throws XML_TypeCompilationException {

			String tag = "";
			if(!typeHandler.hasSubTypes()) {
				tag = declaredTag;
			}
			else if(!isColliding) { /* substitution group non-colliding */
				tag = typeHandler.xml_getTag();
			}
			else {
				tag = declaredTag + XML_Syntax.MAPPING_SEPARATOR + typeHandler.xml_getTag();
			}

			declaringTypeBuilder.addElementGetter(new ObjectElementGetter(tag, method));

		}
	}

	/**
	 * 
	 * @param method
	 */
	public ObjectElementGetter(String tag, Method method) {
		super(tag, method);
	}


	@Override
	public <T> void createComposableElement(ObjectComposableScope scope) throws XML_ComposingException  {

		// invoke consumer on parent object
		try {
			Object subObject = method.invoke(scope.getObject());
			scope.append(new ObjectComposableScope(tag, subObject));
		} 
		catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new XML_ComposingException("failed to use method: "+method);
		}
	}

	@Override
	public Method getMethod() {
		return method;
	}
}
