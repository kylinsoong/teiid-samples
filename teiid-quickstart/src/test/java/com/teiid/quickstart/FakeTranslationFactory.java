package com.teiid.quickstart;

import org.teiid.query.unittest.RealMetadataFactory;

public class FakeTranslationFactory {
	
	private static FakeTranslationFactory instance = new FakeTranslationFactory();
	
	public static FakeTranslationFactory getInstance() {
		return instance;
	}
	
	public TranslationUtility getExampleTranslationUtility() {
		return new TranslationUtility(RealMetadataFactory.example1());
	}

}
