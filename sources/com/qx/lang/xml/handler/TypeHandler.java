package com.qx.lang.xml.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
	 * declared name
	 */
	private String name;


	private Constructor<?> constructor;

	private AttributeGetter valueGetter;

	private AttributeSetter valueSetter;

	private Map<String, AttributeGetter> attributeGetters = new HashMap<>();

	private Map<String, AttributeSetter> attributeSetters = new HashMap<>();

	private Map<String, Method> elementGetters = new HashMap<>();

	private Map<String, ElementSetter> elementSetters = new HashMap<>();

	/**
	 * 
	 */
	public TypeHandler(Class<?> type){
		super();
		this.type = type;
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

		String name = typeAnnotation.name();

		// retrieve name
		this.name = name;
		
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

		for(Method method : type.getMethods()){
			getAttributeAnnotation = method.getAnnotation(XML_GetAttribute.class);
			setAttributeAnnotation = method.getAnnotation(XML_SetAttribute.class);
			getValueAnnotation = method.getAnnotation(XML_GetValue.class);
			setValueAnnotation = method.getAnnotation(XML_SetValue.class);
			getElementAnnotation = method.getAnnotation(XML_GetElement.class);
			setElementAnnotation = method.getAnnotation(XML_SetElement.class);

			if(getAttributeAnnotation!=null){
				attributeGetters.put(getAttributeAnnotation.name(), AttributeGetter.create(method));	
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
				Class<?>[] parameters = method.getParameterTypes();
				if(parameters.length!=0){
					throw new RuntimeException("Illegal number of parameters for a setter");
				}
				elementGetters.put(getElementAnnotation.name(), method);	
			}
			else if(setElementAnnotation!=null){
				
				elementSetters.put(setElementAnnotation.name(), ElementSetter.create(method));	
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
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
			throw new Exception("No field with name "+name+" in type "+this.name);
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
	public String getAttribute(Object object, String name)
			throws Exception{
		AttributeGetter getter = attributeGetters.get(name);
		if(getter==null){
			throw new Exception("No field with name "+name+" in type "+this.name);
		}
		return getter.get(object);
	}

	public void setValue(Object object, String value)
			throws Exception{
		if(valueSetter==null){
			throw new Exception("No value can be set in type "+this.name);
		}
		valueSetter.set(object, value);
	}

	public String getValue(Object object)
			throws Exception{
		if(valueGetter==null){
			throw new Exception("No value can be get in type "+this.name);
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
			throw new Exception("No field with name "+name+" in type "+this.name);
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
			throw new Exception("No field with name "+name+" in type "+this.name);
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
	public Object getElement(Object object, String name)
			throws Exception{
		Method getter = elementGetters.get(name);
		if(getter==null){
			throw new Exception("No field with name "+name+" in type "+this.name);
		}
		return getter.invoke(object);
	}
}
