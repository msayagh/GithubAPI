import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ImportsPackage.ImportsAnalyzer;


public class ImportsAnalyzerTest {

	@Test
	public void testGet_File_Imports() {
		try {
			Map<String, List<String>> r = ImportsAnalyzer.get_File_Imports("msayagh", "secree7", "albb0920/LunaTerm");
			System.out.println(r.size());
			for (String key : r.keySet()) {
				System.out.println(key);
			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
	}

}
