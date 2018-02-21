package com.qx.lang.xml.parser;

import java.lang.reflect.InvocationTargetException;

import com.qx.lang.xml.handler.TypeHandler;

public class ObjectBuilder {

	/**
	 * 
	 */
	public TypeHandler typeHandler;
	
	
	/**
	 * 
	 */
	public Object object;

	/**
	 * 
	 * @param typeHandler
	 * @param object
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public ObjectBuilder(TypeHandler typeHandler)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException {
		super();
		this.typeHandler = typeHandler;
		this.object = typeHandler.create();
	}
	
	

	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public void setAttribute(String name, String value) throws Exception{
		typeHandler.setAttribute(value, name, value);
	}


	/**
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public String getAttribute(Object object, String name) throws Exception{
		return typeHandler.getAttribute(object, name);
	}

	public void setValue(Object object, String value) throws Exception{
		typeHandler.setValue(object, value);
	}

	public String getValue(Object object) throws Exception {
		return typeHandler.getValue(object);
	}

	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public void setElement(String name, Object value) throws Exception{
		typeHandler.setElement(object, name, value);
	}


	/**
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public Object getElement(Object object, String name) throws Exception{
		return typeHandler.getElement(object, name);
	}
	
}
