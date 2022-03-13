package com.s8.io.xml.composer;

import java.util.Stack;

import com.s8.io.xml.handler.XML_Lexicon;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class XML_Composer {

	private XML_Lexicon context;
	
	private XML_StreamWriter writer;
	
	private Stack<ComposableScope> stack = new Stack<>(); 
	

	public XML_Composer(XML_Lexicon context, XML_StreamWriter writer) {
		super();
		this.context = context;
		this.writer = writer;
	}
	
	public void compose(Object object) throws Exception{
		
		ComposableScope scope = new DocumentComposableScope(object);
		
		scope.insert(context, stack, writer);
		while(!stack.isEmpty()) {
			scope = stack.peek();
			boolean hasStacked = scope.compose(context, stack, writer);
			if(!hasStacked) {
				stack.pop();
			}
		}
	}
}
