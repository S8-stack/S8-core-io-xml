package com.qx.lang.xml.parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.qx.lang.xml.context.XML_Context;

public class ArrayParsedElement extends ParsedElement {

	private Class<?> componentType;
	
	@SuppressWarnings("rawtypes")
	private List list;
	
	public ArrayParsedElement(XML_Context context, ObjectParsedElement parent, String fieldName, Class<?> componentType) {
		super(context, parent, fieldName);
		this.componentType = componentType;
		list = new ArrayList<>();
	}

	@Override
	public ParsedElement createField(String tag) throws Exception {
		String typeName = tag;
		return new ObjectParsedElement(context, this, null, typeName);
	}
	

	@SuppressWarnings("unchecked")
	// TODO type safety could be done externally...
	@Override
	public void setElement(String fieldName, Object object) throws Exception {
		list.add(object);
	}
	
	@Override
	public void setValue(String value) throws Exception {
		throw new Exception("Cannot set inner value in array");	
	}
	
	@Override
	public void setAttribute(String name, String value) throws Exception {
		throw new Exception("Cannot set attribute in array");	
	}

	@Override
	public void close() throws Exception {
		int length = list.size();
		Object array = Array.newInstance(componentType, length);
		for(int i=0; i<length; i++){
			Array.set(array, i, list.get(i));	
		}
		parent.setElement(fieldNameInParent, array);
	}

	@Override
	public String getTag() {
		return fieldNameInParent;
	}

}
