package com.s8.io.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.xml.codebase.XML_Codebase;
import com.s8.io.xml.handler.type.TypeHandler;
import com.s8.io.xml.handler.type.attributes.getters.AttributeGetter;
import com.s8.io.xml.handler.type.elements.getters.ElementGetter;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ObjectComposableScope implements ComposableScope {


	public final TagComposer tagComposer;
	
	public final Object object;


	/**
	 * 
	 * @param context
	 * @param fieldValue
	 * @throws XML_ComposingException 
	 */
	public ObjectComposableScope(TagComposer tagComposer, Object object) throws XML_ComposingException{
		super();
		this.tagComposer = tagComposer;
		this.object = object;
		if(object == null) {
			throw new XML_ComposingException("Object is null");
		}
	}


	public Object getObject() {
		return object;
	}


	public void writeClosingTag(XML_StreamWriter writer) throws IOException {

	}


	/**
	 * 
	 * 
	 * has inner scope
	 * <scope1>
	 * 	<scope2></scope2>
	 *  <scope2></scope2>
	 * </scope1>
	 * 
	 * <scope>string</scope> -> no stacking
	 * <scope><inner-scope> -> stacking
	 */
	@Override
	public void compose(XML_Codebase context, XML_StreamWriter writer) throws XML_ComposingException, IOException {

		/* late resolve of type handler */
		TypeHandler typeHandler = context.getTypeHandlerByClass(object.getClass()); 

		if(typeHandler == null) {
			throw new XML_ComposingException("Cannot retrieve type for: "+object.getClass());
		}
		// write opening tag


		String tag = tagComposer.compose(typeHandler.xml_getTag());
		//writer.startTag(tag+XML_Syntax.MAPPING_SEPARATOR+);
		
		writer.appendOpeningTag(tag);


		List<ComposableScope> subScopes = new ArrayList<>();
		for(ElementGetter elementGetter : typeHandler.getElementGetters()){
			elementGetter.compose(object, subScopes);
		}


		// write attributes
		
		for(AttributeGetter attributeGetter : typeHandler.getAttributeGetters()){
			String attributeValue = null;
			try {
				attributeValue = attributeGetter.get(object);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				throw new XML_ComposingException("Composing excpetion at tag: "+tag+", because : "+e.getMessage());
			}	

			if(attributeValue!=null){
				writer.writeAttribute(attributeGetter.getName(), attributeValue);	
			}
		}

		if(subScopes.isEmpty()) {
			writer.append("/>");
		}
		else {

			writer.append(">");

			for(ComposableScope subScope : subScopes) { subScope.compose(context, writer); }

			writer.appendClosingTag(tag);
		}
	}



}
