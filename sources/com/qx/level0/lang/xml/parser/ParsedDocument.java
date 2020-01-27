package com.qx.level0.lang.xml.parser;


import java.io.IOException;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.handler.type.TypeHandler;
import com.qx.level0.lang.xml.parser.ParsedObjectElement.Callback;


/**
 * 
 * @author pc
 *
 */
public class ParsedDocument implements Parsed {
	
	
	private XML_Context context;

	private Object rootObject;


	/**
	 * current parsing state
	 */
	private State state;


	private boolean isParsing;

	private boolean isClosed;


	/**
	 * 
	 * @param tag
	 * @param callback
	 * @param handler
	 * @throws XML_ParsingException
	 */
	public ParsedDocument(XML_Context context) {
		super();
		this.context = context;
	
		// initialize
		state = new ReadOpeningTag();

	}

	
	public Object getRootObject() {
		return rootObject;
	}


	@Override
	public void parse(XML_Parser parser, XML_StreamReader reader) throws IOException, XML_ParsingException {
		if(isClosed) {
			throw new XML_ParsingException(reader.getPoint(), "This scope has already been closed");
		}
		isParsing = true;
		while(isParsing){
			state.parse(parser, reader);
		}
	}



	/**
	 * 
	 * @author pc
	 *
	 */
	private interface State {

		public abstract void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException;

	}

	private class ReadOpeningTag implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			reader.readNext();
			reader.skip(' ', '\t' , '\n');
			reader.check('<');
			reader.readNext();
			// closing tag
			if(reader.isCurrent('/')){
				throw new XML_ParsingException(reader.getPoint(), "Unexpected closing tag at this point");
			}
			else if(reader.isCurrent('!')){
				reader.readNext();
				state = new ReadComment();
			}
			else if(reader.isCurrent('?')){
				state = new ReadHeader();
			}
			else{
				String tag = reader.until(
						/* stop at */ new char[]{'>', ' ', '/', '\n'},
						/* ignore */ null,
						/* forbid */ new char[]{',', '=', '"'});

				/* create new scope */

				TypeHandler handler = context.getXmlRootTypeHandler(tag);
				if(handler==null) {
					throw new XML_ParsingException(reader.getPoint(), "Failed to find root type for tag: "+tag
							+", have you declared this type as root (isRoot = true in annotation)?");
				}
				
				// switch to new scope
				Callback callback = new ParsedObjectElement.Callback() {
					@Override
					public void set(Object object) throws XML_ParsingException {
						ParsedDocument.this.rootObject = object;
					}
				};
				parser.scope = new ParsedObjectElement(null, callback, tag, handler, reader.getPoint());

				// and escape this scope...
				isParsing = false;

				// ...and NEVER come back as reading content
			}
		}
	}



	private class ReadComment implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			String comment = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ null,
					/* forbid */ null);
			reader.readNext();
			if(parser.isVerbose) {
				System.out.println("XML COmment: "+comment);	
			}
			state = new ReadOpeningTag();
		}
	}
	
	
	private class ReadHeader implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			String comment = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ null,
					/* forbid */ null);
			reader.readNext();
			if(parser.isVerbose) {
				System.out.println("XML COmment: "+comment);	
			}
			state = new ReadOpeningTag();
		}
	}
}
