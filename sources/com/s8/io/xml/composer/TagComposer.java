package com.s8.io.xml.composer;

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
