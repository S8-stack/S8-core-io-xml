package com.s8.lang.xml.handler.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.s8.lang.xml.XML_ContextBuilder;
import com.s8.lang.xml.annotation.XML_GetAttribute;
import com.s8.lang.xml.annotation.XML_GetElement;
import com.s8.lang.xml.annotation.XML_GetValue;
import com.s8.lang.xml.annotation.XML_SetAttribute;
import com.s8.lang.xml.annotation.XML_SetElement;
import com.s8.lang.xml.annotation.XML_SetValue;
import com.s8.lang.xml.handler.Initializable;
import com.s8.lang.xml.handler.list.ListHandler;
import com.s8.lang.xml.handler.type.CollectionElementFieldSetter.Entry;
import com.s8.lang.xml.handler.type.TypeHandler.Putter;

public class TypeFieldsInitializable implements Initializable {


	private boolean isInitialized;
	
	private TypeHandler typeHandler;
	
	private List<TypeBuilder> dependencies;
	
	public TypeFieldsInitializable(XML_ContextBuilder contextBuilder, TypeHandler typeHandler) 
			throws XML_TypeCompilationException {
		super();
		
		this.typeHandler = typeHandler;

		isInitialized = false;
		
		// build dependencies
		this.dependencies = new ArrayList<>(8);
		
		/* <fields> */
		XML_SetElement setElementAnnotation;

		for(Method method : typeHandler.getType().getMethods()){
			setElementAnnotation = method.getAnnotation(XML_SetElement.class);
			if(setElementAnnotation!=null){
				ElementFieldSetter.listDependencies(contextBuilder, method, dependencies);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isDependenciesInitialized() {
		for(TypeBuilder dependency : dependencies) {
			if(!dependency.isInheritanceDiscovered()) {
				return false;
			}
		}
		return true;
	}
	

	@Override
	public boolean initialize(XML_ContextBuilder context) throws XML_TypeCompilationException {
		if(!isInitialized && isDependenciesInitialized()) {


			/* <fields> */

			XML_GetAttribute getAttributeAnnotation;
			XML_SetAttribute setAttributeAnnotation;
			XML_GetValue getValueAnnotation;
			XML_SetValue setValueAnnotation;
			XML_GetElement getElementAnnotation;
			XML_SetElement setElementAnnotation;

			List<ElementFieldSetter.Generator> elementSetterGenerators = new ArrayList<>();

			CollectionElementFieldSetter.Factory factory = new CollectionElementFieldSetter.Factory() {
				public @Override Entry createEntry(TypeHandler handler) throws XML_TypeCompilationException {
					
					/*
					 * Note that at this point, all handlers have been resolved as dependencies
					 */
					ListHandler listHandler = new ListHandler(handler);
					return new Entry(typeHandler.nLists++, listHandler);
				}
			};


			for(Method method : typeHandler.getType().getMethods()){
				getAttributeAnnotation = method.getAnnotation(XML_GetAttribute.class);
				setAttributeAnnotation = method.getAnnotation(XML_SetAttribute.class);
				getValueAnnotation = method.getAnnotation(XML_GetValue.class);
				setValueAnnotation = method.getAnnotation(XML_SetValue.class);
				getElementAnnotation = method.getAnnotation(XML_GetElement.class);
				setElementAnnotation = method.getAnnotation(XML_SetElement.class);

				if(getAttributeAnnotation!=null){
					typeHandler.attributeGetters.add(AttributeGetter.create(method));	
				}
				else if(setAttributeAnnotation!=null){
					typeHandler.attributeSetters.put(setAttributeAnnotation.name(), AttributeSetter.create(method));	
				}
				if(getValueAnnotation!=null){
					typeHandler.valueGetter = AttributeGetter.create(method);
				}
				else if(setValueAnnotation!=null){
					typeHandler.valueSetter = AttributeSetter.create(method);
				}
				else if(getElementAnnotation!=null){
					typeHandler.elementGetters.add(ElementGetter.create(method));	
				}
				else if(setElementAnnotation!=null){
					elementSetterGenerators.add(ElementFieldSetter.create(context, method, factory));
				}
			}

			Putter putter = new Putter() {
				public @Override void put(ElementFieldSetter setter) throws XML_TypeCompilationException {
					if(typeHandler.elementSetters.containsKey(setter.getTag())) {
						throw new XML_TypeCompilationException(
								"Conflict in standard element setter mapping, for field tag: "+setter.getTag()
								+" in type "+typeHandler.getType().getName());
					}
					typeHandler.elementSetters.put(setter.getTag(), setter);
				}
			};

			// check non-contextual level
			for(ElementFieldSetter.Generator gen : elementSetterGenerators) {
				gen.getStandardSetters(putter);
			}


			int nGen = elementSetterGenerators.size();
			if(nGen>1) {
				for(int i0=0; i0<nGen; i0++) {
					ElementFieldSetter.Generator gen0 = elementSetterGenerators.get(i0);
					int i1=0;
					while(gen0.areContextualTagsEnabled() && i1<nGen) {
						if(i1!=i0) {
							ElementFieldSetter.Generator gen1 = elementSetterGenerators.get(i1);
							if(gen1.areContextualTagsEnabled()) {
								if(gen0.isContextuallyConflictingWith(gen1)) {
									gen0.disableContextualTags();
									gen1.disableContextualTags();
								}
							}
						}
						i1++;
					}
				}	
			}

			for(int i=0; i<nGen; i++) {
				ElementFieldSetter.Generator gen = elementSetterGenerators.get(i);
				if(gen.areContextualTagsEnabled()) {
					gen.getContextualSetters(putter);
				}
			}

			/* </fields> */
			
			isInitialized = true;
		}
		return isInitialized;
	}
}
