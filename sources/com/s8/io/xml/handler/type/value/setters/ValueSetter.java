package com.s8.io.xml.handler.type.value.setters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.xml.parser.XML_ParsingException;
import com.s8.io.xml.parser.XML_StreamReader;

public abstract class ValueSetter {

	/**
	 * 
	 * @param method
	 * @return
	 */
	public static ValueSetter create(Method method){
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
			throw new RuntimeException("parameters type is not supported: "+type.getName());
		}
	}

	protected Method method;
	

	public ValueSetter(Method method) {
		super();
		this.method = method;
		
		//XML_SetValue attributeAnnotation = method.getAnnotation(XML_SetValue.class);
		
	}
	
	

	public abstract void set(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException;

	private static class BooleanFieldSetter extends ValueSetter {

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

	private static class ShortFieldSetter extends ValueSetter {

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

	private static class IntegerFieldSetter extends ValueSetter {

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

	private static class LongFieldSetter extends ValueSetter {

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

	private static class FloatFieldSetter extends ValueSetter {

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

	private static class DoubleFieldSetter extends ValueSetter {

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

	private static class StringFieldSetter extends ValueSetter {

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

	private static class EnumFieldSetter extends ValueSetter {

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
