package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.api.XML_GetElement;
import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;

public abstract class ElementGetter {


	public static abstract class Prototype {

		public abstract boolean matches(Method method);

		public abstract ElementGetter.Builder create(Method method);

	}

	public static abstract class Builder {

		Method method;

		public Builder(Method method) {
			super();
			this.method = method;
		}

		/**
		 * 
		 * @param contextBuilder
		 * @throws XML_TypeCompilationException 
		 */
		public abstract void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException;
		
		
		/**
		 * 
		 */
		public abstract void build(TypeBuilder typeBuilder);

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
			ObjectListElementGetter.PROTOTYPE,
			
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

	protected String tag;

	public ElementGetter(Method method) {
		super();
		this.method = method;

		

		XML_GetElement getElementAnnotation = method.getAnnotation(XML_GetElement.class);
		this.tag = getElementAnnotation.tag();
	}



	public String getTag(){
		return tag;
	}


	/**
	 * 
	 * @param context
	 * @param parent
	 * @param typeName
	 * @return
	 * @throws Exception 
	 */
	public abstract <T> void createComposableElement(ObjectComposableScope scope) throws Exception;



}
