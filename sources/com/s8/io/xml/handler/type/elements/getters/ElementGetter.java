package com.s8.io.xml.handler.type.elements.getters;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import com.s8.io.xml.annotations.XML_GetElement;
import com.s8.io.xml.codebase.XML_CodebaseBuilder;
import com.s8.io.xml.composer.ComposableScope;
import com.s8.io.xml.composer.XML_ComposingException;
import com.s8.io.xml.handler.type.TypeBuilder;
import com.s8.io.xml.handler.type.XML_TypeCompilationException;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ElementGetter {


	public static abstract class Prototype {

		public abstract boolean matches(Method method);

		public abstract ElementGetter.Builder create(Method method);

	}

	public static abstract class Builder {

		protected boolean isBuilt0;

		protected boolean isBuilt1;		

		protected Method method;

		public final String declaredTag;

		public Builder(Method method) {
			super();
			this.method = method;

			XML_GetElement elementAnnotation = method.getAnnotation(XML_GetElement.class);
			declaredTag = elementAnnotation.tag();
		}

		/**
		 * 
		 * @param contextBuilder
		 * @throws XML_TypeCompilationException 
		 */
		public abstract void explore(XML_CodebaseBuilder contextBuilder) throws XML_TypeCompilationException;


		public abstract void link(XML_CodebaseBuilder contextBuilder) throws XML_TypeCompilationException;

		public abstract boolean hasSubstitutionGroup();
		
		public abstract Set<String> getSubstitutionGroup();

		public abstract boolean isColliding(Set<String> substitutionGroup);


		public abstract void build(TypeBuilder typeBuilder, boolean isColliding) throws XML_TypeCompilationException;

	}


	public final static Prototype[] PROTOTYPES = new Prototype[] {

			// primitives
			BooleanElementGetter.PROTOTYPE,
			ShortElementGetter.PROTOTYPE,
			IntegerElementGetter.PROTOTYPE,
			LongElementGetter.PROTOTYPE,
			FloatElementGetter.PROTOTYPE,
			DoubleElementGetter.PROTOTYPE,
			StringElementGetter.PROTOTYPE,

			// list
			ObjectsCollectionElementGetter.PROTOTYPE,

			// simple object
			ObjectElementGetter.PROTOTYPE
	};


	public static ElementGetter.Builder create(Method method) throws XML_TypeCompilationException {
		for(Prototype prototype : PROTOTYPES) {
			if(prototype.matches(method)) {
				return prototype.create(method);
			}
		}
		throw new XML_TypeCompilationException("Cannot match getter: "+method);
	}





	protected Method method;


	public ElementGetter(Method method) {
		super();
		this.method = method;
	}




	/**
	 * 
	 * @param context
	 * @param parent
	 * @param typeName
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	public abstract void compose(Object object, List<ComposableScope> subScopes) throws XML_ComposingException;



	/**
	 * 
	 * @return underlying method
	 */
	public abstract Method getMethod();



}
