package com.qx.level0.lang.xml.parser2;

import java.io.IOException;

import com.qx.level0.lang.xml.parser2.ParsedObjectElement.ReadContent;
import com.qx.level0.lang.xml.parser2.ParsedObjectElement.State;

public class DocumentParsing {
	


	/**
	 * read doc header
	 */
	private class ReadHeader implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			reader.readNext();
			reader.check("<?xml");
			String value = reader.until(new char[]{'>'}, null, null);
			if(parser.isVerbose) {
				System.out.println("[XML_Parser] read header: "+value);	
			}
			state = new ReadContent();
		}
	};
	
	private class ReadElementHeader implements State {

		@Override
		public void parse(XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			String header = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ null,
					/* forbid */ null);
			if(parser.isVerbose) {
				System.out.println("XML Header: "+header);	
			}
			state = new ReadContent();
		}
	};

}
