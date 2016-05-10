import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;


public class Main {

	public static void main(String[] args) throws IOException {
		// args[0] : your github login
		// args[1] : your github password
		GitHub github = GitHub.connectUsingPassword(args[0], args[1]);
		// Extract all the GitHub public repositories 
		PagedIterable<GHRepository> repositories = github.listAllPublicRepositories();
		for (GHRepository ghRepository : repositories) {
			try {
				// Apparently, ghRepository.getLanguage() is not working, so I developed javaIsPrimaryLanguage
				if (javaIsPrimaryLanguage(ghRepository)) {
					
					System.out.println(ghRepository.getUrl());
					/*
					 * The following code allows to traverse the content of a repository
					 */
					/*
					 List<GHContent> content = ghRepository.getDirectoryContent("");

					for (GHContent ghContent : content) {
					//	System.out.println(ghContent.getName());
					}*/
				}
			} catch (Exception e) {
			}
		}

	}

	public static boolean javaIsPrimaryLanguage(GHRepository ghRepository) throws IOException {
		String l = getPrimaryLanguage(ghRepository);

		if (l != null && l.toLowerCase().equals("java")) {
			return true;
		}
		return false;
	}

	public static String getPrimaryLanguage (GHRepository ghRepository) throws IOException {
		int max = 0;
		String result = null;
		Map languages = ghRepository.listLanguages();
		if (languages != null) {
			for (Object language: languages.keySet()) {
				if (max < (int) languages.get(language)) {
					max = (int) languages.get(language);
					result = (String) language;
				}
			}
		}
		return result;
	}

}
