import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;


public class Main {

	public static void main(String[] args) throws IOException {
		// args[0] : start from project id 
		List<User> users = new LinkedList<User>();

		for (int i = 1 ; i < args.length; i = i + 2) {
			users.add(new User(args[i], args[i+1]));
		}

		int nbreUser = 0;

		int total = 0; 
		int javaProjects = 0;
		String since = "0";
		for (User user : users) {
			GitHub github = GitHub.connectUsingPassword(user.getUsername(), user.getPassword());
			// Extract all the GitHub public repositories 
			PagedIterable<GHRepository> repositories = github.listAllPublicRepositories(
					since);
			List<String> listJavaProjects = new LinkedList<String>();
			for (GHRepository ghRepository : repositories) {
				total ++;
				try {
					// Apparently, ghRepository.getLanguage() is not working, 
					// so I developed javaIsPrimaryLanguage
					if (javaIsPrimaryLanguage(ghRepository)) {
						javaProjects ++;
						System.out.println(ghRepository.getFullName());
						listJavaProjects.add(ghRepository.getFullName());
					}
					if (total == 4000*(nbreUser + 1)) {
						since = ghRepository.getId() + "";
						nbreUser ++;
						break;
					}
					System.err.println("FROM : " + since + " : " + javaProjects + " / " + total);
				} catch (Exception e) {
				}
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
