package com.qx.level0.lang.xml.handler.list;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.qx.level0.lang.xml.handler.type.TypeHandler;

/**
 * 
 * @author pc
 *
 */
public class ListHandler {
	
	public interface Factory {
		public ListHandler create(TypeHandler handler);
	}
	
	
	/**
	 * component type handler
	 */
	private TypeHandler handler;
	
	
	private Map<String, ElementItemSetter> setters;
	
	/**
	 * 
	 */
	public ListHandler(TypeHandler handler){
		super();
		this.handler = handler;
		
		setters = new HashMap<>();
		
		// root types
		setters.put(handler.getXmlName(), new ElementItemSetter(handler));
		
		for(TypeHandler subTypeHanlder : handler.getSubTypes()) {
			setters.put(subTypeHanlder.getXmlName(), new ElementItemSetter(subTypeHanlder));
		}
	}
	

	public Class<?> getType() {
		return handler.getType();
	}
	
	
	public Set<String> getElementTags() {
		return setters.keySet();
	}
	
	
	public void traverseTypeHandler(Consumer<TypeHandler> consumer) {
		setters.forEach((k, setter)->{ consumer.accept(setter.getTypeHanlder()); });
	}
	
	
	public ElementItemSetter getElementSetter(String tag) {
		return setters.get(tag);	
	}
	
	
}
