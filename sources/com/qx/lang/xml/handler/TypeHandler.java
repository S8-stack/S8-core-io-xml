package com.qx.lang.xml.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qx.lang.xml.XML_Context;
import com.qx.lang.xml.annotation.XML_GetAttribute;
import com.qx.lang.xml.annotation.XML_GetElement;
import com.qx.lang.xml.annotation.XML_GetValue;
import com.qx.lang.xml.annotation.XML_SetAttribute;
import com.qx.lang.xml.annotation.XML_SetElement;
import com.qx.lang.xml.annotation.XML_SetValue;
import com.qx.lang.xml.annotation.XML_Type;

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
	private String serialName;

	/**
	 * declared name in Class<?> (JAVA side)
	 */
	private String deserialName;

	
	/**
	 * 
	 */
	private Constructor<?> constructor;

	private AttributeGetter valueGetter;

	private AttributeSetter valueSetter;

	private List<AttributeGetter> attributeGetters = new ArrayList<>();

	private Map<String, AttributeSetter> attributeSetters = new HashMap<>();

	private List<ElementGetter> elementGetters = new ArrayList<>();

	private Map<String, ElementSetter> elementSetters = new HashMap<>();

	/**
	 * 
	 */
	public TypeHandler(Class<?> type){
		super();
		this.type = type;
		
		
		XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);
		if(typeAnnotation==null){
			throw new RuntimeException("Missing type declaration for type: "+type.getName());
		}
		this.serialName = typeAnnotation.name();
		
		this.deserialName = type.getName();
	}


	/**
	 * @param context
	 * @throws Exception 
	 */
	public void initialize(XML_Context context) throws Exception{
		
		XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);
		if(typeAnnotation==null){
			throw new RuntimeException("Missing type declaration for type: "+type.getName());
		}

		
		constructor = type.getConstructor(new Class<?>[]{});
		
		// read subTypes
		if(typeAnnotation.sub()!=null){
			for(Class<?> subType : typeAnnotation.sub()){
				context.discover(subType);
			}
		}

		XML_GetAttribute getAttributeAnnotation;
		XML_SetAttribute setAttributeAnnotation;
		XML_GetValue getValueAnnotation;
		XML_SetValue setValueAnnotation;
		XML_GetElement getElementAnnotation;
		XML_SetElement setElementAnnotation;

		String name;
		for(Method method : type.getMethods()){
			getAttributeAnnotation = method.getAnnotation(XML_GetAttribute.class);
			setAttributeAnnotation = method.getAnnotation(XML_SetAttribute.class);
			getValueAnnotation = method.getAnnotation(XML_GetValue.class);
			setValueAnnotation = method.getAnnotation(XML_SetValue.class);
			getElementAnnotation = method.getAnnotation(XML_GetElement.class);
			setElementAnnotation = method.getAnnotation(XML_SetElement.class);

			if(getAttributeAnnotation!=null){
				attributeGetters.add(AttributeGetter.create(method));	
			}
			else if(setAttributeAnnotation!=null){
				attributeSetters.put(setAttributeAnnotation.name(), AttributeSetter.create(method));	
			}
			if(getValueAnnotation!=null){
				valueGetter = AttributeGetter.create(method);
			}
			else if(setValueAnnotation!=null){
				valueSetter = AttributeSetter.create(method);
			}
			else if(getElementAnnotation!=null){
				elementGetters.add(ElementGetter.create(method));	
			}
			else if(setElementAnnotation!=null){
				name =setElementAnnotation.name();
				elementSetters.put(name, ElementSetter.create(context, method, name));	
			}
		}
	}
	
	/**
	 * 
	 * @return tag displayed in XML
	 */
	public String getSerialName() {
		return serialName;
	}
	
	public String getDeserialName(){
		return deserialName;
	}


	public Object create()
			throws
			InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException {
		return constructor.newInstance(new Object[]{});
	}

	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public void setAttribute(Object object, String name, String value) throws Exception{
		AttributeSetter setter = attributeSetters.get(name);
		if(setter==null){
			throw new Exception("No field with name "+name+" in type "+this.serialName);
		}
		setter.set(object, value);
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

	public void setValue(Object object, String value)
			throws Exception{
		if(valueSetter==null){
			throw new Exception("No value can be set in type "+this.serialName);
		}
		valueSetter.set(object, value);
	}

	public String getValue(Object object)
			throws Exception{
		if(valueGetter==null){
			throw new Exception("No value can be get in type "+this.serialName);
		}
		return valueGetter.get(object);
	}

	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public void setElement(Object object, String name, Object value) throws Exception{
		ElementSetter setter = elementSetters.get(name);
		if(setter==null){
			throw new Exception("No field with name "+name+" in type "+this.serialName);
		}
		setter.set(object, value);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public ElementSetter getElementSetter(String name) throws Exception{
		ElementSetter setter = elementSetters.get(name);
		if(setter==null){
			throw new Exception("No field with name "+name+" in type "+this.serialName);
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
}
