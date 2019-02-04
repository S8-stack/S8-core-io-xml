package com.qx.lang.xml;

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

import com.qx.lang.xml.composer.XML_Composer;
import com.qx.lang.xml.composer.XML_StreamWriter;
import com.qx.lang.xml.handler.TypeHandler;
import com.qx.lang.xml.parser.XML_Parser;
import com.qx.lang.xml.parser.XML_ParsingException;
import com.qx.lang.xml.parser.XML_StreamReader;

/**
 * 
 * @author pc
 *
 */
public class XML_Context {

	private boolean isVerbose = false;

	private Map<String, TypeHandler> serialMap = new HashMap<>();

	private Map<String, TypeHandler> deserialMap = new HashMap<>();

	/**
	 * 
	 * @param types
	 * @throws Exception 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public XML_Context(Class<?>... types) throws Exception {
		super();
		for(Class<?> type : types){
			discover(type);
		}
	}
	
	public void setVerbosity(boolean isVerbose) {
		this.isVerbose = isVerbose;
	}
	

	/**
	 * 
	 * @param type
	 * @throws Exception 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void discover(Class<?> type) throws Exception {
		TypeHandler typeHandler = new TypeHandler(type);
		if(!deserialMap.containsKey(typeHandler.getDeserialName())){

			serialMap.put(typeHandler.getSerialName(), typeHandler);

			deserialMap.put(typeHandler.getDeserialName(), typeHandler);

			try {
				typeHandler.initialize(this);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new Exception("Failed to initialize "+type.getName()+" due to "+e.getMessage());
			}
		}
	}

	public boolean isRegistered(String name){
		return serialMap.containsKey(name);
	}


	/**
	 * 
	 * @param deserialName
	 * @return
	 */
	public TypeHandler getBySerialName(String serialName){
		return serialMap.get(serialName);
	}

	public TypeHandler getByDeserialName(String deserialName){
		return deserialMap.get(deserialName);
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

	public Object deserialize(InputStream inputStream) throws Exception{
		return deserialize(new InputStreamReader(inputStream));
	}
	
	public Object deserialize(File file) throws Exception{
		return deserialize(new FileInputStream(file));
	}

	public void serialize(Object object, Writer writer) throws Exception{
		XML_StreamWriter streamWriter = new XML_StreamWriter(writer);
		new XML_Composer(this, streamWriter).compose(object);
		streamWriter.close();
	}
	
	public void serialize(Object object, OutputStream outputStream) throws Exception{
		serialize(object, new OutputStreamWriter(outputStream));
	}
	
	public void serialize(Object object, File file) throws Exception{
		serialize(object, new FileOutputStream(file));
	}

}
