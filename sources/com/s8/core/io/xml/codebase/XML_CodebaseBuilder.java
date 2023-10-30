package com.s8.core.io.xml.codebase;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.s8.core.io.xml.annotations.XML_Type;
import com.s8.core.io.xml.handler.type.TypeBuilder;
import com.s8.core.io.xml.handler.type.TypeHandler;
import com.s8.core.io.xml.handler.type.XML_TypeCompilationException;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class XML_CodebaseBuilder {

	private final XML_Codebase codebase;

	
	/**
	 * indexed by java classname
	 */
	private Map<String, TypeBuilder> typeBuilders = new HashMap<>();
	
	
	private final Queue<TypeBuilder> buildQueue = new ArrayDeque<>();
	

	public XML_CodebaseBuilder() throws XML_TypeCompilationException {
		super();
		this.codebase = new XML_Codebase();
	}

	
	public void discover(Class<?> type) throws XML_TypeCompilationException {
		
		/* is annotated */
		if(type.isAnnotationPresent(XML_Type.class) 
				/* AND is not yet discovered */
				&& !typeBuilders.containsKey(type.getName())) {
			
			/* create the type builder */
			TypeBuilder typeBuilder = new TypeBuilder(type);
			
			/* register it (so cannot have two type builders for the same type */
			typeBuilders.put(type.getName(), typeBuilder);
			
			/* enqueue for build */
			buildQueue.add(typeBuilder);
		}
	}
	
	
	public TypeBuilder getTypeBuilder(String javaName) {
		return typeBuilders.get(javaName);
	}
	
	
	
	public final static int MAX_COUNT = 65536;
	
	
	/**
	 * 
	 * @param types
	 * @param isVerbose
	 * @throws XML_TypeCompilationException
	 */
	public XML_Codebase build(Class<?>[] types, boolean isVerbose) throws XML_TypeCompilationException {

		/* discover initial set */
		for(Class<?> type : types) { discover(type); }
		
		
		/* continue while build queue is not exhausted */
		int c = 0;
		while(!buildQueue.isEmpty() && c < MAX_COUNT) {
			
			TypeBuilder builder = buildQueue.poll();
			
			builder.buildOutline(this, isVerbose);
			
			c++;
		}
		
		if(c == MAX_COUNT) {
			throw new XML_TypeCompilationException("Failed to build context");
		}
		
		/* inheritance */
		typeBuilders.forEach((name, builder) -> builder.buildInheritance());
		
		
		
		typeBuilders.forEach((name, builder) -> {
			try {
				builder.buildAccessors(this, isVerbose);
			} 
			catch (XML_TypeCompilationException e) {
				e.printStackTrace();
			}
		});	
		
		
		/* warpping */
		typeBuilders.forEach((name, builder) -> { 
			TypeHandler typeHandler = builder.getHandler();
			codebase.map.put(name, typeHandler);
			if(typeHandler.isRootElement()) { codebase.rootElements.put(typeHandler.xml_getTag(), typeHandler); }
		});	
		
		return codebase;
	}
	

	
	public TypeBuilder getTypeBuilder(Class<?> type) {
		return typeBuilders.get(type.getName());
	}

	public XML_Codebase getContext() {
		return codebase;
	}
}