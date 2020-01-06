package com.qx.level0.lang.xml.handler;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.handler.type.XML_TypeCompilationException;

public interface Initializable {
	
	
	
	/**
	 * 
	 * @param context
	 * 
	 * @return true if the initialization has been successful
	 * @throws XML_TypeCompilationException 
	 */
	public abstract boolean initialize(XML_Context context) throws XML_TypeCompilationException;
}
