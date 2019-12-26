package com.qx.level0.lang.xml.parser;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;

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
public abstract class OldParsingScope {
	

	protected String fieldNameInParent;
	
	protected OldParsingScope parent;
	
	public OldParsingScope(OldParsingScope parent, String fieldNameInParent) {
		super();
		this.parent = parent;
		this.fieldNameInParent = fieldNameInParent;
	}


	public abstract OldParsingScope createField(String tag) throws XML_ParsingException;
	
	
	
	public abstract String getTag();
	
	public abstract void close() throws XML_ParsingException;

	public abstract void setValue(String value) throws XML_ParsingException;

	public abstract void setAttribute(String name, String value) throws XML_ParsingException;

	public abstract void setElement(String fieldName, Object object) throws XML_ParsingException ;
	
	
}
