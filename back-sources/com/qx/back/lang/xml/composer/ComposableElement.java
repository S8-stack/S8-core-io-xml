package com.qx.back.lang.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class ComposableElement {
	
	public XML_Composer composer;
	
	public String fieldName;

	
	/**
	 * 
	 * @param context
	 * @param object
	 */
	public ComposableElement(XML_Composer composer, String fieldName){
		super();
		this.composer = composer;
		this.fieldName = fieldName;
	}
	
	/**
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 * @throws Exception 
	 * 
	 */
	public abstract void compose(XML_StreamWriter writer)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, Exception;

}
