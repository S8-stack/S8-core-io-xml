package com.s8.io.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import com.s8.io.xml.XML_Syntax;
import com.s8.io.xml.codebase.XML_Codebase;



/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class DocumentComposableScope extends ComposableScope {
	
	
	private String tag;
	private Object object;

	
	private ObjectComposableScope rootObjectScope;

	private boolean isHeaderWritten;
	
	public DocumentComposableScope(String tag, Object object) {
		super();
		this.tag = tag;
		this.object = object;
	}
	
	@Override
	public boolean insert(XML_Codebase context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, Exception {
		
		if(!isHeaderWritten) {

			// write header
			writer.append(XML_Syntax.HEADER+"\n");
			
			
			rootObjectScope = new ObjectComposableScope(tag, object);
			stack.push(this);
			isHeaderWritten = true;
			
			// has stacked something
			return true;
		}
		else {
			return false; // already done
		}
	}

	@Override
	public boolean compose(XML_Codebase context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws Exception {
		return rootObjectScope.insert(context, stack, writer);
	}

}
