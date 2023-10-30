package com.s8.core.io.xml.composer;

@FunctionalInterface
public interface TagComposer {

	/**
	 * 
	 * @param declaredFieldName
	 * @param typeName
	 * @return
	 */
	public String compose(String typeName);
	
}
