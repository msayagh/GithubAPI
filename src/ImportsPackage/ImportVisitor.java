package ImportsPackage;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple visitor implementation for visiting MethodDeclaration nodes. 
 */
public class ImportVisitor extends VoidVisitorAdapter {

	private List<String> imports = new LinkedList<String>();

	@Override
	public void visit(final ImportDeclaration n, Object arg) {
		imports.add(n.getName().toString());
		super.visit(n, arg);
	}
	public List<String> getImports () {
		return this.imports;
	}

}