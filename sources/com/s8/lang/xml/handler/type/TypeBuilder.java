package com.s8.lang.xml.handler.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.s8.lang.xml.api.XML_GetAttribute;
import com.s8.lang.xml.api.XML_GetElement;
import com.s8.lang.xml.api.XML_SetAttribute;
import com.s8.lang.xml.api.XML_SetElement;
import com.s8.lang.xml.api.XML_Type;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.attributes.getters.AttributeGetter;
import com.s8.lang.xml.handler.type.attributes.setters.AttributeSetter;
import com.s8.lang.xml.handler.type.elements.getters.ElementGetter;
import com.s8.lang.xml.handler.type.elements.setters.ElementSetter;

public class TypeBuilder {

	private TypeHandler typeHandler;


	private boolean isBuilt;

	private boolean isInheritanceBuilt;

	private boolean isGettersBuilt;

	private boolean isSettersBuilt0;

	private boolean isSettersBuilt1;


	private List<ElementGetter.Builder> elementGetBuilders;

	private List<ElementSetter.Builder> elementSetBuilders;

	public TypeBuilder(TypeHandler typeHandler) {
		super();
		this.typeHandler = typeHandler;
		elementGetBuilders = new ArrayList<ElementGetter.Builder>();
		elementSetBuilders = new ArrayList<>();
		isBuilt = false;
	}

	public TypeHandler getHandler() {
		return typeHandler;
	}


	public void setElementSetter(ElementSetter elementSetter) {
		typeHandler.elementSetters.put(elementSetter.getTag(), elementSetter);
	}

	public Class<?> getType(){
		return typeHandler.getType();
	}

	public void initialize() throws XML_TypeCompilationException {

		//typeHandler.isRoot = typeAnnotation.isRoot();


		/* <constructor> */

		try {
			typeHandler.constructor = getType().getConstructor(new Class<?>[]{});
		} 
		catch (NoSuchMethodException | SecurityException e) {
			throw new XML_TypeCompilationException("Failed to find default constructor");
		}

		/* </constructor> */


		/* <fields> */


		/* search through all methods of the type */
		for(Method method : getType().getMethods()){

			// get annotations out of the method

			// attributes
			XML_GetAttribute getAttributeAnnotation = method.getAnnotation(XML_GetAttribute.class);
			XML_SetAttribute setAttributeAnnotation = method.getAnnotation(XML_SetAttribute.class);

			//  value
			//XML_GetValue getValueAnnotation = method.getAnnotation(XML_GetValue.class);
			//XML_SetValue setValueAnnotation = method.getAnnotation(XML_SetValue.class);

			// elements
			XML_GetElement getElementAnnotation = method.getAnnotation(XML_GetElement.class);
			XML_SetElement setElementAnnotation = method.getAnnotation(XML_SetElement.class);

			/* Attribute getter -> direct creation */
			if(getAttributeAnnotation!=null){
				typeHandler.attributeGetters.add(AttributeGetter.create(method));	
			}

			/* Attribute setter ->  direct creation */
			else if(setAttributeAnnotation!=null){
				typeHandler.attributeSetters.put(setAttributeAnnotation.name(), AttributeSetter.create(method));	
			}

			/*
				if(getValueAnnotation!=null){
					valueGetter = ValueGetter.create(method);
				}
				else if(setValueAnnotation!=null){
					typeHandler.valueSetter = AttributeSetter.create(method);
				}
			 */

			/* element getter -> direct creation */
			else if(getElementAnnotation!=null){
				elementGetBuilders.add(ElementGetter.create(method));	
			}

			/* element setter -> build */
			else if(setElementAnnotation!=null){
				elementSetBuilders.add(ElementSetter.create(method));
			}
		}

		/* </fields> */
	}

