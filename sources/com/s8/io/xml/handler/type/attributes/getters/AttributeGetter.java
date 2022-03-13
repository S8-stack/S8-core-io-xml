package com.s8.io.xml.handler.type.attributes.getters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.s8.io.xml.annotations.XML_GetAttribute;

public abstract class AttributeGetter {
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	public static AttributeGetter create(Method method){
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
	
	private String name;
	
	public AttributeGetter(Method method) {
		super();
		this.method = method;
		XML_GetAttribute getAttributeAnnotation = method.getAnnotation(XML_GetAttribute.class);
		this.name = getAttributeAnnotation.tag();
		
	}
	
	
	public String getName(){
		return name;
	}
	
	public abstract String get(Object object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	private static class BooleanFieldGetter extends AttributeGetter {

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
	
	private static class ShortFieldGetter extends AttributeGetter {

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
	

	
	private static class IntegerFieldGetter extends AttributeGetter {

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
	

	private static class LongFieldGetter extends AttributeGetter {

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
	


	private static class FloatFieldGetter extends AttributeGetter {

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
	

	private static class DoubleFieldGetter extends AttributeGetter {

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
	

	private static class StringFieldGetter extends AttributeGetter {

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
	
	private static class EnumFieldGetter extends AttributeGetter {

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
