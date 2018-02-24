package com.qx.lang.xml.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class ElementSetter {
	
	
	public static ElementSetter create(Method method){
		
		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=1){
			throw new RuntimeException("Illegal number of parameters for a setter");
		}
		
		Class<?> type = parameters[0];
		if(type.isArray()){
			return new ArrayElementSetter(method, type);
		}
		else if(Map.class.isAssignableFrom(type)){
			return new MapElementSetter(method);
		}
		else{
			return new ObjectElementSetter(method);
		}
	}

	protected Method method;
	
	
	public ElementSetter(Method method) {
		super();
		this.method = method;
	}

	
	public abstract ElementType getType();
	
	public void set(Object parent, Object value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		method.invoke(parent, value);
	}
	
	public static class ObjectElementSetter extends ElementSetter {

		public ObjectElementSetter(Method method) {
			super(method);
		}

		@Override
		public ElementType getType() {
			return ElementType.OBJECT;
		}
		
	}
	
	public static class ArrayElementSetter extends ElementSetter {

		private Class<?> componentType;
		
		public ArrayElementSetter(Method method, Class<?> type) {
			super(method);
			this.componentType = type.getComponentType();
		}

		@Override
		public ElementType getType() {
			return ElementType.ARRAY;
		}

		public Class<?> getComponentType() {
			return componentType;
		}
		
	}
	
	public static class MapElementSetter extends ElementSetter {

		public MapElementSetter(Method method) {
			super(method);
		}

		@Override
		public ElementType getType() {
			return ElementType.MAP;
		}
	}
	
}
