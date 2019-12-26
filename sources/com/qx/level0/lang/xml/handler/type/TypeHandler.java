package com.qx.level0.lang.xml.handler.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.annotation.XML_GetAttribute;
import com.qx.level0.lang.xml.annotation.XML_GetElement;
import com.qx.level0.lang.xml.annotation.XML_GetValue;
import com.qx.level0.lang.xml.annotation.XML_SetAttribute;
import com.qx.level0.lang.xml.annotation.XML_SetElement;
import com.qx.level0.lang.xml.annotation.XML_SetValue;
import com.qx.level0.lang.xml.annotation.XML_Type;
import com.qx.level0.lang.xml.handler.list.ListHandler;
import com.qx.level0.lang.xml.handler.type.CollectionElementFieldSetter.Entry;
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
	private String xmlName;

	/**
	 * declared name in Class<?> (JAVA side)
	 */
	private String className;


	/**
	 * mapping of all subclasses
	 */
	private List<TypeHandler> subTypes;


	private int nLists;


	/**
	 * 
	 */
	private Constructor<?> constructor;

	private AttributeGetter valueGetter;

	private AttributeSetter valueSetter;

	private List<AttributeGetter> attributeGetters = new ArrayList<>();

	private Map<String, AttributeSetter> attributeSetters = new HashMap<>();

	private List<ElementGetter> elementGetters = new ArrayList<>();

	/**
	 * Mapping of all possible ways of settings field (with element setters)
	 */
	private Map<String, ElementFieldSetter> elementSetters = new HashMap<>();


	private boolean isRoot;


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
	
	public void initialize(XML_Context context) throws XML_TypeCompilationException {

		try {

			XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);
			if(typeAnnotation==null){
				throw new Exception("Missing type declaration for type: "+type.getName());
			}
			this.xmlName = typeAnnotation.name();
			this.isRoot = typeAnnotation.isRoot();


			/* <sub-types> */

			subTypes = new ArrayList<>();
			listSubTypes(context, subTypes);

			/* </sub-types> */



			/* <constructor> */

			try {
				constructor = type.getConstructor(new Class<?>[]{});
			} 
			catch (NoSuchMethodException | SecurityException e) {
				throw new XML_TypeCompilationException(type, "Failed to find defualt constructor");
			}

			/* </constructor> */

			/* <fields> */

			XML_GetAttribute getAttributeAnnotation;
			XML_SetAttribute setAttributeAnnotation;
			XML_GetValue getValueAnnotation;
			XML_SetValue setValueAnnotation;
			XML_GetElement getElementAnnotation;
			XML_SetElement setElementAnnotation;

			List<ElementFieldSetter.Generator> elementSetterGenerators = new ArrayList<>();

			CollectionElementFieldSetter.Factory factory = new CollectionElementFieldSetter.Factory() {
				public @Override Entry createEntry(TypeHandler handler) {
					return new Entry(nLists++, new ListHandler(handler));
				}
			};


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
					elementSetterGenerators.add(ElementFieldSetter.create(context, method, factory));
				}
			}



			Putter putter = new Putter() {
				public @Override void put(ElementFieldSetter setter) throws XML_TypeCompilationException {
					if(elementSetters.containsKey(setter.getTag())) {
						throw new XML_TypeCompilationException(type,
								"Conflict in standard element setter mapping, for field tag: "+setter.getTag());
					}
					elementSetters.put(setter.getTag(), setter);
				}
			};
			
			// check non-contextual level
			for(ElementFieldSetter.Generator gen : elementSetterGenerators) {
				gen.getStandardSetters(putter);
			}


			int nGen = elementSetterGenerators.size();
			if(nGen>1) {
				for(int i0=0; i0<nGen; i0++) {
					ElementFieldSetter.Generator gen0 = elementSetterGenerators.get(i0);
					int i1=0;
					while(gen0.areContextualTagsEnabled() && i1<nGen) {
						if(i1!=i0) {
							ElementFieldSetter.Generator gen1 = elementSetterGenerators.get(i1);
							if(gen1.areContextualTagsEnabled()) {
								if(gen0.isContextuallyConflictingWith(gen1)) {
									gen0.disableContextualTags();
									gen1.disableContextualTags();
								}
							}
						}
						i1++;
					}
				}	
			}
			

			for(int i=0; i<nGen; i++) {
				ElementFieldSetter.Generator gen = elementSetterGenerators.get(i);
				if(gen.areContextualTagsEnabled()) {
					gen.getContextualSetters(putter);
				}
			}

			/* </fields> */

		}
		catch (Exception e) {
			e.printStackTrace();
			throw new XML_TypeCompilationException(type, e.getMessage());
		}
	}


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


	/**
	 * 
	 * @return
	 */
	public List<TypeHandler> getSubTypes() {
		return subTypes;
	}

	private void listSubTypes(XML_Context context, List<TypeHandler> subTypes) throws XML_TypeCompilationException {

		// typeAnnotation has already been checked before
		XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);

		/* <sub-types> */
		if(typeAnnotation.sub()!=null){
			TypeHandler subTypeHandler;
			for(Class<?> subType : typeAnnotation.sub()) {

				// register type (if not already done)
				context.register(subType);

				// test inheritance
				if(type.isAssignableFrom(subType)) {
					if((subTypeHandler = context.getTypeHandler(subType))!=null) {
						subTypes.add(subTypeHandler);
						subTypeHandler.listSubTypes(context, subTypes);
					}	
				}
			}
		}
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
			throw new XML_ParsingException(point, "Failed to retreive element setter for tag: "+tag);
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
