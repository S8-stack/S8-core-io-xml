
module com.s8.blocks.xml {
	

	exports com.s8.blocks.xml;

	exports com.s8.blocks.xml.annotations;
	
	exports com.s8.blocks.xml.parser;

	exports com.s8.blocks.xml.composer;
	
	exports com.s8.blocks.xml.handler;
	
	// type
	exports com.s8.blocks.xml.handler.type;
	
	// attributes
	exports com.s8.blocks.xml.handler.type.attributes.getters;
	exports com.s8.blocks.xml.handler.type.attributes.setters;
	
	// elements
	exports com.s8.blocks.xml.handler.type.elements.getters;
	exports com.s8.blocks.xml.handler.type.elements.setters;
	
	//exports com.s8.io.xml.tests;
	//exports com.s8.io.xml.tests.examples;
	
	//opens com.s8.io.xml.tests.examples;
	requires transitive com.s8.alpha;
}