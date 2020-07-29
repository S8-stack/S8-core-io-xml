package com.s8.lang.xml.handler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.s8.lang.xml.composer.XML_Composer;
import com.s8.lang.xml.composer.XML_StreamWriter;
import com.s8.lang.xml.handler.type.TypeHandler;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;
import com.s8.lang.xml.parser.XML_Parser;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader;

/**
 * <h1>XML Context</h1>
 * <h2>Syntax</h2>
 * <p>
 * XML context is now supporting a wider syntax:
 * </p>
 * <ul>
 * <li>Only type annotated as <code>isRoot=true</code> are eligible as
 * roots.</li>
 * <li>Field elements are declared with the default syntax
 * <code>{$field_name}:{$type_name}</code></li>
 * <li><b>Contextual naming</b>. Note that <b>it is allowed that types names
 * conflict in the global scope</b>, they just need not to raise conflict on a
 * specific field. For instance, you can have 3 different types called
 * <code>Function</code> as long as no field possible elements includes more
 * than one of them.</li>
 * <li>Reference to the list can be omitted when there is no conflicts. For
 * instance if a JAVA object (XML-called <code>my-object</code>) has 3 lists of
 * elements whose types are different (say: List of View, List of Callback, List
 * of Schematics), them the following syntax is correct:
 * 
 * <pre>
 * {@code
 * 		<my-object>
 * 			<view id="view01"/>
 * 			<view id="view01"/>
 * 			<view id="view01"/>
 * 			<callback func="whatdoyouwanttodo()"/>
 * 		</my-object>
 * }
 * </pre>
 * 
 * </li>
 * </ul>
 * <p>
 * <h2>Implementation notes</h2>
 * <p>
 * All setting (i.e. support of the various syntaxes exposed above)
 * possibilities are hard-compiled when building the type handler.
 * </p>
 * 
 * @author pc
 *
 */
public class XML_Context {

	private boolean isVerbose = false;
	
	//Map<String, TypeHandler> xmlRoots = new HashMap<>();

	Map<String, TypeHandler> mapByTag = new HashMap<>();

	Map<String, TypeHandler> mapByClassname = new HashMap<>();
	
	private DTD_TemplateGenerator DTD_templateGenerator;

	public XML_Context(Class<?>... types) throws XML_TypeCompilationException {
		super();

		// create builder and run it to get the context compiled
		XML_ContextBuilder builder = new XML_ContextBuilder(this, types, null);
		builder.build();
		
		DTD_templateGenerator = new DTD_TemplateGenerator(this);
	}
	
	/**
	 * 
	 * @param types
	 * @throws Exception 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public XML_Context(Class<?>[] types, Class<?>[] extensions) throws XML_TypeCompilationException {
		super();

		// create builder and run it to get the context compiled
		XML_ContextBuilder builder = new XML_ContextBuilder(this, types, extensions);
		builder.build();
	}
	
	


	


	public void setVerbosity(boolean isVerbose) {
		this.isVerbose = isVerbose;
	}




	/**
	 * 
	 * @param type
	 * @return
	 */
	/*
	public TypeHandler getTypeHandler(Class<?> type){
		return typeMap.get(type.getName());
	}
	*/


	/**
	 * 
	 * @param tag
	 * @return
	 */
	public TypeHandler getTypeHandlerByTag(String tag) {
		return mapByTag.get(tag);
	}
	
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public TypeHandler getTypeHandlerByClass(Class<?> type) {
		return mapByClassname.get(type.getName());
	}


	/**
	 * 
	 * @param reader
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public Object deserialize(Reader reader) throws XML_ParsingException, IOException {
		XML_StreamReader streamReader = new XML_StreamReader(reader, isVerbose);
		Object object = new XML_Parser(this, streamReader, isVerbose).parse();
		streamReader.close();
		return object;
	}

	public Object deserialize(InputStream inputStream) throws XML_ParsingException, IOException {
		return deserialize(new InputStreamReader(inputStream));
	}

	public Object deserialize(File file) throws XML_ParsingException, IOException {
		try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))){
			Object result = deserialize(inputStream);
			inputStream.close();
			return result;	
		}
	}




	public void serialize(Object object, Writer writer) throws Exception{
		XML_StreamWriter streamWriter = new XML_StreamWriter(writer);
		new XML_Composer(this, streamWriter).compose(object);
		streamWriter.close();
	}

	public void serialize(Object object, OutputStream outputStream) throws Exception{
		serialize(object, new OutputStreamWriter(outputStream));
	}

	
	/**
	 * 
	 * @param object
	 * @param file
	 * @throws Exception
	 */
	public void serialize(Object object, File file) throws Exception{
		serialize(object, new FileOutputStream(file));
	}
	
	
	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void DTD_writeTemplate(Writer writer) throws IOException {
		DTD_templateGenerator.write(writer);
	}

}
