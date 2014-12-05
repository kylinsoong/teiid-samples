package org.teiid.test.language;

import java.io.IOException;

public class TreeList {

	public static void main(String[] args) throws IOException {
//		TeiidLanguageConsole console = new TeiidLanguageConsole("");
		
//		MetadataReferenceTreeInputConsole console = new MetadataReferenceTreeInputConsole("");
		
		LanguageObjectVisitorTreeConsole console = new LanguageObjectVisitorTreeConsole("");
		
		console.start();
	}

}
