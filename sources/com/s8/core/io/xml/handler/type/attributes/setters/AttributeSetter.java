package com.s8.core.io.xml.handler.type.attributes.setters;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.s8.core.io.xml.annotations.XML_SetAttribute;
import com.s8.core.io.xml.parser.XML_ParsingException;
import com.s8.core.io.xml.parser.XML_StreamReader;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class AttributeSetter {

	/**
	 * 
	 * @param method
	 * @return
	 */
	public static AttributeSetter create(Method method){
		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=1){
			throw new RuntimeException("Illegal number of parameters for a setter "+method.getName()+" in type "
					+method.getDeclaringClass().getName());
		}
		Class<?> type = parameters[0];

		if(type==boolean.class){
			return new BooleanFieldSetter(method);
		}
		else if(type==short.class){
			return new ShortFieldSetter(method);
		}
		else if(type==int.class){
			return new IntegerFieldSetter(method);
		}
		else if(type==long.class){
			return new LongFieldSetter(method);
		}
		else if(type==float.class){
			return new FloatFieldSetter(method);
		}
		else if(type==double.class){
			return new DoubleFieldSetter(method);
		}
		else if(type==String.class){
			return new StringFieldSetter(method);
		}
		else if(type.isEnum()){
			return new EnumFieldSetter(method, type);
		}
		else{
			throw new RuntimeException("parameters type is not supported as attribute: "+type.getName()
			+", for method: "+method);
		}
	}

	protected Method method;

	private String name;

	private boolean isRequired;

	public AttributeSetter(Method method) {
		super();
		this.method = method;

		XML_SetAttribute attributeAnnotation = method.getAnnotation(XML_SetAttribute.class);
		name = attributeAnnotation.name();
		isRequired = attributeAnnotation.isRequired();
	}


	public boolean isRequired() {
		return isRequired;
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void writeDTD(Writer writer) throws IOException {
		//b  CDATA  #IMPLIED
		writer.append(name);
		writer.append(' ');

		writer.append("CDATA");
		writer.append(' ');

		if(isRequired) {
			writer.append("#REQUIRED");	
		}
		else {
			writer.append("#IMPLIED");	
		}
	}

	public void XSD_write(Writer writer) throws IOException {

		// <xs:attribute name="factor" type="xs:string"/>
		writer.append("\n\t\t<xs:attribute name=\"");

		writer.append(name);

		writer.append("\" type=\"xs:string\"/>");
	}


	public abstract void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException;

	private static class BooleanFieldSetter extends AttributeSetter {

		public BooleanFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException {
			try {
				method.invoke(object, Boolean.valueOf(value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method.getName()+" due to "
						+e.getMessage());
			}
		}

	}

	private static class ShortFieldSetter extends AttributeSetter {

		public ShortFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException{
			try{
				method.invoke(object, Short.valueOf(value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method.getName()+" due to "
						+e.getMessage());
			}
		}

	}

	private static class IntegerFieldSetter extends AttributeSetter {

		public IntegerFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException {
			try{
				method.invoke(object, Integer.decode(value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method.getName()+" due to "
						+e.getMessage());
			}
		}

	}

	private static class LongFieldSetter extends AttributeSetter {

		public LongFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException {
			try{

				method.invoke(object, Long.valueOf(value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method.getName()+" due to "
						+e.getMessage());
			}
		}

	}

	private static class FloatFieldSetter extends AttributeSetter {

		public FloatFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException {
			try{
				method.invoke(object, Float.valueOf(value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method.getName()+" due to "
						+e.getMessage());
			}
		}

	}

	private static class DoubleFieldSetter extends AttributeSetter {

		public DoubleFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException {
			try{
				method.invoke(object, Double.valueOf(value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method.getName()+" due to "
						+e.getMessage());
			}
		}

	}

	private static class StringFieldSetter extends AttributeSetter {

		public StringFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException {
			try{
				method.invoke(object, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method+" due to "
						+e.getMessage());
			}
		}

	}

	private static class EnumFieldSetter extends AttributeSetter {

		private Map<String, Object> map;

		public EnumFieldSetter(Method method, Class<?> type) {
			super(method);
			map = new HashMap<>();
			for(Object enumInstance : type.getEnumConstants()){
				map.put(enumInstance.toString(), enumInstance);
			}
		}

		@Override
		public void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException {
			try{
				method.invoke(object, map.get(value));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new XML_ParsingException(point, "Cannot set value: "+value+" with method "+method.getName()+" due to "
						+e.getMessage());
			}
		}	
	}

}
