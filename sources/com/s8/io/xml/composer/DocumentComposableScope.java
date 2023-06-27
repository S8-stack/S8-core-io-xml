package com.s8.io.xml.composer;

import java.io.IOException;

import com.s8.io.xml.XML_Syntax;
import com.s8.io.xml.codebase.XML_Codebase;



/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class DocumentComposableScope implements ComposableScope {


	private Object root;


	public DocumentComposableScope(Object object) throws XML_ComposingException {
		super();
		this.root = object;
	}


	@Override
	public void compose(XML_Codebase context, XML_StreamWriter writer)
			throws IOException, XML_ComposingException {



		// write header
		writer.append(XML_Syntax.HEADER+"\n");

		new ObjectComposableScope(typeName -> typeName, root).compose(context, writer);
	}

}
