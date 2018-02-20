package com.qx.lang.xml.handler;

import java.util.HashMap;
import java.util.Map;

import com.qx.lang.xml.annotation.XML_Type;

/**
 * 
 * @author pc
 *
 */
public class XML_Context {

	
	private Map<String, TypeHandler> typeHandlers = new HashMap<>();
	
	/**
	 * 
	 * @param types
	 */
	public XML_Context(Class<?>... types) {
		super();
		for(Class<?> type : types){
			discover(type);
		}
	}
	
	protected void discover(Class<?> type){
		XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);
		if(typeAnnotation==null){
			throw new RuntimeException("Missing type declaration for type: "+type.getName());
		}
		String name = typeAnnotation.name();
		if(!typeHandlers.containsKey(name)){
			TypeHandler typeHandler = new TypeHandler(type);
			typeHandlers.put(name, typeHandler);
			typeHandler.initialize(this);
		}
	}
	
	public boolean isRegistered(String name){
		return typeHandlers.containsKey(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public ObjectHandler create(String name) throws Exception{
		TypeHandler typeHandler = typeHandlers.get(name);
		if(typeHandler==null){
			throw new Exception("Unknown type: "+name);
		}
		return new ObjectHandler(typeHandler);
	}
	
}
