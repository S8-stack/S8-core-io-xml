package com.s8.io.xml.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface XML_SetAttribute {

	public String name();
	
	public boolean isRequired() default false;
	
}
