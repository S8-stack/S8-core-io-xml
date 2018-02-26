package com.qx.lang.xml.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import com.qx.lang.xml.context.XML_Context;
import com.qx.lang.xml.parser.ArrayElementBuilder;
import com.qx.lang.xml.parser.ElementBuilder;
import com.qx.lang.xml.parser.MapElementBuilder;
import com.qx.lang.xml.parser.ObjectElementBuilder;
import com.qx.lang.xml.parser.PrimitiveElementBuilder;

public abstract class ElementSetter {


	public static ElementSetter create(XML_Context context, Method method, String name) throws Exception{

		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=1){
			throw new RuntimeException("Illegal number of parameters for a setter");
		}

		Class<?> type = parameters[0];
		
		
		if(type==boolean.class){
			return new BooleanElementSetter(method, name);
		}
		else if(type==short.class){
			return new ShortElementSetter(method, name);
		}
		else if(type==int.class){
			return new IntegerElementSetter(method, name);
		}
		else if(type==long.class){
			return new LongElementSetter(method, name);
		}
		else if(type==float.class){
			return new FloatElementSetter(method, name);
		}
		else if(type==double.class){
			return new DoubleElementSetter(method, name);
		}
		else if(type==String.class){
			return new StringElementSetter(method, name);
		}
		else if(type.isArray()){
			context.discover(type.getComponentType());
			return new ArrayElementSetter(method, name, type);
		}
		else if(Map.class.isAssignableFrom(type)){

			Class<?> valueType =
					(Class<?>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0];
			context.discover(valueType);
			return new MapElementSetter(method, name);
		}
		else{
			context.discover(type);
			return new ObjectElementSetter(method, name, type);
		}
	}

	protected Method method;
	
	protected String name;

	public ElementSetter(Method method, String name) {
		super();
		this.method = method;
		this.name = name;
	}


	public abstract ElementType getElementType();

	public void set(Object parent, Object value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		method.invoke(parent, value);
	}
	
	
	/**
	 * 
	 * @param context
	 * @param parent
	 * @param typeName
	 * @return
	 * @throws Exception 
	 */
	public abstract ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
			throws Exception;


	public static class BooleanElementSetter extends ElementSetter {
		public BooleanElementSetter(Method method, String name) { super(method, name); }
		public Class<?> getType(){ return boolean.class; }
		public @Override ElementType getElementType() { return ElementType.BOOLEAN; }
		public @Override ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new PrimitiveElementBuilder.BooleanElementBuilder(context, parent, name);
		}
	}
	
	public static class ShortElementSetter extends ElementSetter {
		public ShortElementSetter(Method method, String name) { super(method, name); }
		public Class<?> getType(){ return short.class; }
		public @Override ElementType getElementType() { return ElementType.SHORT; }
		public @Override ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new PrimitiveElementBuilder.ShortElementBuilder(context, parent, name);
		}
	}
	
	public static class IntegerElementSetter extends ElementSetter {
		public IntegerElementSetter(Method method, String name) { super(method, name); }
		public Class<?> getType(){ return int.class; }
		public @Override ElementType getElementType() { return ElementType.INTEGER; }
		public @Override ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new PrimitiveElementBuilder.IntegerElementBuilder(context, parent, name);
		}
	}
	
	public static class LongElementSetter extends ElementSetter {
		public LongElementSetter(Method method, String name) { super(method, name); }
		public Class<?> getType(){ return long.class; }
		public @Override ElementType getElementType() { return ElementType.LONG; }
		public @Override ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new PrimitiveElementBuilder.LongElementBuilder(context, parent, name);
		}
	}
	
	public static class FloatElementSetter extends ElementSetter {
		public FloatElementSetter(Method method, String name) { super(method, name); }
		public Class<?> getType(){ return float.class; }
		public @Override ElementType getElementType() { return ElementType.FLOAT; }
		public @Override ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new PrimitiveElementBuilder.FloatElementBuilder(context, parent, name);
		}
	}
	
	public static class DoubleElementSetter extends ElementSetter {
		public DoubleElementSetter(Method method, String name) { super(method, name); }
		public Class<?> getType(){ return double.class; }
		public @Override ElementType getElementType() { return ElementType.DOUBLE; }
		public @Override ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new PrimitiveElementBuilder.DoubleElementBuilder(context, parent, name);
		}
	}
	
	public static class StringElementSetter extends ElementSetter {
		public StringElementSetter(Method method, String name) { super(method, name); }
		public Class<?> getType(){ return String.class; }
		public @Override ElementType getElementType() { return ElementType.STRING; }
		public @Override ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new PrimitiveElementBuilder.StringElementBuilder(context, parent, name);
		}
	}





	public static class ObjectElementSetter extends ElementSetter {

		private Class<?> type;

		public ObjectElementSetter(Method method, String name, Class<?> type) {
			super(method, name);
			this.type = type;
		}


		public Class<?> getType(){
			return type;
		}

		@Override
		public ElementType getElementType() {
			return ElementType.OBJECT;
		}


		@Override
		public ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new ObjectElementBuilder(context, parent, name, typeName);
		}

	}

	public static class ArrayElementSetter extends ElementSetter {

		private Class<?> componentType;

		public ArrayElementSetter(Method method, String name, Class<?> type) {
			super(method, name);
			this.componentType = type.getComponentType();
		}

		@Override
		public ElementType getElementType() {
			return ElementType.ARRAY;
		}

		public Class<?> getComponentType() {
			return componentType;
		}
		
		@Override
		public ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new ArrayElementBuilder(context, (ObjectElementBuilder) parent, name, componentType);
		}
	}

	public static class MapElementSetter extends ElementSetter {

		public MapElementSetter(Method method, String name) {
			super(method, name);
		}

		@Override
		public ElementType getElementType() {
			return ElementType.MAP;
		}

		@Override
		public ElementBuilder createElementBuilder(XML_Context context, ElementBuilder parent, String typeName)
				throws Exception {
			return new MapElementBuilder(context, (ObjectElementBuilder) parent, name);
		}
	}

}
