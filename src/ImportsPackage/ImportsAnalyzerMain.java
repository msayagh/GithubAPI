package ImportsPackage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import fileUtilities.ReadingUtilities;

public class ImportsAnalyzerMain {

	public static void main(String[] args) throws IOException {
		// args[0] : users file
		// args[1] : java projects file
		List<String> users = ReadingUtilities.getLines(args[0]);
		List<String> javaProjects = ReadingUtilities.getLines(args[1]);

		for (int i = 0 , j = 0 ; i < javaProjects.size(); i ++, j++) {

			String currentJavaProject = javaProjects.get(i);
			String user = users.get(j);
			String login = user.split(" ")[0];
			String pwd = user.split(" ")[1];

			printMap(currentJavaProject, 
					ImportsAnalyzer.get_File_Imports(login, pwd, currentJavaProject));

			if (j == users.size() - 1) {
				j = 0;
			}

		}
	}

	public static void printMap (String javaProject, Map<String, List<String>> imports) {
		if (imports != null) {
			for (String key : imports.keySet()) {
				if (imports.get(key) != null) {
					for (String imp : imports.get(key)) {
						System.out.println(javaProject + "," + key + "," + imp);
					}
				}
			}
		}
	}

}
