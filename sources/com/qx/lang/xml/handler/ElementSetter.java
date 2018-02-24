package com.qx.lang.xml.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

public abstract class ElementSetter {
	
	
	public static ElementSetter create(XML_Context context, Method method) throws Exception{
		
		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=1){
			throw new RuntimeException("Illegal number of parameters for a setter");
		}
		
		Class<?> type = parameters[0];
		if(type.isArray()){
			context.discover(type.getComponentType());
			return new ArrayElementSetter(method, type);
		}
		else if(Map.class.isAssignableFrom(type)){
			
			Class<?> valueType =
					(Class<?>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0];
			context.discover(valueType);
			return new MapElementSetter(method);
		}
		else{
			context.discover(type);
			return new ObjectElementSetter(method, type);
		}
	}

	protected Method method;
	
	public ElementSetter(Method method) {
		super();
		this.method = method;
	}

	
	public abstract ElementType getElementType();
	
	public void set(Object parent, Object value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		method.invoke(parent, value);
	}
	
	public static class ObjectElementSetter extends ElementSetter {

		private Class<?> type;
		
		public ObjectElementSetter(Method method, Class<?> type) {
			super(method);
			this.type = type;
		}

		
		public Class<?> getType(){
			return type;
		}
		
		@Override
		public ElementType getElementType() {
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
		public ElementType getElementType() {
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
		public ElementType getElementType() {
			return ElementType.MAP;
		}
	}
	
}
