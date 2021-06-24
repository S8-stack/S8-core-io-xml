package com.s8.blocks.xml.handler.type.elements.setters;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.alpha.xml.XML_SetElement;
import com.s8.blocks.xml.XML_Syntax;
import com.s8.blocks.xml.handler.XML_LexiconBuilder;
import com.s8.blocks.xml.handler.type.TypeBuilder;
import com.s8.blocks.xml.handler.type.TypeHandler;
import com.s8.blocks.xml.handler.type.XML_TypeCompilationException;
import com.s8.blocks.xml.parser.ObjectParsedScope;
import com.s8.blocks.xml.parser.ParsedScope;
import com.s8.blocks.xml.parser.XML_ParsingException;
import com.s8.blocks.xml.parser.XML_StreamReader;

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
		public void explore(XML_LexiconBuilder contextBuilder) throws XML_TypeCompilationException {
			contextBuilder.register(fieldType);
		}


		@Override
		public boolean build0(XML_LexiconBuilder contextBuilder, TypeBuilder typeBuilder, boolean isVerbose) {
			if(!isBuilt0) {

				if(fieldTypeBuilder==null) {
					fieldTypeBuilder = contextBuilder.getTypeBuilder(fieldType);
				}

				if(!fieldTypeBuilder.isInheritanceDiscovered()) {
					return true; // not done, still need to discover field type inheritance
				}

				TypeHandler fieldTypeHandler = fieldTypeBuilder.getHandler();

				/* default full explanatory field tag, put single allowed type -> put pre-typed*/
				tag = fieldTag+XML_Syntax.MAPPING_SEPARATOR+fieldTypeHandler.xml_getTag();
				typeBuilder.setElementSetter(new ObjectElementSetter(tag, true, method, fieldTypeHandler, false));


				if(fieldTypeHandler.hasSubTypes()) { // is polymorphic
					String tag;

					/* use field tag, put untyped setter */
					for(TypeHandler subFieldTypeHandler : fieldTypeHandler.getSubTypes()) {

						// standard polymorphic field
						tag = fieldTag+XML_Syntax.MAPPING_SEPARATOR+subFieldTypeHandler.xml_getTag();
						typeBuilder.setElementSetter(new ObjectElementSetter(tag, true, method, subFieldTypeHandler, false));	
					}
				}
				else { // type is univoque
					/* simple field tag, put single allowed type -> put pre-typed*/
					typeBuilder.setElementSetter(new ObjectElementSetter(fieldTag, true, method, fieldTypeHandler, true));

				}

				isBuilt0 = true;
				return false;
			}
			else {
				return false;
			}
		}


		@Override
		public boolean build1(XML_LexiconBuilder contextBuilder, TypeBuilder typeBuilder, boolean isVerbose) {			
			if(!isBuilt1) {

				//TypeHandler typeHandler = typeBuilder.getHandler();
				TypeHandler fieldTypeHandler = fieldTypeBuilder.getHandler();

				/* search for collisions */
				boolean isSubstitutionGroupColliding = false;

				// check main type
				if(typeBuilder.isSetElementColliding(fieldTypeHandler.xml_getTag())) {
					isSubstitutionGroupColliding = true;
				}
				
				if(isSubstitutionGroupColliding) {
					System.out.println("Collision on setters name for method: "+method);
				}

				//
				if(!isSubstitutionGroupColliding) {
					TypeHandler[] subTypes = fieldTypeHandler.getSubTypes();
					int n = subTypes.length;
					int i=0;
					TypeHandler subType;
					while(!isSubstitutionGroupColliding && i<n) {
						subType = subTypes[i++];
						if(typeBuilder.isSetElementColliding(subType.xml_getTag())){
							isSubstitutionGroupColliding = true;
						}
					}	
				}


				/* if no collision, expand */
				if(!isSubstitutionGroupColliding) {
					typeBuilder.setElementSetter(new ObjectElementSetter(fieldTypeHandler.xml_getTag(), 
							false, method, fieldTypeHandler, false));

					// subtypes...
					for(TypeHandler fieldSubTypeHandler : fieldTypeHandler.getSubTypes()) {
						String typeTag = fieldSubTypeHandler.xml_getTag();
						typeBuilder.setElementSetter(new ObjectElementSetter(typeTag, 
								false, method, fieldSubTypeHandler, false));
					}
				}

				isBuilt1 = true;
				return false;
			}
			else {
				return false;
			}
		}


	}


	private Method method;

	private TypeHandler fieldTypeHandler;

	private boolean isTypeUnivoque;

	/**
	 * Pre-defined type
	 * @param tag
	 * @param method
	 * @param fieldTypeHandler
	 * @param DTD_isFieldTag 
	 */
	public ObjectElementSetter(String tag, boolean DTD_isFieldTag, Method method, TypeHandler fieldTypeHandler, boolean isTypeUnivoque) {
		super(tag, DTD_isFieldTag);
		this.method = method;
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
	public ParsedScope createParsedElement(ObjectParsedScope parent, XML_StreamReader.Point point) throws XML_ParsingException {

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

		return new ObjectParsedScope(parent, callback, getTag(), fieldTypeHandler, point);
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
	public void xsd_write(Writer writer) throws IOException {
		writer.write("\n\t\t\t<xs:element name=\""+getTag()+"\" type=\"");
		writer.write("tns:"+fieldTypeHandler.xsd_getTypeName()+"\"");
		
		/// minOccurs="1" maxOccurs="unbounded"
		writer.write(" minOccurs=\"0\" maxOccurs=\"unbounded\" />");
	}

	@Override
	public void DTD_writeHeader(Writer writer) throws IOException {
		writer.append(getTag());
		writer.append("*");
	}


	@Override
	public void DTD_writeFieldDefinition(TypeHandler typeHandler, Writer writer) throws IOException {
		if(isFieldTag()) {
			fieldTypeHandler.DTD_typeGenerator.writeFieldElement(getTag(), writer);
		}
	}
}
