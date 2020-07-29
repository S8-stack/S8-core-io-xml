package com.s8.lang.xml.parser;

import java.io.IOException;

/**
 * <p>
 * On why not recursive: "However, the textbook is incorrect in the context of
 * Java. Current Java compilers do not implement tail-call optimization,
 * apparently because it would interfere with the Java security implementation,
 * and would alter the behaviour of applications that introspect on the call
 * stack for various purposes."
 * See <a href="https://stackoverflow.com/questions/105834/does-the-jvm-prevent-tail-call-optimizations">here</a>.
 * </p>
 * 
 * @author pc
 *
 */
public interface ParsedScope {
	
	
	
	/**
	 * 
	 * @param reader
	 * @throws IOException 
	 * @throws XML_ParsingException 
	 */
	public abstract void parse(XML_Parser parser, XML_StreamReader reader) throws IOException, XML_ParsingException;
	
	
	

	public static boolean isBlank(String str){
		int n = str.length();
		if(n>0){
			for(int i=0; i<n; i++){
				if(str.charAt(i)!=' '){
					return false;
				}
			}
			return true;	
		}
		else{
			return true;
		}	
	}

}
