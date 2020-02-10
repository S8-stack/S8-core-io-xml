package com.qx.level0.lang.xml;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import com.qx.level0.lang.xml.handler.Initializable;
import com.qx.level0.lang.xml.handler.type.TypeBuilder;
import com.qx.level0.lang.xml.handler.type.TypeHandler;
import com.qx.level0.lang.xml.handler.type.XML_TypeCompilationException;

public class XML_ContextBuilder {

	private XML_Context context;

	private ArrayDeque<Initializable> initializables = new ArrayDeque<>();

	private Map<String, TypeBuilder> typeBuilders = new HashMap<>();

	private Class<?>[] types;
	private Class<?>[] extensions;

	public XML_ContextBuilder(XML_Context context, Class<?>[] types, Class<?>[] extensions) {
		super();
		this.context = context;
		this.types = types;
		this.extensions = extensions;
		initializables = new ArrayDeque<>();
	}

	public void build() throws XML_TypeCompilationException {

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
		while(!initializables.isEmpty()) {
			Initializable initializable = initializables.poll();
			if(!initializable.initialize(this)) {
				initializables.add(initializable);
			}
		}

	}

	public void appendInitializable(Initializable initializable) {
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
				context.typeMap.put(typeHandler.getClassName(), typeHandler);

				// create type builder and map it
				TypeBuilder typeBuilder = new TypeBuilder(typeHandler);
				typeBuilders.put(typeHandler.getClassName(), typeBuilder);

				// then initialize
				typeBuilder.initialize(this);

				if(typeHandler.isRoot()) {
					String tag = typeHandler.getXmlTag();
					context.xmlRoots.put(tag, typeHandler);
					context.xmlRoots.put("root:"+tag, typeHandler);
				}

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
}