package com.s8.blocks.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import com.s8.blocks.xml.XML_Syntax;
import com.s8.blocks.xml.handler.XML_Lexicon;

public class DocumentComposableScope extends ComposableScope {
	
	private Object object;

	
	private ObjectComposableScope rootObjectScope;

	private boolean isHeaderWritten;
	
	public DocumentComposableScope(Object object) {
		super();
		this.object = object;
	}
	
	@Override
	public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, Exception {
		
		if(!isHeaderWritten) {

			// write header
			writer.append(XML_Syntax.HEADER+"\n");
			
			
			rootObjectScope = new ObjectComposableScope.TypeTagged(object);
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
	public boolean compose(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws Exception {
		return rootObjectScope.insert(context, stack, writer);
	}

}
