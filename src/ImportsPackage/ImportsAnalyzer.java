package ImportsPackage;
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

	/**
	 * THis method gets a map of files and imported packages
	 * 
	 * @param login : user login
	 * @param pwd : password of the user
	 * @param repository : repository name to get its imports
	 * @return a map between each file and its import statements
	 * @throws IOException
	 */
	public static Map<String, List<String>> get_File_Imports(String login, 
			String pwd, String repository) throws IOException {
		// args[0] : your github login
		// args[1] : your github password
		// args[2] : repository address
		try {
			System.out.println("Line to remove by grep :" + login + " - " + pwd + " - " + repository);
			GitHub github = GitHub.connectUsingPassword(login, pwd);
			GHRepository ghRepository = github.getRepository(repository);

			List<GHContent> javaFiles = getJavaFiles(ghRepository, 
					ghRepository.getDirectoryContent(""));

			Map<String, List<String>> imports = new HashMap<String, List<String>>();
			for (GHContent javaFile : javaFiles) {
				imports.put(javaFile.getPath(), getImports(javaFile));
			}
			return imports;
		} catch (Exception e) {
			System.out.println("Line to remove by grep - Eror :" + login + " - " + pwd + " - " + repository);
			return null;
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
				if (ghContent.getName().endsWith(".java")) {
					javaFiles.add(ghContent);
				}
			}
		}

		return javaFiles;

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


}
