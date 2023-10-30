package com.s8.core.io.xml.handler.type;

@FunctionalInterface
public interface UtilityConsumer<T> {

	
	public void accept(T builder);
	
}
