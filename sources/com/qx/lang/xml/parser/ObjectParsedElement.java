package com.qx.lang.xml.parser;


import com.qx.lang.xml.XML_Context;
import com.qx.lang.xml.handler.ElementSetter;
import com.qx.lang.xml.handler.TypeHandler;


/**
 * 
 * @author pc
 *
 */
public class ObjectParsedElement extends ParsedElement {


	/**
	 * 
	 */
	private TypeHandler typeHandler;


	/**
	 * 
	 */
	private Object object;



	/**
	 * 
	 * @param context
	 * @param parent
	 * @param parentFieldName
	 * @param typeName : serialName
	 * @throws XML_ParsingException 
	 * @throws Exception
	 */
	public ObjectParsedElement(XML_Context context, ParsedElement parent, String parentFieldName, String typeName) throws XML_ParsingException {
		super(context, parent, parentFieldName);
		this.typeHandler = context.getBySerialName(typeName);

		if(typeHandler==null){
			throw new XML_ParsingException("Unknown type: "+typeName);
		}
		this.object = typeHandler.create();
	}



	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws XML_ParsingException 
	 * @throws Exception 
	 */
	@Override
	public void setAttribute(String name, String value) throws XML_ParsingException {
		typeHandler.setAttribute(object, name, value);
	}



	@Override
	public void setValue(String value) throws XML_ParsingException {
		typeHandler.setValue(object, value);
	}

	public String getValue() throws Exception {
		return typeHandler.getValue(object);
	}

	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws XML_ParsingException 
	 * @throws Exception 
	 */
	@Override
	public void setElement(String name, Object value) throws XML_ParsingException {
		typeHandler.setElement(object, name, value);
	}




	@Override
	public ParsedElement createField(String tag) throws XML_ParsingException {
		String[] fragments = tag.split(":");
		String fieldName = fragments[0];
		String typeName = fragments.length>1?fragments[1]:null;
		ElementSetter setter = typeHandler.getElementSetter(fieldName);
		return setter.createParsedElement(context, this, typeName);
	}


	@Override
	public void close() throws XML_ParsingException {
		parent.setElement(fieldNameInParent, object);
	}


	@Override
	public String getTag() {
		if(fieldNameInParent!=null){
			return fieldNameInParent+':'+typeHandler.getSerialName();	
		}
		else{
			return typeHandler.getSerialName();
		}
	}

	

}
