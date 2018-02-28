package com.qx.lang.xml.parser;


import com.qx.lang.xml.context.XML_Context;
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
	 * @throws Exception
	 */
	public ObjectParsedElement(XML_Context context, ParsedElement parent, String parentFieldName, String typeName)
			throws Exception {
		super(context, parent, parentFieldName);
		this.typeHandler = context.getBySerialName(typeName);

		if(typeHandler==null){
			throw new Exception("Unknown type: "+typeName);
		}
		this.object = typeHandler.create();
	}



	/**
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	@Override
	public void setAttribute(String name, String value) throws Exception{
		typeHandler.setAttribute(object, name, value);
	}



	@Override
	public void setValue(String value) throws Exception{
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
	 * @throws Exception 
	 */
	@Override
	public void setElement(String name, Object value) throws Exception{
		typeHandler.setElement(object, name, value);
	}




	@Override
	public ParsedElement createField(String tag) throws Exception {
		String[] fragments = tag.split(":");
		String fieldName = fragments[0];
		String typeName = fragments.length>1?fragments[1]:null;
		ElementSetter setter = typeHandler.getElementSetter(fieldName);
		return setter.createParsedElement(context, this, typeName);
	}


	@Override
	public void close() throws Exception {
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
