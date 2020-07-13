package com.s8.lang.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import com.s8.lang.xml.handler.XML_Context;

/**
 * 
 * @author pierreconvert
 *
 */
public abstract class ComposableScope {
	

	public ComposableScope next;
	
	
	/**
	 * 
	 * @param context
	 * @param object
	 */
	public ComposableScope(){
		super();
	}
	
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
	public abstract boolean insert(XML_Context context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, Exception;

	/**
	 * Plain construction
	 * 
	 * @param context
	 * @param composer
	 * @param writer
	 * @return
	 * @throws Exception
	 */
	public abstract boolean compose(XML_Context context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws Exception;
	

}
