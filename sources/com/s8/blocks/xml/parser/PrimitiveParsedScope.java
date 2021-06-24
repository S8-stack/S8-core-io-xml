package com.s8.blocks.xml.parser;

import java.io.IOException;

/**
 * 
 * @author pc
 *
 */
public class PrimitiveParsedScope implements ParsedScope {
	
	
	/**
	 * 
	 * @author pc
	 *
	 */
	public interface Callback {
		
		public void set(String value) throws XML_ParsingException;
		
	}

	/**
	 * 
	 * @author pc
	 *
	 */
	public interface State {

		public abstract void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException;

	}
	
	
	private ParsedScope parent;
	
	private Callback callback;
	
	private boolean isClosed;
	private boolean isParsing;
	
	private State state;
	
	private String tag;
	
	private String value;
	
	
	
	/**
	 * 
	 * @param typeHandler
	 * @param object
	 * @throws Exception 
	 */
	public PrimitiveParsedScope(String tag, ParsedScope parent, Callback callback){
		super();
		this.parent = parent;
		this.callback = callback;
		this.tag = tag;
		this.state = new ReadEndOfTag();
	}
	
	


	@Override
	public void parse(XML_Parser parser, XML_StreamReader reader) 
			throws IOException, XML_ParsingException {
		
		if(isClosed) {
			throw new XML_ParsingException(reader.getPoint(), "This scope has already been closed");
		}
		isParsing = true;
		while(isParsing){
			state.parse(parser, reader);
		}
	}

	
	private class ReadEndOfTag implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {

			reader.skip(' ', '\t' , '\n');
			
			// we found the end of the tag
			if(reader.isCurrent('>')){
				reader.readNext();
				state = new ReadValue();
			}
			else {
				throw new XML_ParsingException(reader.getPoint(), "Expecting char \'>\' at this point");
			}
		}
	};



	private class ReadValue implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {

			// read value until next tag start
			String value = reader.until(
					/* stop at */ new char[]{'<'},
					/* ignore */ null,
					/* forbid */ null);	

			// set value if any
			PrimitiveParsedScope.this.value = value;
			state = new ReadClosingTag();
		}
	};

	private class ReadClosingTag implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			reader.readNext();
			if(!reader.isCurrent('/')) {
				throw new XML_ParsingException(reader.getPoint(), "Excpect '\' at this point");
			}
			reader.readNext();
			String tag = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ new char[]{' '},
					/* forbid */ new char[]{',', '=', '"', '/'});
			
			// check closing tag
			if(!PrimitiveParsedScope.this.tag.equals(tag)) {
				throw new XML_ParsingException(reader.getPoint(), "Closing tag is not matching: "+tag+ "instead of "
						+PrimitiveParsedScope.this.tag+".");
			}
			//reader.readNext();
			state = new CloseScope();
		}
	};

	
	
	private class CloseScope implements State {


		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			callback.set(value);
			isParsing = false;
			// never returning to this scope parser.
			isClosed = true;
			parser.scope = parent;
		}
	}
	
}
