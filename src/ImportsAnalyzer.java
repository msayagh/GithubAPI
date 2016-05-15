import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import fileUtilities.WritingUtilities;


public class ImportsAnalyzer {

	public static void main(String[] args) throws IOException {
		// args[0] : your github login
		// args[1] : your github password
		// args[2] : repository address
		// args[3] : import statement
		GitHub github = GitHub.connectUsingPassword(args[0], args[1]);
		GHRepository ghRepository = github.getRepository(args[2]);

		List<GHContent> javaFiles = getJavaFiles(ghRepository, 
				ghRepository.getDirectoryContent(""));

		Map<String, List<String>> imports = new HashMap<String, List<String>>();
		for (GHContent javaFile : javaFiles) {
			imports.put(javaFile.getPath(), getImports(javaFile));
		}

		for (String key : imports.keySet()) {
			if (imports.get(key) != null) {
				System.out.println(key);
				for (String imp : imports.get(key)) {
					System.out.println("\t" + imp);
				}
			} else {
				System.err.println("Parse error : " + key);
			}
		}

	}

	private static List<GHContent> getJavaFiles (GHRepository repository, List<GHContent> content) throws IOException {
		List<GHContent> javaFiles = new LinkedList<GHContent>();
		/*
		 * The following code allows to traverse the content of a repository
		 */

		for (GHContent ghContent : content) {
			if (ghContent.isDirectory()) {
				javaFiles.addAll(getJavaFiles(repository, 
						repository.getDirectoryContent(ghContent.getPath())));
			} else {
				if (isJavaFile(ghContent.getName())) {
					javaFiles.add(ghContent);
				}
			}
		}

		return javaFiles;

	}

	private static boolean isJavaFile(String name) {
		// TODO Auto-generated method stub
		return name.endsWith(".java");
	}

	private static List<String> getImports (GHContent javaFile) throws IOException {

		CompilationUnit cu;
		// parse the file
		try {
			cu = JavaParser.parse(javaFile.read());
			ImportVisitor visitor = new ImportVisitor();
			visitor.visit(cu, null);
			return visitor.getImports();

		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes. 
	 */
	private static class ImportVisitor extends VoidVisitorAdapter {

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
}
