package com.s8.lang.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import com.s8.lang.xml.XML_Syntax;
import com.s8.lang.xml.handler.XML_Context;
import com.s8.lang.xml.handler.type.TypeHandler;

public class DocumentComposableScope extends ComposableScope {
	
	private Object object;

	
	private ObjectComposableScope rootScope;

	private boolean isHeaderWritten;
	
	public DocumentComposableScope(Object object) {
		super();
		this.object = object;
	}
	
	@Override
	public boolean insert(XML_Context context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, Exception {
		
		if(!isHeaderWritten) {

			// write header
			writer.append(XML_Syntax.HEADER+"\n");
			
			Class<?> type = object.getClass();
			TypeHandler typeHandler = context.getTypeHandlerByClass(type);
			
			rootScope = new ObjectComposableScope(typeHandler.getXmlTag(), object);
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
	public boolean compose(XML_Context context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws Exception {
		return rootScope.insert(context, stack, writer);
	}

}
