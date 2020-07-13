package com.s8.lang.xml.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface XML_Type {

	public String name();
	
	public Class<?>[] sub() default {};
	
	/**
	 * Tells whether this type can be used as a root type.
	 * @return
	 */
	//public boolean isRoot() default false;
}
