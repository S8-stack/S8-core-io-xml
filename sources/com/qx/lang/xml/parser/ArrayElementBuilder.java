package com.qx.lang.xml.parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.qx.lang.xml.handler.XML_Context;

public class ArrayElementBuilder extends ElementBuilder {

	private Class<?> componentType;
	
	@SuppressWarnings("rawtypes")
	private List list;
	
	public ArrayElementBuilder(XML_Context context, ObjectElementBuilder parent, String fieldName, Class<?> componentType) {
		super(context, parent, fieldName);
		this.componentType = componentType;
		list = new ArrayList<>();
	}

	@Override
	public ElementBuilder createField(String tag) throws Exception {
		String typeName = tag;
		return new ObjectElementBuilder(context, parent, null, typeName);
	}
	

	@SuppressWarnings("unchecked")
	// TODO type safety could be done externally...
	@Override
	public void appendElement(String fieldName, Object object) throws Exception {
		list.add(object);
	}

	@Override
	public void close() throws Exception {
		int length = list.size();
		Object array = Array.newInstance(componentType, length);
		for(int i=0; i<length; i++){
			Array.set(array, i, list.get(i));	
		}
		parent.appendElement(fieldName, array);
	}

}
