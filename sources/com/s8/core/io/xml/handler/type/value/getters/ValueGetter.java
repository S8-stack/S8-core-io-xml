package com.s8.core.io.xml.handler.type.value.getters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.s8.core.io.xml.codebase.XML_Codebase;
import com.s8.core.io.xml.composer.ComposableScope;
import com.s8.core.io.xml.composer.XML_ComposingException;
import com.s8.core.io.xml.composer.XML_StreamWriter;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ValueGetter {
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	public static ValueGetter create(Method method){
		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=0){
			throw new RuntimeException("Illegal number of parameters for a setter: "+method.getName()
			+" in type "+method.getDeclaringClass().getName());
		}
		Class<?> type = method.getReturnType();
		
		if(type==boolean.class){
			return new BooleanFieldGetter(method);
		}
		else if(type==short.class){
			return new ShortFieldGetter(method);
		}
		else if(type==int.class){
			return new IntegerFieldGetter(method);
		}
		else if(type==long.class){
			return new LongFieldGetter(method);
		}
		else if(type==float.class){
			return new FloatFieldGetter(method);
		}
		else if(type==double.class){
			return new DoubleFieldGetter(method);
		}
		else if(type==String.class){
			return new StringFieldGetter(method);
		}
		else if(type.isEnum()){
			return new EnumFieldGetter(method);
		}
		else{
			throw new RuntimeException("parameters type is not supported: "+type.getName());
		}
	}

	protected Method method;
	
	
	public ValueGetter(Method method) {
		super();
		this.method = method;
	}
	
	public abstract String get(Object object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	public void compose(Object object, List<ComposableScope> subScopes) throws XML_ComposingException {
		try {
			String value = get(object);
			subScopes.add(new ComposableScope() {
				
				@Override
				public void compose(XML_Codebase context, XML_StreamWriter writer) throws XML_ComposingException, IOException {
					writer.append(value);
				}
			});
		} 
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new XML_ComposingException(e.getMessage());
		}
	}
	
	
	private static class BooleanFieldGetter extends ValueGetter {

		public BooleanFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object attribute = method.invoke(object);
			if(attribute!=null){
				return Boolean.toString((boolean) attribute);	
			}
			else{
				return null;
			}
		}
		
	}
	
	private static class ShortFieldGetter extends ValueGetter {

		public ShortFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object attribute = method.invoke(object);
			if(attribute!=null){
				return Short.toString((short) attribute);
			}
			else{
				return null;
			} 
		}
	}
	

	
	private static class IntegerFieldGetter extends ValueGetter {

		public IntegerFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object attribute = method.invoke(object);
			if(attribute!=null){
				return Integer.toString((int) attribute);
			}
			else{
				return null;
			}
		}
	}
	

	private static class LongFieldGetter extends ValueGetter {

		public LongFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object attribute = method.invoke(object);
			if(attribute!=null){
				return Long.toString((long) attribute);
			}
			else{
				return null;
			}
		}
	}
	


	private static class FloatFieldGetter extends ValueGetter {

		public FloatFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object attribute = method.invoke(object);
			if(attribute!=null){
				return Float.toString((float) attribute);
			}
			else{
				return null;
			}
		}
	}
	

	private static class DoubleFieldGetter extends ValueGetter {

		public DoubleFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object attribute = method.invoke(object);
			if(attribute!=null){
				return Double.toString((double) attribute);
			}
			else{
				return null;
			}
		}
	}
	

	private static class StringFieldGetter extends ValueGetter {

		public StringFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			try{
				Object attribute = method.invoke(object);
				if(attribute!=null){
					return (String) attribute;
				}
				else{
					return null;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	private static class EnumFieldGetter extends ValueGetter {

		public EnumFieldGetter(Method method) {
			super(method);
		}

		@Override
		public String get(Object object)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			try{
				Object attribute = method.invoke(object);
				if(attribute!=null){
					return attribute.toString();
				}
				else{
					return null;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	
}
