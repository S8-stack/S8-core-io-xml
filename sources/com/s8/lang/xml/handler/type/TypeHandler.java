package com.s8.lang.xml.handler.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.s8.lang.xml.XML_Syntax;
import com.s8.lang.xml.api.XML_Type;
import com.s8.lang.xml.handler.type.attributes.getters.AttributeGetter;
import com.s8.lang.xml.handler.type.attributes.setters.AttributeSetter;
import com.s8.lang.xml.handler.type.elements.getters.ElementGetter;
import com.s8.lang.xml.handler.type.elements.setters.ElementSetter;
import com.s8.lang.xml.handler.type.value.getters.ValueGetter;
import com.s8.lang.xml.handler.type.value.setters.ValueSetter;
import com.s8.lang.xml.parser.ObjectParsedScope;
import com.s8.lang.xml.parser.ParsedScope;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader;

/**
 * 
 * @author pc
 *
 */
public class TypeHandler {



	Class<?> type;


	/**
	 * declared name in XML. Go in tag. (XML side)
	 */
	String xmlName;

	
	
	boolean isRootElement;
	
	/**
	 * declared name in Class<?> (JAVA side)
	 */
	//private String className;


	/**
	 * mapping of all subclasses
	 * (package-private for external construction)
	 */
	TypeHandler[] subTypes;

	/** ready for ext construction */
	Constructor<?> constructor;

	/** ready for ext construction */
	ValueGetter valueGetter;

	/** ready for ext construction */
	ValueSetter valueSetter;

	/** ready for ext construction */
	List<AttributeGetter> attributeGetters = new ArrayList<>();

	/** ready for ext construction */
	Map<String, AttributeSetter> attributeSetters = new HashMap<>();

	/** ready for ext construction */
	List<ElementGetter> elementGetters = new ArrayList<>();

	/**
	 * Mapping of all possible ways of settings field (with element setters)
	 */
	Map<String, ElementSetter> elementSetters = new HashMap<>();

	/**
	 * 
	 */
	Set<String> elementGettersTagSet = new HashSet<String>();

	/**
	 * 
	 */
	public DTD_ElementGenerator DTD_typeGenerator;

	//boolean isRoot;
	
	public XSD_TypeGenerator xsd_TypeGenerator;



	/**
	 * @throws XML_TypeCompilationException 
	 * 
	 */
	public TypeHandler(Class<?> type) throws XML_TypeCompilationException {
		super();
		this.type = type;

		// pre-initialize
		XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);
		if(typeAnnotation==null){
			throw new XML_TypeCompilationException("Missing type declaration for type: "+type.getName());
		}
		xmlName = typeAnnotation.name();
		
		isRootElement = typeAnnotation.root();
		
		DTD_typeGenerator = new DTD_ElementGenerator(this);
		
		xsd_TypeGenerator = new XSD_TypeGenerator(this);
	}




	public interface Putter {
		public void put(ElementSetter setter) throws XML_TypeCompilationException;
	}


	/**
	 * 
	 * @return tag displayed in XML
	 */
	public String xml_getTag() {
		return xmlName;
	}
	
	public String xsd_getTypeName() {
		return type.getName().replace('$', '-');
	}

	
	public boolean isRootElement() {
		return isRootElement;
	}
	
	/**
	 * 
	 * @return JAVA name
	 */
	/*
	public String getClassName(){
		return className;
	}
	 */


	public boolean hasSubTypes() {
		return subTypes.length > 0;
	}

	/**
	 * 
	 * @return subTypes, including this one
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
		
		if(!XML_Syntax.isSchemaAttribute(name)) {
			AttributeSetter setter = attributeSetters.get(name);
			if(setter==null){
				throw new XML_ParsingException(point, "No field with name "+name+" in type "+xml_getTag());
			}
			setter.set(object, value, point);	
		}
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
	public ElementSetter getElementSetter(String name, XML_StreamReader.Point point) throws XML_ParsingException {
		ElementSetter setter = elementSetters.get(name);
		if(setter==null){
			throw new XML_ParsingException(point, "No field with name "+name+" in type "+this.xmlName);
		}
		return setter;
	}



	public List<AttributeGetter> getAttributeGetters(){
		return attributeGetters;
	}
	/**
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public List<ElementGetter> getElementGetters() {
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
	public ParsedScope createParsedElement(ObjectParsedScope parent, String tag, XML_StreamReader.Point point) 
			throws XML_ParsingException {
		ElementSetter setter = elementSetters.get(tag);
		if(setter==null) {
			throw new XML_ParsingException(point, "Failed to retrieve element setter for tag: "+tag);
		}
		return setter.createParsedElement(parent, point);
	}

	public Class<?> getType() {
		return type;
	}

	@Override
	public String toString() {
		return "handler ["+xmlName+"] for "+type;
	}
	

}
