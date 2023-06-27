package com.s8.io.xml.handler.type.elements.getters;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.s8.io.xml.codebase.XML_CodebaseBuilder;
import com.s8.io.xml.handler.type.TypeBuilder;
import com.s8.io.xml.handler.type.XML_TypeCompilationException;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveElementGetter extends ElementGetter {


	public static abstract class Builder extends ElementGetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		@Override
		public void explore(XML_CodebaseBuilder codebaseBuilder) throws XML_TypeCompilationException {
			// nothing to explore since primitive type
		}
		
		@Override
		public void link(XML_CodebaseBuilder contextBuilder) throws XML_TypeCompilationException {
			/* nothing to link */
		}
		
		@Override
		public boolean hasSubstitutionGroup() {
			return false;
		}
		
		@Override
		public Set<String> getSubstitutionGroup() {
			return new HashSet<>();
		}
		

		@Override
		public boolean isColliding(Set<String> substitutionGroup) {
			return false; /* since set is empty, alsways non-colliding */
		}

		public abstract PrimitiveElementGetter createGetter();
		
		
		@Override
		public void build(TypeBuilder declaringTypeBuilder, boolean isColliding) throws XML_TypeCompilationException {
			declaringTypeBuilder.addElementGetter(createGetter());
		}
	}
	
	
	public final String tag;

	public PrimitiveElementGetter(String tag, Method method) {
		super(method);
		this.tag = tag;
	}


	@Override
	public Method getMethod() {
		return method;
	}
}
