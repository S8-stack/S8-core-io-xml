package com.s8.io.xml.handler.type;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.s8.io.xml.annotations.XML_GetAttribute;
import com.s8.io.xml.annotations.XML_GetElement;
import com.s8.io.xml.annotations.XML_GetValue;
import com.s8.io.xml.annotations.XML_SetAttribute;
import com.s8.io.xml.annotations.XML_SetElement;
import com.s8.io.xml.annotations.XML_SetValue;
import com.s8.io.xml.annotations.XML_Type;
import com.s8.io.xml.codebase.XML_CodebaseBuilder;
import com.s8.io.xml.handler.type.attributes.getters.AttributeGetter;
import com.s8.io.xml.handler.type.attributes.setters.AttributeSetter;
import com.s8.io.xml.handler.type.elements.getters.ElementGetter;
import com.s8.io.xml.handler.type.elements.setters.ElementSetter;
import com.s8.io.xml.handler.type.value.getters.ValueGetter;
import com.s8.io.xml.handler.type.value.setters.ValueSetter;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class TypeBuilder {

	public final XML_Type typeAnnotation;

	public final Class<?> type;

	public final TypeHandler typeHandler;


	private final List<ElementGetter.Builder> elementGetBuilders = new ArrayList<>();

	private final List<ElementSetter.Builder> elementSetBuilders = new ArrayList<>();

	private final List<TypeBuilder> subTypeBuilders = new ArrayList<>();


	public TypeBuilder(Class<?> type) throws XML_TypeCompilationException {
		super();

		this.type = type;

		typeAnnotation  = type.getAnnotation(XML_Type.class);
		if(typeAnnotation==null){
			throw new XML_TypeCompilationException("Missing type declaration for type: "+type.getName());
		}

		typeHandler = new TypeHandler(type, typeAnnotation.name(), typeAnnotation.root());
	}


	public String getRuntimeName() {
		return typeHandler.type.getName();
	}

	public TypeHandler getHandler() {
		return typeHandler;
	}



	public Class<?> getType(){
		return type;
	}





	/**
	 * Initialize and explore all references types
	 * 
	 * @param codebaseBuilder
	 * @param isVerbose
	 * @throws XML_TypeCompilationException
	 */
	public void buildOutline(XML_CodebaseBuilder codebaseBuilder, boolean isVerbose) throws XML_TypeCompilationException {

		/* <explore-super-type> */
		Class<?> superType = type.getSuperclass();
		if(superType != null) {
			codebaseBuilder.discover(superType);
		}
		/* </explore-super-type> */


		/* <explore-sub-types> */
		if(typeAnnotation.sub() != null){
			for(Class<?> subType : typeAnnotation.sub()) {
				codebaseBuilder.discover(subType);

				TypeBuilder subTypeBuilder = codebaseBuilder.getTypeBuilder(subType);
				if(subTypeBuilder == null) {
					throw new XML_TypeCompilationException("Problem while registering type: "+subType);
				}
				subTypeBuilders.add(subTypeBuilder);
			}
		}
		/* </explore-sub-types> */


		/* <constructor> */
		if(!getType().isInterface()) {
			try {
				typeHandler.constructor = getType().getConstructor(new Class<?>[]{});
			}
			catch (NoSuchMethodException | SecurityException e) {
				throw new XML_TypeCompilationException("Failed to find default constructor for type: "+getType());
			}	
		}
		/* </constructor> */


		/* <fields> */

		Set<String> elementGetterNamespace = new HashSet<>();

		Set<String> elementSetterNamespace = new HashSet<>();

		/* search through all methods of the type */
		for(Method method : getType().getMethods()){

			// get annotations out of the method


			/* Attribute getter -> direct creation */
			if(method.isAnnotationPresent(XML_GetAttribute.class)){
				typeHandler.attributeGetters.add(AttributeGetter.create(method));
			}

			/* Attribute setter ->  direct creation */
			else if(method.isAnnotationPresent(XML_SetAttribute.class)){
				AttributeSetter attributeSetter = AttributeSetter.create(method);
				typeHandler.attributeSetters.put(attributeSetter.getName(), attributeSetter);	
			}

			else if(method.isAnnotationPresent(XML_GetValue.class)){
				typeHandler.valueGetter = ValueGetter.create(method);
			}

			else if(method.isAnnotationPresent(XML_SetValue.class)){
				typeHandler.valueSetter = ValueSetter.create(method);
			}

			/* element getter -> direct creation */
			else if(method.isAnnotationPresent(XML_GetElement.class)){
				ElementGetter.Builder egBuilder = ElementGetter.create(method);
				if(elementGetterNamespace.contains(egBuilder.declaredTag)) {
					throw new XML_TypeCompilationException("Conflict in element getter tag, for tag: "+
							egBuilder.declaredTag+", in type: "+typeHandler.type.getName());
				}

				// sub explore
				egBuilder.explore(codebaseBuilder);

				// register
				elementGetterNamespace.add(egBuilder.declaredTag);
				elementGetBuilders.add(egBuilder);
			}

			/* element setter -> build */
			else if(method.isAnnotationPresent(XML_SetElement.class)){
				ElementSetter.Builder esBuilder = ElementSetter.create(method);
				if(elementSetterNamespace.contains(esBuilder.declaredTag)) {
					throw new XML_TypeCompilationException("Conflict in element getter tag, for tag: "+
							esBuilder.declaredTag+", in type: "+typeHandler.type.getName());
				}

				// sub explore
				esBuilder.explore(codebaseBuilder);

				// register
				elementSetterNamespace.add(esBuilder.declaredTag);
				elementSetBuilders.add(esBuilder);
			}
		}

		/* </fields> */
	}




	public void buildInheritance() {

		/* <inheritance> */
		Map<String, TypeBuilder> map = new HashMap<String, TypeBuilder>();

		forSubTypes(subTypeBuilder -> {

			/* put all sub types */
			map.put(subTypeBuilder.getRuntimeName(), subTypeBuilder);	
		});

		typeHandler.subTypes = map.values().stream().map(t -> t.getHandler()).toArray(size -> new TypeHandler[size]);

		/* </inheritance> */

	}




	/**
	 * 
	 * @param lexiconBuilder
	 * @return
	 * @throws XML_TypeCompilationException
	 */
	public void buildAccessors(XML_CodebaseBuilder lexiconBuilder, boolean isVerbose) throws XML_TypeCompilationException {


		int n;

		/* <getters> */
		n = elementGetBuilders.size();

		/* link : all must be linked before computing substition group collisions */
		for(int i = 0; i < n; i++) { 
			elementGetBuilders.get(i).link(lexiconBuilder); 
		}

		// build
		for(int i = 0; i < n; i++) {
			ElementGetter.Builder builder = elementGetBuilders.get(i);
			boolean isColliding = false;
			Set<String> substitutionGroup = builder.getSubstitutionGroup();
			for(int j = 0; j<n; j++) { if(j!=i && elementGetBuilders.get(j).isColliding(substitutionGroup)) isColliding = true; }
				
			builder.build(this, isColliding);
		}
		/* </getters> */

		
		/* <element-setters> */

		n = elementSetBuilders.size();

		/* link : all must be linked before computing substition group collisions */
		for(int i = 0; i < n; i++) { 
			elementSetBuilders.get(i).link(lexiconBuilder); 
		}

		// build
		for(int i = 0; i < n; i++) {
			ElementSetter.Builder builder = elementSetBuilders.get(i);
			boolean isColliding = false;
			Set<String> substitutionGroup = builder.getSubstitutionGroup();
			for(int j = 0; j<n; j++) { if(j!=i && elementSetBuilders.get(j).isColliding(substitutionGroup)) isColliding = true; }

			builder.build(this, isColliding);
		}
		/* </element-setters> */



	}


	public void forSubTypes(UtilityConsumer<TypeBuilder> consumer) {
		Queue<TypeBuilder> queue = new ArrayDeque<>();
		for(TypeBuilder subTypeBuilder : subTypeBuilders) {
			consumer.accept(subTypeBuilder);
			subTypeBuilder.enqueueSubTypes(queue);
		}
	}

	private void enqueueSubTypes(Queue<TypeBuilder> queue) {
		if(!subTypeBuilders.isEmpty()) {
			for(TypeBuilder subTypeBuilder : subTypeBuilders) {
				queue.add(subTypeBuilder);
			}
		}
	}







	public void putElementSetter(String tag, ElementSetter elementSetter) {
		typeHandler.elementSetters.put(tag, elementSetter);
	}

	public void addElementGetter(ElementGetter elementGetter) throws XML_TypeCompilationException {
		typeHandler.elementGetters.add(elementGetter);
	}


	@Override
	public String toString() {
		return "builder for: "+typeHandler.toString();
	}
}
