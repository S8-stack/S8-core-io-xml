package com.qx.lang.xml.parser;


import com.qx.lang.xml.handler.ElementSetter;
import com.qx.lang.xml.handler.ElementSetter.ArrayElementSetter;
import com.qx.lang.xml.handler.TypeHandler;
import com.qx.lang.xml.handler.XML_Context;


/**
 * 
 * @author pc
 *
 */
public class ObjectElementBuilder extends ElementBuilder {


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
	 * @param typeHandler
	 * @param object
	 * @throws Exception 
	 */
	public ObjectElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName, String typeName)
			throws Exception {
		super(context, parent, parentFieldName);
		this.typeHandler = context.get(typeName);

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


	/**
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public String getAttribute(String name) throws Exception{
		return typeHandler.getAttribute(object, name);
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


	/**
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public Object getElement(Object object, String name) throws Exception{
		return typeHandler.getElement(object, name);
	}



	@Override
	public ElementBuilder createField(String tag) throws Exception {

		String[] fragments = tag.split(":");
		String fieldName = fragments[0];
		
		ElementSetter setter = typeHandler.getElementSetter(fieldName);
		switch (setter.getElementType()) {
		
		case OBJECT:
			if(fragments.length!=2){
				throw new Exception("Missing type name for field "+fieldName);
			}
			return new ObjectElementBuilder(context, this, fieldName, fragments[1]);
			
		case ARRAY:
			if(fragments.length!=1){
				throw new Exception("Unexpected type name for array field "+fieldName);
			}
			return new ArrayElementBuilder(context, this, fieldName, ((ArrayElementSetter) setter).getComponentType());
			
		case MAP:
			if(fragments.length!=1){
				throw new Exception("Unexpected type name for map field "+fieldName);
			}
			return new MapElementBuilder(context, this, fieldName);

		default: throw new Exception("Unexpected type");
		}
		
	}



	@Override
	public void close() throws Exception {
		parent.setElement(fieldNameInParent, object);
	}


	@Override
	public String getTag() {
		return fieldNameInParent+':'+typeHandler.getName();
	}

	

}
