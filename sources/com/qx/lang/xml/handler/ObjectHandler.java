package com.qx.lang.xml.handler;

import java.lang.reflect.InvocationTargetException;

public class ObjectHandler {

	public TypeHandler typeHandler;
	
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
	public ObjectHandler(TypeHandler typeHandler)
			throws InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException {
		super();
		this.typeHandler = typeHandler;
		this.object = typeHandler.create();
	}
	
}
