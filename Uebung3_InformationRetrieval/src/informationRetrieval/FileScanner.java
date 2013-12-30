package informationRetrieval;
import java.io.*;
import java.util.*;

/**
 * Class FileScanner
 * Scanns a file
 * 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 */
public class FileScanner {

	/**
	 * Scanns a file and returns a HashMap with the words and their frequency
	 * @param filepath
	 * @return HashMap<String, Integer> with the word counts
	 * @throws IOException
	 */
	public static HashMap<String, Integer> readFile(String filepath) throws IOException {
		HashMap<String, Integer> list = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		String line = null;
		do  {
			line = reader.readLine();
			if (line != null) checkLine(line, list);
		} while (line != null);
		reader.close();
		return list;
	}

	/**
	 * Checks a line and count the word frequency
	 * @param line
	 * @param wordCount
	 */
	public static void checkLine(String line, HashMap<String, Integer> wordCount) {
		String[] words = Util.cleanString(line).split(" ");
		for (int i = 0; i < words.length; i++) {
			if (!words[i].isEmpty()) {
				if (wordCount.containsKey(words[i])) wordCount.put(words[i], wordCount.get(words[i]) + 1);
				else 								 wordCount.put(words[i], 1);
			}
		}
	}
	


}
