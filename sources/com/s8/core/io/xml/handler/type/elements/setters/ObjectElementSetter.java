package com.s8.core.io.xml.handler.type.elements.setters;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.s8.core.io.xml.XML_Syntax;
import com.s8.core.io.xml.codebase.XML_CodebaseBuilder;
import com.s8.core.io.xml.handler.type.TypeBuilder;
import com.s8.core.io.xml.handler.type.TypeHandler;
import com.s8.core.io.xml.handler.type.XML_TypeCompilationException;
import com.s8.core.io.xml.parser.ObjectParsedScope;
import com.s8.core.io.xml.parser.ParsedScope;
import com.s8.core.io.xml.parser.XML_ParsingException;
import com.s8.core.io.xml.parser.XML_StreamReader;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class ObjectElementSetter extends ElementSetter {


	/**
	 * 
	 */
	public final static Prototype PROTOTYPE = new Prototype() {

		@Override
		public boolean matches(Class<?> fieldType) {
			return !fieldType.isPrimitive() && !fieldType.equals(String.class);
		}

		@Override
		public ElementSetter.Builder create(Method method) {
			return new ObjectElementSetter.Builder(method);
		}
	};

	
	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class Builder extends ElementSetter.Builder {


		/**
		 * type of this element
		 */
		private final Class<?> type;


		private TypeHandler typeHandler;

		public Builder(Method method) {
			super(method);
			type = method.getParameterTypes()[0];
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
		public boolean hasSubstitutionGroup() {
			return true;
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


			/* default full explanatory field tag, put single allowed type -> put pre-typed*/
			String tag = declaredTag + XML_Syntax.MAPPING_SEPARATOR + typeHandler.xml_getTag();
			declaringTypeBuilder.putElementSetter(tag, new ObjectElementSetter(method, typeHandler, false));


			if(typeHandler.hasSubTypes()) { // is polymorphic

				/* use field tag, put untyped setter */
				for(TypeHandler subFieldTypeHandler : typeHandler.getSubTypes()) {

					// standard polymorphic field
					tag = declaredTag + XML_Syntax.MAPPING_SEPARATOR + subFieldTypeHandler.xml_getTag();
					declaringTypeBuilder.putElementSetter(tag, new ObjectElementSetter(method, subFieldTypeHandler, false));	
				}
			}
			else { // type is univoque
				/* simple field tag, put single allowed type -> put pre-typed*/
				declaringTypeBuilder.putElementSetter(declaredTag, new ObjectElementSetter(method, typeHandler, true));
			}


			/* if no collision, expand */
			if(!isColliding) {
				declaringTypeBuilder.putElementSetter(typeHandler.xml_getTag(), new ObjectElementSetter(method, typeHandler, false));

				// subtypes...
				for(TypeHandler subFieldTypeHandler : typeHandler.getSubTypes()) {
					declaringTypeBuilder.putElementSetter(subFieldTypeHandler.xml_getTag(),
							new ObjectElementSetter(method, subFieldTypeHandler, false));
				}
			}
		}


		
	}



	private TypeHandler fieldTypeHandler;

	private boolean isTypeUnivoque;

	/**
	 * Pre-defined type
	 * @param tag
	 * @param method
	 * @param fieldTypeHandler
	 * @param DTD_isFieldTag 
	 */
	public ObjectElementSetter(Method method, TypeHandler fieldTypeHandler, boolean isTypeUnivoque) {
		super(method);
		this.fieldTypeHandler = fieldTypeHandler;
		this.isTypeUnivoque = isTypeUnivoque;
	}




	public TypeHandler getTypeHandler() {
		return fieldTypeHandler;
	}

	/**
	 * 
	 */
	@Override
	public ParsedScope createParsedElement(String tag, ObjectParsedScope parent, XML_StreamReader.Point point) throws XML_ParsingException {

		// retrieve parentObject
		Object parentObject = parent.getObject();

		// setter instance
		ObjectParsedScope.Callback callback = new ObjectParsedScope.Callback() {

			public @Override void set(Object value) throws XML_ParsingException {
				try {
					method.invoke(parentObject, value);
				} 
				catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
					throw new XML_ParsingException(point, e.getMessage());
				}
			}
		};

		return new ObjectParsedScope(parent, callback, tag, fieldTypeHandler, point);
	}


	/**
	 * 
	 * @return
	 */
	public boolean isTypeUnivoque() {
		return isTypeUnivoque;
	}


	@Override
	public Method getMethod() {
		return method;
	}


	@Override
	public void xsd_write(String tag, Writer writer) throws IOException {
		writer.write("\n\t\t\t<xs:element name=\""+tag+"\" type=\"");
		writer.write("tns:"+fieldTypeHandler.xsd_getTypeName()+"\"");

		/// minOccurs="1" maxOccurs="unbounded"
		writer.write(" minOccurs=\"0\" maxOccurs=\"unbounded\" />");
	}

}
