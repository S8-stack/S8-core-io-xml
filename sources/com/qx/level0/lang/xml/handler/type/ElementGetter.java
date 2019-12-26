package com.qx.level0.lang.xml.handler.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.qx.level0.lang.xml.annotation.XML_GetElement;
import com.qx.level0.lang.xml.composer.ArrayComposableElement;
import com.qx.level0.lang.xml.composer.ComposableElement;
import com.qx.level0.lang.xml.composer.ObjectComposableElement;
import com.qx.level0.lang.xml.composer.PrimitiveComposableElement.BooleanComposableElement;
import com.qx.level0.lang.xml.composer.PrimitiveComposableElement.DoubleComposableElement;
import com.qx.level0.lang.xml.composer.PrimitiveComposableElement.FloatComposableElement;
import com.qx.level0.lang.xml.composer.PrimitiveComposableElement.IntegerComposableElement;
import com.qx.level0.lang.xml.composer.PrimitiveComposableElement.LongComposableElement;
import com.qx.level0.lang.xml.composer.PrimitiveComposableElement.ShortComposableElement;
import com.qx.level0.lang.xml.composer.PrimitiveComposableElement.StringComposableElement;
import com.qx.level0.lang.xml.composer.XML_Composer;

public abstract class ElementGetter {

	
	public static ElementGetter create(Method method){
		Class<?> type = method.getReturnType();
		if(type==boolean.class){
			return new BooleanElementGetter(method);
		}
		else if(type==short.class){
			return new ShortElementGetter(method);
		}
		else if(type==int.class){
			return new IntegerElementGetter(method);
		}
		else if(type==long.class){
			return new LongElementGetter(method);
		}
		else if(type==float.class){
			return new FloatElementGetter(method);
		}
		else if(type==double.class){
			return new DoubleElementGetter(method);
		}
		else if(type==String.class){
			return new StringElementGetter(method);
		}
		else if(type.isArray()){
			return new ArrayElementGetter(method);
		}
		else {
			return new ObjectElementGetter(method);
		}
	}
	
	private Method method;
	
	private String name;

	public ElementGetter(Method method) {
		super();
		this.method = method;
		
		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=0){
			throw new RuntimeException("Illegal number of parameters for a setter");
		}
		
		XML_GetElement getElementAnnotation = method.getAnnotation(XML_GetElement.class);
		this.name = getElementAnnotation.name();
	}
	
	
	/**
	 * 
	 * @param object
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object getValue(Object object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return method.invoke(object, new Object[]{});
	}
	
	
	public String getName(){
		return name;
	}
	

	/**
	 * 
	 * @param context
	 * @param parent
	 * @param typeName
	 * @return
	 * @throws Exception 
	 */
	public abstract ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception;
	
	
	
	private static class ObjectElementGetter extends ElementGetter {

		public ObjectElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new ObjectComposableElement(composer, getName(), fieldValue);	
			}
			else{
				return null;
			}	
		}
	}
	
	private static class ArrayElementGetter extends ElementGetter {

		public ArrayElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new ArrayComposableElement(composer, getName(), fieldValue);	
			}
			else{
				return null;
			}	
		}
	}
	
	public static class BooleanElementGetter extends ElementGetter {

		public BooleanElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new BooleanComposableElement(composer, getName(), fieldValue);
			}
			else{
				return null;
			}	
		}
	}
	
	public static class ShortElementGetter extends ElementGetter {

		public ShortElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new ShortComposableElement(composer, getName(), fieldValue);
			}
			else{
				return null;
			}	
		}
	}
	
	public static class IntegerElementGetter extends ElementGetter {

		public IntegerElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new IntegerComposableElement(composer, getName(), fieldValue);
			}
			else{
				return null;
			}	
		}
	}
	
	public static class LongElementGetter extends ElementGetter {

		public LongElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new LongComposableElement(composer, getName(), fieldValue);
			}
			else{
				return null;
			}	
		}
	}

	
	public static class FloatElementGetter extends ElementGetter {

		public FloatElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new FloatComposableElement(composer, getName(), fieldValue);
			}
			else{
				return null;
			}	
		}
	}
	
	public static class DoubleElementGetter extends ElementGetter {

		public DoubleElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new DoubleComposableElement(composer, getName(), fieldValue);
			}
			else{
				return null;
			}	
		}
	}
	
	public static class StringElementGetter extends ElementGetter {

		public StringElementGetter(Method method) {
			super(method);
		}

		@Override
		public ComposableElement createComposableElement(XML_Composer composer, Object parent) throws Exception {
			Object fieldValue = getValue(parent);
			if(fieldValue!=null){
				return new StringComposableElement(composer, getName(), fieldValue);
			}
			else{
				return null;
			}	
		}
	}

}
