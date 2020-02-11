package com.qx.level0.lang.xml.handler.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qx.level0.lang.xml.parser.Parsed;
import com.qx.level0.lang.xml.parser.ParsedObjectElement;
import com.qx.level0.lang.xml.parser.XML_ParsingException;
import com.qx.level0.lang.xml.parser.XML_StreamReader;

/**
 * 
 * @author pc
 *
 */
public class TypeHandler {

	private Class<?> type;


	/**
	 * declared name in XML. Go in tag. (XML side)
	 */
	String xmlName;

	/**
	 * declared name in Class<?> (JAVA side)
	 */
	private String className;


	/**
	 * mapping of all subclasses
	 * (package-private for external construction)
	 */
	TypeHandler[] subTypes;


	/**
	 * Intentionally left package private
	 */
	int nLists;


	/** ready for ext construction */
	Constructor<?> constructor;

	/** ready for ext construction */
	AttributeGetter valueGetter;

	/** ready for ext construction */
	AttributeSetter valueSetter;

	/** ready for ext construction */
	List<AttributeGetter> attributeGetters = new ArrayList<>();

	/** ready for ext construction */
	Map<String, AttributeSetter> attributeSetters = new HashMap<>();

	/** ready for ext construction */
	List<ElementGetter> elementGetters = new ArrayList<>();

	/**
	 * Mapping of all possible ways of settings field (with element setters)
	 */
	Map<String, ElementFieldSetter> elementSetters = new HashMap<>();


	boolean isRoot;

	

	/**
	 * 
	 */
	public TypeHandler(Class<?> type) {
		super();
		this.type = type;
		this.className = type.getName();
	}


	

	public interface Putter {
		public void put(ElementFieldSetter setter) throws XML_TypeCompilationException;
	}

	/*
	public boolean isInitialized() {
		return isInitialized;
	}
	 */

	public int getNumberOfLists() {
		return nLists;
	}

	/**
	 * 
	 * @return tag displayed in XML
	 */
	public String getXmlTag() {
		return xmlName;
	}

	/**
	 * 
	 * @return JAVA name
	 */
	public String getClassName(){
		return className;
	}

	
	public boolean hasSubTypes() {
		return subTypes.length > 0;
	}

	/**
	 * 
	 * @return
	 */
	public TypeHandler[] getSubTypes() {
		return subTypes;
	}





	public Object create(XML_StreamReader.Point point) throws XML_ParsingException {
		try {
			return constructor.newInstance(new Object[]{});
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new XML_ParsingException(point, "Cannot instantiate "+xmlName+" due to "+e.getMessage());
		}
	}

	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws XML_ParsingException 
	 * @throws Exception 
	 */
	public void setAttribute(Object object, String name, String value, XML_StreamReader.Point point) 
			throws XML_ParsingException {
		AttributeSetter setter = attributeSetters.get(name);
		if(setter==null){
			throw new XML_ParsingException(point, "No field with name "+name+" in type "+getXmlTag());
		}
		setter.set(object, value, point);
	}


	/**
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public List<AttributeGetter> getAttributeGetters() {
		return attributeGetters;
	}


	public boolean hasValueSetter() {
		return valueSetter!=null;
	}

	public void setValue(Object object, String value, XML_StreamReader.Point point) throws XML_ParsingException{
		valueSetter.set(object, value, point);
	}

	public String getValue(Object object, XML_StreamReader.Point point) throws XML_ParsingException{
		if(valueGetter==null){
			throw new XML_ParsingException(point, "No value can be get in type "+this.xmlName);
		}
		try {
			return valueGetter.get(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new XML_ParsingException(point, e.getMessage());
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws XML_ParsingException 
	 * @throws Exception
	 */
	public ElementFieldSetter getElementSetter(String name, XML_StreamReader.Point point) throws XML_ParsingException {
		ElementFieldSetter setter = elementSetters.get(name);
		if(setter==null){
			throw new XML_ParsingException(point, "No field with name "+name+" in type "+this.xmlName);
		}
		return setter;
	}


	/**
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public List<ElementGetter> getElementGetters() throws Exception{
		return elementGetters;
	}



	/**
	 * 
	 * @param parent
	 * @param tag
	 * @param point
	 * @return
	 * @throws XML_ParsingException
	 */
	public Parsed createParsedElement(ParsedObjectElement parent, String tag, XML_StreamReader.Point point) 
			throws XML_ParsingException {
		ElementFieldSetter setter = elementSetters.get(tag);
		if(setter==null) {
			throw new XML_ParsingException(point, "Failed to retrieve element setter for tag: "+tag);
		}
		return setter.getParsedElement(parent, point);
	}

	public Class<?> getType() {
		return type;
	}


	public boolean isRoot() {
		return isRoot;
	}

}
