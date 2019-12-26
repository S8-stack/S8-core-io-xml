package com.qx.level0.lang.xml.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.qx.level0.lang.xml.handler.list.ListHandler;


/**
 * 
 * @author pc
 *
 */
public class ParsedListElement implements Parsed {

	/**
	 * Callback method for the Object element
	 * @author pc
	 *
	 */
	public interface Callback {

		public void set(List<Object> objects) throws XML_ParsingException;

	}


	/**
	 * <b>Callback #1</b>: for returning to the parent scope when done
	 */
	private Parsed parent;

	/**
	 * <b>Callback #2</b>: for setting the object when done
	 */
	private Callback callback;

	private List<Object> elements;

	private ListHandler handler;

	private String tag;

	private boolean isClosed;

	private boolean isParsing;

	private State state;

	public ParsedListElement(Parsed parent, Callback callback, String tag, ListHandler handler) {
		super();
		this.parent = parent;
		this.callback = callback;
		this.handler = handler;
		this.tag = tag;
		this.elements = new ArrayList<>();

		state = new ReadTagTrailing();
	}



	/**
	 * Add hard object
	 * @param object
	 */
	public void add(Object object) {
		elements.add(object);
	}


	@Override
	public void parse(XML_Parser parser, XML_StreamReader reader) throws IOException, XML_ParsingException {
		if(isClosed) {
			throw new XML_ParsingException(reader, "This scope has already been closed");
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

	private class ReadTagTrailing implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {

			reader.skip(' ', '\t' , '\n');

			if(reader.isCurrent('>')){
				reader.readNext();
				state = new ReadOpeningTag();
			}
			else if(reader.isCurrent('/')){
				reader.readNext();
				reader.check('>');
				reader.readNext();
				state = new CloseScope();
			}
			else {
				throw new XML_ParsingException(reader.getPoint(), "Failed to match end of beginning tag for list");
			}
		}
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
				reader.readNext();
				state = new ReadClosingTag();
			}
			else if(reader.isCurrent('!')){
				reader.readNext();
				state = new ReadComment();
			}
			else if(reader.isCurrent('?')){
				throw new XML_ParsingException(reader, "Illegal header position within an element");
				//state = new ReadElementHeader();
			}
			else{
				String tag = reader.until(
						/* stop at */ new char[]{'>', ' ', '/', '\n'},
						/* ignore */ null,
						/* forbid */ new char[]{',', '=', '"'});

				/* create new scope */

				// switch to new scope
				parser.scope = handler.createParsedElement(ParsedListElement.this, tag, reader.getPoint());

				// and escape this scope...
				isParsing = false;

				// ...and come back as reading content
				state = new ReadOpeningTag();
			}
		}
	}


	private class ReadClosingTag implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			String tag = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ new char[]{' '},
					/* forbid */ new char[]{',', '=', '"', '/'});

			// check closing tag
			if(!ParsedListElement.this.tag.equals(tag)) {
				throw new XML_ParsingException(reader, "Closing tag is not matching: "+tag+ "instead of "
						+ParsedListElement.this.tag+".");
			}

			reader.readNext();
			state = new CloseScope();
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


	private class CloseScope implements State {


		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			// close
			close();
			
			// stop
			isParsing = false;
			
			// and go back to parent
			parser.scope = parent;
		}
	}
	
	
	/**
	 * close and flush data with callback 2 (setter)
	 * @throws XML_ParsingException 
	 */
	public void close() throws XML_ParsingException {
		
		if(!isClosed) {
			
			callback.set(elements);

			// never returning to this scope parser.
			isClosed = true;
		}
	}
}
