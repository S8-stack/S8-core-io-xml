package com.s8.blocks.xml.handler;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.s8.blocks.xml.handler.type.TypeBuilder;
import com.s8.blocks.xml.handler.type.TypeHandler;
import com.s8.blocks.xml.handler.type.XML_TypeCompilationException;

public class XML_LexiconBuilder {

	private XML_Lexicon context;

	
	/**
	 * indexed by tag
	 */
	private ArrayDeque<FeatureBuilder> initializables = new ArrayDeque<>();

	/**
	 * indexed by java classname
	 */
	private Map<String, TypeBuilder> typeBuilders = new HashMap<>();

	private Class<?>[] types;
	private Class<?>[] extensions;

	public XML_LexiconBuilder(XML_Lexicon context, Class<?>[] types, Class<?>[] extensions) {
		super();
		this.context = context;
		this.types = types;
		this.extensions = extensions;
		initializables = new ArrayDeque<>();
	}

	public void build(boolean isVerbose) throws XML_TypeCompilationException {

		// register all types
		for(Class<?> type : types){
			register(type);
		}

		// register all extensions
		if(extensions!=null) {
			for(Class<?> type : extensions){
				register(type);
			}	
		}

		// resolve late inits
		boolean isBuilt = false;
		Collection<TypeBuilder> buildSet = typeBuilders.values();
		
		int c=0;
		while(!isBuilt && c<65536) {
			
			// clear status
			isBuilt = true;
			
			for(TypeBuilder typeBuilder : buildSet) {
				boolean isTypeBuilt = typeBuilder.build(this, isVerbose);
				if(!isTypeBuilt) {
					isBuilt = false;
				}
			}
			
			c++;
		}
		
		if(!isBuilt) {
			throw new XML_TypeCompilationException("Failed to build context");
		}
	}

	public void appendInitializable(FeatureBuilder initializable) {
		initializables.add(initializable);
	}

	public boolean hasExtensions(){
		return extensions!=null;
	}

	public Class<?>[] getExtensions(){
		return extensions;
	}

	/**
	 * 
	 * @param type
	 * @throws Exception 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void register(Class<?> type) throws XML_TypeCompilationException {

		if(!isRegistered(type)){
			try {

				// create type handler and map it
				TypeHandler typeHandler = new TypeHandler(type);
				context.map.put(type.getName(), typeHandler);
				
				if(typeHandler.isRootElement()) {
					context.rootElements.put(typeHandler.xml_getTag(), typeHandler);
				}
				
				// create type builder and map it
				TypeBuilder typeBuilder = new TypeBuilder(typeHandler);
				typeBuilders.put(type.getName(), typeBuilder);

				// then initialize
				typeBuilder.initialize();
				
				// then explore all referenced types
				typeBuilder.explore(this);

				/*
				if(typeHandler.isRoot()) {
					String tag = typeHandler.getXmlTag();
					context.xmlRoots.put(tag, typeHandler);
					context.xmlRoots.put("root:"+tag, typeHandler);
				}
				*/

			}
			catch (SecurityException e) {
				throw new XML_TypeCompilationException("Failed to initialize due to "+e.getMessage());
			}
		}
	}


	public boolean isRegistered(Class<?> type){
		return typeBuilders.containsKey(type.getName());
	}

	public TypeBuilder getTypeBuilder(Class<?> type) {
		return typeBuilders.get(type.getName());
	}

	public XML_Lexicon getContext() {
		return context;
	}
}