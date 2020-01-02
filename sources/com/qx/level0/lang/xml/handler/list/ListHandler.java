package com.qx.level0.lang.xml.handler.list;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.handler.type.TypeHandler;
import com.qx.level0.lang.xml.handler.type.XML_TypeCompilationException;
import com.qx.level0.lang.xml.parser.Parsed;
import com.qx.level0.lang.xml.parser.ParsedListElement;
import com.qx.level0.lang.xml.parser.XML_ParsingException;
import com.qx.level0.lang.xml.parser.XML_StreamReader.Point;

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
	 * @throws XML_TypeCompilationException 
	 * 
	 */
	public ListHandler(XML_Context context, TypeHandler handler) throws XML_TypeCompilationException{
		super();
		this.handler = handler;
		handler.initialize(context);
		
		setters = new HashMap<>();
		
		// root types
		setters.put(handler.getXmlTag(), new ElementItemSetter(handler));
		
		for(TypeHandler subTypeHanlder : handler.getSubTypes()) {
			setters.put(subTypeHanlder.getXmlTag(), new ElementItemSetter(subTypeHanlder));
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


	public Parsed createParsedElement(ParsedListElement parent, String tag, Point point) 
			throws XML_ParsingException {
		ElementItemSetter setter = setters.get(tag);
		if(setter==null) {
			throw new XML_ParsingException(point, "Failed to find field matching tag: "+tag);
		}
		return setter.createParseElement(parent, point);
	}
	
	
}