	/**
	 * explore all referenced types
	 * 
	 * @param contextBuilder
	 * @throws XML_TypeCompilationException 
	 */
	public void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {

		// typeAnnotation has already been checked before
		XML_Type typeAnnotation  = getType().getAnnotation(XML_Type.class);

		/* <annotated_sub-types> */
		if(typeAnnotation.sub()!=null){
			for(Class<?> subType : typeAnnotation.sub()) {
				contextBuilder.register(subType);
			}
		}
		/* </annotated_sub-types> */

		// explore element getters
		for(ElementGetter.Builder builder : elementGetBuilders) {
			builder.explore(contextBuilder);
		}

		// explore through element setters
		for(ElementSetter.Builder builder : elementSetBuilders) {
			builder.explore(contextBuilder);
		}
	}


	/**
	 * 
	 * @param contextBuilder
	 * @return
	 * @throws XML_TypeCompilationException
	 */
	public boolean build(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {
		if(!isBuilt) {

			/* <inheritance> */
			if(!isInheritanceBuilt) {
				Map<String, TypeBuilder> map = new HashMap<String, TypeBuilder>();

				listSubTypes(contextBuilder, map);

				typeHandler.subTypes = map.values().stream().map(t -> t.getHandler()).toArray(size -> new TypeHandler[size]);
				isInheritanceBuilt = true;	
			}

			/* <getters> */
			if(!isGettersBuilt) {
				for(ElementGetter.Builder builder : elementGetBuilders) {
					builder.build(this);
				}
				isGettersBuilt = true;
			}
			/* </getters> */

			/* <setters> */
			if(!isSettersBuilt0) {
				boolean hasMissingBuilds = false;
				for(ElementSetter.Builder builder : elementSetBuilders) {
					boolean isBuildMissingDependencies = builder.build0(contextBuilder, this);
					if(isBuildMissingDependencies) {
						hasMissingBuilds = true;
					}
				}
				isSettersBuilt0 = !hasMissingBuilds;
			}

			if(isSettersBuilt0 && !isSettersBuilt1) {
				boolean hasMissingBuilds = false;
				for(ElementSetter.Builder builder : elementSetBuilders) {
					boolean isBuildMissingDependencies = builder.build1(contextBuilder, this);
					if(isBuildMissingDependencies) {
						hasMissingBuilds = true;
					}
				}
				isSettersBuilt1 = !hasMissingBuilds;
			}

			/* </setters> */

			isBuilt = isSettersBuilt1;
		}
		return isBuilt;
	}



	public void buildInheritance(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {


	}



	/**
	 * 
	 * @return
	 */
	public boolean isInheritanceDiscovered() {
		return isInheritanceBuilt;
	}

	/**
	 * 
	 * @param context
	 * @param initializables
	 * @param subTypes
	 * @throws XML_TypeCompilationException
	 */
	public void listSubTypes(XML_ContextBuilder contextBuilder, Map<String, TypeBuilder> map) 
			throws XML_TypeCompilationException {



		// typeAnnotation has already been checked before
		XML_Type typeAnnotation  = getType().getAnnotation(XML_Type.class);

		TypeBuilder subTypeHandler;

		/* <annotated_sub-types> */
		if(typeAnnotation.sub()!=null){

			for(Class<?> subType : typeAnnotation.sub()) {

				// check if already registered
				if(!map.containsKey(subType.getName())) {

					// register type (if not already done)
					contextBuilder.register(subType);

					// test inheritance
					if(typeHandler.type.isAssignableFrom(subType)) {
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
				if(!map.containsKey(subType.getName()) && subType.getSuperclass().equals(typeHandler.type)) {

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



	/*
@Deprecated
private boolean isDependenciesInitialized() {
	for(TypeBuilder dependency : dependencies) {
		if(!dependency.isInheritanceDiscovered()) {
			return false;
		}
	}
	return true;
}
	 */





	public boolean isSetElementColliding(String tag) {
		return typeHandler.elementSetters.containsKey(tag);
	}

	public void putElementGetter(ElementGetter elementGetter) {
		typeHandler.elementGetters.add(elementGetter);
	}

	public void putElementSetter(ElementSetter elementSetter) {
		typeHandler.elementSetters.put(elementSetter.getTag(), elementSetter);
	}
	
	@Override
	public String toString() {
		return "builder for: "+typeHandler.toString();
	}
}
