package com.qx.lang.xml.parser;

import java.io.IOException;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import com.qx.lang.xml.XML_Syntax;
import com.qx.lang.xml.reader.XML_StreamReader;

public class Parsing {

	protected State state;

	protected XML_StreamReader reader;

	//protected Stakc stack;

	/**
	 * 
	 * @author pc
	 *
	 */
	private abstract class State {

		public abstract void parse(Parsing parsing) throws Exception;

	}

	private State acquireObject = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			reader.readNext();
			reader.check(XML_Syntax.START_OF_TAG);
		}
	};

	private String currentTag;
	
	private State readCurrentTag = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			currentTag = reader.readUntilOneOf(XML_Syntax.KEY_CHARS);
			reader.check(XML_Syntax.WHITE_SPACE);
			// update stack
			state = readHeaderField;
		}
	};
	
	private String currentHeaderFieldName;
	
	private State readHeaderFieldName = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			reader.skipWhiteSpace();
			if(reader.getCurrentChar()==XML_Syntax.END_OF_TAG){
				
			}
			else{
				currentHeaderFieldName = reader.read('=', new char[]{});
				reader.check(XML_Syntax.HEADER_FIELD_DEFINITION);
				state = readHeaderFieldValue;	
			}
		}
	};
	
	
	private State readHeaderFieldValue = new State() {

		@Override
		public void parse(Parsing parsing) {
			reader.readNextWhileIgnoring(XML_Syntax.WHITE_SPACE);
			reader.check(XML_Syntax.HEADER_FIELD_DEFINITION);
			reader.readUntil(XML_Syntax.HEADER_FIELD_DEFINITION);
			// TODO set field value here
			state = readObjectHeaderFields;
		}
	};
}
