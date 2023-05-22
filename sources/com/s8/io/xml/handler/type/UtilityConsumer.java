package com.s8.io.xml.handler.type;

@FunctionalInterface
public interface UtilityConsumer<T> {

	
	public void accept(T builder);
	
}
