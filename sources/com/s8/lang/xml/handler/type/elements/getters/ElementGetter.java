package com.s8.lang.xml.handler.type.elements.getters;

import java.lang.reflect.Method;

import com.s8.lang.xml.api.XML_GetElement;
import com.s8.lang.xml.composer.ObjectComposableScope;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.TypeHandler;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;

public abstract class ElementGetter {


	public static abstract class Prototype {

		public abstract boolean matches(Method method);

		public abstract ElementGetter.Builder create(Method method);

	}

	public static abstract class Builder {
		
		protected boolean isBuilt0;
		
		protected boolean isBuilt1;		

		protected Method method;
		
		protected String fieldTag;

		public Builder(Method method) {
			super();
			this.method = method;
			
			XML_GetElement getElementAnnotation = method.getAnnotation(XML_GetElement.class);
			this.fieldTag = getElementAnnotation.tag();
		}

		/**
		 * 
		 * @param contextBuilder
		 * @throws XML_TypeCompilationException 
		 */
		public abstract void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException;
		
		
		/**
		 * @throws XML_TypeCompilationException 
		 * 
		 */
		public abstract boolean build0(TypeBuilder typeBuilder) throws XML_TypeCompilationException;

		public abstract boolean build1(XML_ContextBuilder contextBuilder, TypeBuilder typeBuilder) throws XML_TypeCompilationException;

		
		/**
		 *  check collision
		 * @param contextBuilder
		 * @param typeBuilder
		 * @return true if colliding, false otherwise
		 */
		public boolean isFieldTypeTagColliding(TypeBuilder typeBuilder, TypeBuilder fieldTypeBuilder) {
			boolean isColliding = false;
			TypeHandler[] subTypes = fieldTypeBuilder.getHandler().getSubTypes();
			int n = subTypes.length, i=0;
			while(!isColliding && i<n) {
				TypeHandler subType = subTypes[i++];
				if(typeBuilder.isGetElementColliding(subType.xml_getTag())) {
					isColliding = true;
				}
			}
			return isColliding;
		}
		
		
		/**
		 * 
		 * @param typeBuilder
		 * @param fieldTypeBuilder
		 * @throws XML_TypeCompilationException
		 */
		public void fillFieldTypeTags(TypeBuilder typeBuilder, TypeBuilder fieldTypeBuilder) throws XML_TypeCompilationException {
			boolean isColliding = false;
			TypeHandler[] subTypes = fieldTypeBuilder.getHandler().getSubTypes();
			int n = subTypes.length, i=0;
			while(!isColliding && i<n) {
				TypeHandler subType = subTypes[i++];
				typeBuilder.putElementGetterTag(subType.xml_getTag());
			}
		}
					
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





	protected String fieldTag;
	
	protected Method method;


	public ElementGetter(String tag, Method method) {
		super();
		this.fieldTag = tag;
		this.method = method;
	}



	public String getTag(){
		return fieldTag;
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



	/**
	 * 
	 * @return underlying method
	 */
	public abstract Method getMethod();



}
