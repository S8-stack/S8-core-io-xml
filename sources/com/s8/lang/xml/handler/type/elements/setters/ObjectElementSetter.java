package com.s8.lang.xml.handler.type.elements.setters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.lang.xml.api.XML_SetElement;
import com.s8.lang.xml.handler.XML_Context;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.TypeHandler;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;
import com.s8.lang.xml.parser.ObjectParsedScope;
import com.s8.lang.xml.parser.ParsedScope;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader;

public class ObjectElementSetter extends ElementSetter {

	
	public final static Prototype PROTOTYPE = new Prototype() {
		
		@Override
		public boolean matches(Class<?> fieldType) {
			return !fieldType.isPrimitive() && !fieldType.equals(String.class);
		}
		
		@Override
		public ElementSetter.Builder create(Method method) {
			XML_SetElement setElementAnnotation = method.getAnnotation(XML_SetElement.class);
			String tag = setElementAnnotation.tag();
			return new ObjectElementSetter.Builder(tag, method, method.getParameterTypes()[0]);
		}
	};
	
	public static class Builder extends ElementSetter.Builder {


		private String fieldTag;

		private Method method;

		private Class<?> fieldType;
		
		private TypeBuilder fieldTypeBuilder;

		public Builder(String tag, Method method, Class<?> fieldType) {
			super(tag);
			this.fieldTag = tag;
			this.method = method;
			this.fieldType = fieldType;
		}


		@Override
		public void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {
			contextBuilder.register(fieldType);
		}
		

		@Override
		public boolean build0(XML_ContextBuilder contextBuilder, TypeBuilder typeBuilder) {

			if(fieldTypeBuilder==null) {
				fieldTypeBuilder = contextBuilder.getTypeBuilder(fieldType);
			}
			
			if(!fieldTypeBuilder.isInheritanceDiscovered()) {
				return true; // not done, still need to discover field type inheritance
			}

			TypeHandler fieldTypeHandler = fieldTypeBuilder.getHandler();


			if(fieldTypeHandler.hasSubTypes()) { // is polymorphic
				/* use field tag, put untyped setter */
				typeBuilder.setElementSetter(new ObjectElementSetter(fieldTag, method));	
			}
			else { 
				/* suse field tag, put single allowed type -> put pre-typed*/
				typeBuilder.setElementSetter(new ObjectElementSetter(fieldTag, method, fieldTypeHandler));
			}

			return false;
		}


		@Override
		public boolean build1(XML_ContextBuilder contextBuilder, TypeBuilder typeBuilder) {
			
			//TypeHandler typeHandler = typeBuilder.getHandler();
			TypeHandler fieldTypeHandler = fieldTypeBuilder.getHandler();
			
			/* search for collisions */
			boolean isSubstitutionGroupColliding = false;
			
			// check main type
			if(typeBuilder.isSetElementColliding(fieldTypeHandler.getXmlTag())) {
				isSubstitutionGroupColliding = true;
			}
			
			// 
			TypeHandler[] subTypes = fieldTypeHandler.getSubTypes();
			int n = subTypes.length;
			int i=0;
			TypeHandler subType;
			while(!isSubstitutionGroupColliding && i<n) {
				subType = subTypes[i++];
				if(typeBuilder.isSetElementColliding(subType.getXmlTag())){
					isSubstitutionGroupColliding = true;
				}
			}
			
			/* if no collision, expand */
			if(!isSubstitutionGroupColliding) {
				typeBuilder.setElementSetter(new ObjectElementSetter(fieldTypeHandler.getXmlTag(), method, fieldTypeHandler));
				for(TypeHandler handler : fieldTypeHandler.getSubTypes()) {
					String typeTag = handler.getXmlTag();
					typeBuilder.setElementSetter(new ObjectElementSetter(typeTag, method, handler));
				}
			}
			
			return false;
		}


	}

	
	private Method method;

	private boolean isTypeDefined;

	private TypeHandler handler;


	/**
	 * Untyped setter (type must ne define inline by attribute)
	 * @param tag
	 * @param method
	 */
	public ObjectElementSetter(String tag, Method method) {
		super(tag);
		this.method = method;
		isTypeDefined = false;
	}


	/**
	 * Pre-defined type
	 * @param tag
	 * @param method
	 * @param handler
	 */
	public ObjectElementSetter(String tag, Method method, TypeHandler handler) {
		super(tag);
		this.method = method;
		this.handler = handler;
		isTypeDefined = true;
	}



	public boolean isTypeDefined() {
		return isTypeDefined;
	}

	public TypeHandler getTypeHandler() {
		return handler;
	}

	/**
	 * 
	 */
	@Override
	public ParsedScope createParsedElement(XML_Context context, 
			ObjectParsedScope parent, XML_StreamReader.Point point) throws XML_ParsingException {

		// retrieve parentObject
		Object parentObject = parent.getObject();

		// setter instance
		ObjectParsedScope.Callback callback = new ObjectParsedScope.Callback() {

			public @Override void set(Object value) throws XML_ParsingException {
				try {
					method.invoke(parentObject, value);
				} 
				catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					throw new XML_ParsingException(point, e.getMessage());
				}
			}
		};

		if(isTypeDefined) {
			return new ObjectParsedScope(context, parent, callback, tag, handler, point);
		}
		else {
			return new ObjectParsedScope(context, parent, callback, tag, point);
		}
	}
	
	@Override
	public Method getMethod() {
		return method;
	}

}
