package com.qx.lang.xml.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author pc
 *
 */
public class XML_Context {

	
	private Map<String, TypeHandler> typeHandlers = new HashMap<>();
	
	public XML_Context(Class<?>... types) {
		super();
		for(Class<?> type : types){
			new TypeHandler(this, type);
		}
	}
	
	protected void add(String name, TypeHandler typeHandler){
		typeHandlers.put(name, typeHandler);
	}
	
	public boolean isRegistered(String name){
		return typeHandlers.containsKey(name);
	}
	
	
}
