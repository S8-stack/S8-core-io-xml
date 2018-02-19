package com.qx.lang.xml.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class FieldSetter {
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	public static FieldSetter create(Method method){
		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=1){
			throw new RuntimeException("Illegal number of parameters for a setter");
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
		else{
			throw new RuntimeException("parameters type is not supported: "+type.getName());
		}
	}

	public Method method;
	
	public FieldSetter(Method method) {
		super();
		this.method = method;
	}
	
	public abstract void set(Object object, String value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	
	private static class BooleanFieldSetter extends FieldSetter {

		public BooleanFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(object, Boolean.valueOf(value));
		}
		
	}
	
	private static class ShortFieldSetter extends FieldSetter {

		public ShortFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(object, Short.valueOf(value));
		}
		
	}
	
	private static class IntegerFieldSetter extends FieldSetter {

		public IntegerFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(object, Integer.valueOf(value));
		}
		
	}
	
	private static class LongFieldSetter extends FieldSetter {

		public LongFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(object, Long.valueOf(value));
		}
		
	}
	
	private static class FloatFieldSetter extends FieldSetter {

		public FloatFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(object, Float.valueOf(value));
		}
		
	}
	
	private static class DoubleFieldSetter extends FieldSetter {

		public DoubleFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(object, Double.valueOf(value));
		}
		
	}
	
	private static class StringFieldSetter extends FieldSetter {

		public StringFieldSetter(Method method) {
			super(method);
		}

		@Override
		public void set(Object object, String value)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(object, value);
		}
		
	}
	
}
