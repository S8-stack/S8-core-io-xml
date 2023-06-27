package com.s8.io.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.s8.io.xml.codebase.XML_Codebase;

/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public interface ComposableScope {


	/**
	 * NESTED composing
	 * 
	 * @param composer to push additional composables in the course of composition
	 * @param writer to output data
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws Exception
	 */
	public abstract void compose(XML_Codebase context, XML_StreamWriter writer) throws XML_ComposingException, IOException;	


}
