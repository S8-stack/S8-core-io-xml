package com.qx.level0.lang.xml.handler.type;

import java.util.HashMap;
import java.util.Map;

import com.qx.level0.lang.xml.XML_ContextBuilder;
import com.qx.level0.lang.xml.annotation.XML_Type;
import com.qx.level0.lang.xml.handler.Initializable;

public class TypeBuilder {

	private Class<?> type;
	
	private TypeHandler typeHandler;
	
	private boolean isInheritanceDiscovered;
	
	public TypeBuilder(TypeHandler typeHandler) {
		super();
		this.type = typeHandler.getType();
		this.typeHandler = typeHandler;
	}
	
	
	public TypeHandler getHandler() {
		return typeHandler;
	}
	
	public void initialize(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {

		/* <sub-types> */
		Map<String, TypeBuilder> map = new HashMap<>();

		// annotated subTypes and following
		listSubTypes(contextBuilder, map);

		typeHandler.subTypes = map.values().stream().map(t -> t.getHandler()).toArray(size -> new TypeHandler[size]);
		isInheritanceDiscovered = true;
		/* </sub-types> */

		
		XML_Type typeAnnotation  = typeHandler.getType().getAnnotation(XML_Type.class);
		if(typeAnnotation==null){
			throw new XML_TypeCompilationException("Missing type declaration for type: "+type.getName());
		}
		typeHandler.xmlName = typeAnnotation.name();
		typeHandler.isRoot = typeAnnotation.isRoot();


		/* <constructor> */

		try {
			typeHandler.constructor = type.getConstructor(new Class<?>[]{});
		} 
		catch (NoSuchMethodException | SecurityException e) {
			throw new XML_TypeCompilationException("Failed to find default constructor");
		}

		/* </constructor> */

		Initializable initializable = new TypeFieldsInitializable(contextBuilder, typeHandler);
		if(!initializable.initialize(contextBuilder)) {
			contextBuilder.appendInitializable(initializable);
		}
	}
	
	

	/**
	 * 
	 * @param context
	 * @param initializables
	 * @param subTypes
	 * @throws XML_TypeCompilationException
	 */
	private void listSubTypes(XML_ContextBuilder contextBuilder, Map<String, TypeBuilder> map) 
			throws XML_TypeCompilationException {

		// typeAnnotation has already been checked before
		XML_Type typeAnnotation  = type.getAnnotation(XML_Type.class);

		TypeBuilder subTypeHandler;

		/* <annotated_sub-types> */
		if(typeAnnotation.sub()!=null){

			for(Class<?> subType : typeAnnotation.sub()) {

				// check if already registered
				if(!map.containsKey(subType.getName())) {
					
					// register type (if not already done)
					contextBuilder.register(subType);

					// test inheritance
					if(typeHandler.getType().isAssignableFrom(subType)) {
						if((subTypeHandler = contextBuilder.getTypeBuilder(subType))!=null) {
							map.put(subType.getName(), subTypeHandler);
							subTypeHandler.listSubTypes(contextBuilder, map);
						}	
					}	
				}
				
			}
		}
		/* </annotated_sub-types> */


		/* <extensions> */
		if(contextBuilder.hasExtensions()){
			for(Class<?> subType : contextBuilder.getExtensions()) {
				
				// check if already registered and if direct super class
				if(!map.containsKey(subType.getName()) && subType.getSuperclass().equals(type)) {

					// register type (if not already done)
					contextBuilder.register(subType);

					// test inheritance
					if((subTypeHandler = contextBuilder.getTypeBuilder(subType))!=null) {
						map.put(subType.getName(), subTypeHandler);
						subTypeHandler.listSubTypes(contextBuilder, map);
					}
				}
			}
		}
		/* </extensions> */

	}
	


	public boolean isInheritanceDiscovered() {
		return isInheritanceDiscovered;
	}

}
