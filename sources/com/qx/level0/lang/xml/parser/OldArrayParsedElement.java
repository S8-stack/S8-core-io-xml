package com.qx.level0.lang.xml.parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;

public class OldArrayParsedElement extends OldElementParsing {

	private Class<?> componentType;
	
	@SuppressWarnings("rawtypes")
	private List list;
	
	public OldArrayParsedElement(XML_Context context, ObjectParsing parent, String fieldName, Class<?> componentType) {
		super(context, parent, fieldName);
		this.componentType = componentType;
		list = new ArrayList<>();
	}

	@Override
	public OldElementParsing createField(String tag) throws XML_ParsingException {
		String typeName = tag;
		return new ObjectParsing(context, this, null, typeName);
	}
	

	@SuppressWarnings("unchecked")
	// TODO type safety could be done externally...
	@Override
	public void setElement(String fieldName, Object object) {
		list.add(object);
	}
	
	@Override
	public void setValue(String value) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot set inner value in array");	
	}
	
	@Override
	public void setAttribute(String name, String value) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot set attribute in array");	
	}

	@Override
	public void close() throws XML_ParsingException {
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
