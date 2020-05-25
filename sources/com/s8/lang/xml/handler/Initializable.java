package com.s8.lang.xml.handler;

import com.s8.lang.xml.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;

public interface Initializable {
	
	
	
	/**
	 * 
	 * @param context
	 * 
	 * @return true if the initialization has been successful
	 * @throws XML_TypeCompilationException 
	 */
	public abstract boolean initialize(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException;
}
