package com.qx.lang.xml.parser;

import java.io.IOException;
import java.util.Stack;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import com.qx.lang.xml.XML_Syntax;
import com.qx.lang.xml.handler.ObjectHandler;
import com.qx.lang.xml.handler.XML_Context;
import com.qx.lang.xml.reader.XML_StreamReader;

public class Parsing {

	protected State state;
	
	protected XML_Context context;

	protected XML_StreamReader reader;

	protected Stack<ObjectHandler> stack;

	/**
	 * 
	 * @author pc
	 *
	 */
	private abstract class State {

		public abstract void parse(Parsing parsing) throws Exception;

	}


	/**
	 * current tag
	 */
	private String tag;
	
	private State readTag = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			reader.readNext(null, null);
			reader.check(XML_Syntax.START_OF_TAG);
			reader.readNext(null, null);
			if(reader.isCurrent(XML_Syntax.TAG_END_MARKER)){
				tag = reader.read(new char[]{'>'}, new char[]{' '}, new char[]{',', '=', '"', '/'});
				String openingTag = stack.peek().typeHandler.getName();
				if(stack.peek().typeHandler.getName().equals(tag)){
					stack.pop();
					state = readTag;
				}
				else{
					throw new Exception("Closing tag "+tag+" is not matching opening tag "+openingTag);
				}
			}
			else{
				// no white spaces allowed between '<' and tag word start
				tag = reader.getCurrentChar()+reader.read(new char[]{' ','/','>'}, null, new char[]{',', '=', '"'});
				if(reader.isCurrent(XML_Syntax.WHITE_SPACE)){
					stack.push(context.create(tag));
					// update stack
					state = readAttributeName;
				}
				else if(reader.isCurrent('>')){
					stack.push(context.create(tag));
					state = readTag;
				}
				else if(reader.isCurrent('/')){
					reader.readNext();
					reader.check('>');
					stack.pop();
					state = readTag;
				}
				else{
					throw new RuntimeException("Uncontrolled read stop");
				}
			}
		}
	};

	
	
	private String attributeName;
	
	private State readAttributeName = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			reader.skipWhiteSpace();
			if(reader.getCurrentChar()==XML_Syntax.END_OF_TAG){
				state = readElement;
			}
			else if(reader.getCurrentChar()==XML_Syntax.TAG_END_MARKER){
				reader.readNext();
				if(reader.getCurrentChar()!=XML_Syntax.END_OF_TAG){
					throw new Exception("Expecting end of tag");
				}
				stack.pop();
				state = readElement;
			}
			else{
				attributeName = reader.read('=', new char[]{' '}, new char[]{});
				reader.check(XML_Syntax.ATTRIBUTE_DEFINITION);
				state = readHeaderFieldValue;	
			}
		}
	};
	
	
	private State readHeaderFieldValue = new State() {

		@Override
		public void parse(Parsing parsing) {
			reader.readNextWhileIgnoring(XML_Syntax.WHITE_SPACE);
			reader.check(XML_Syntax.ATTRIBUTE_DEFINITION);
			reader.readUntil(XML_Syntax.ATTRIBUTE_DEFINITION);
			// TODO set field value here
			state = readObjectHeaderFields;
		}
	};
}
