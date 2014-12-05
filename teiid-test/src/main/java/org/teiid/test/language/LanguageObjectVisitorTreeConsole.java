package org.teiid.test.language;

import org.teiid.test.cli.TreeInputConsole;
import org.teiid.test.cli.TreeNode;

public class LanguageObjectVisitorTreeConsole extends TreeInputConsole {

	public LanguageObjectVisitorTreeConsole(String name) {
		super(name);
		setRootNode(new TreeNode("LanguageObjectVisitor", "", null, null));
	}
	
	@Override
	protected void handleADD(String pointer) {
		String[] array = pointer.split(" ");
		
		if(array.length != 1) {
			promptErrorCommand(pointer);
			return ;
		}
		
		String name = readString("Enter Node Name:", true);
		TreeNode node = new TreeNode(name, "", getCurrentNode(), null);
		if(addValidation.validate(node)){
			getCurrentNode().getSons().add(node);
		}
	}

}
