package com.qx.lang.xml.handler;

import java.util.HashMap;
import java.util.Map;

import com.qx.lang.xml.annotation.XML_Type;
import com.qx.lang.xml.parser.ObjectBuilder;

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
	 * @throws Exception 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public XML_Context(Class<?>... types) throws Exception {
		super();
		for(Class<?> type : types){
			discover(type);
		}
	}
	
	/**
	 * 
	 * @param type
	 * @throws Exception 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	protected void discover(Class<?> type) throws Exception {
		XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);
		if(typeAnnotation==null){
			throw new RuntimeException("Missing type declaration for type: "+type.getName());
		}
		String name = typeAnnotation.name();
		if(!typeHandlers.containsKey(name)){
			TypeHandler typeHandler = new TypeHandler(type);
			typeHandlers.put(name, typeHandler);
			try {
				typeHandler.initialize(this);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new Exception("Failed to initialize "+type.getName()+" due to "+e.getMessage());
			}
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
	public ObjectBuilder create(String name) throws Exception{
		TypeHandler typeHandler = typeHandlers.get(name);
		if(typeHandler==null){
			throw new Exception("Unknown type: "+name);
		}
		return new ObjectBuilder(typeHandler);
	}
	
}
