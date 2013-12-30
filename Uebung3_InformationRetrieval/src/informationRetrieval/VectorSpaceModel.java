package informationRetrieval;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * class VectorSpaceModel
 * This class doing the main part of the work.
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 *
 */
public class VectorSpaceModel {
	
	/**
	 * HashMap that saves the word frequency
	 */
	private HashMap<String, HashMap<String, Integer>> wordCount = new HashMap<>();
	
	/**
	 * HashMap that saves the calculated Data
	 */
	private HashMap<String, HashMap<String, Double>> data = new HashMap<>();
	
	/**
	 * Array with all filenames
	 */
	private String[] fileNames;
	
	/**
	 * Instance of the stopwatch
	 */
	private static Timer watch = Timer.getInstance();
	
	/**
	 * log(2)
	 */
	private final double LOG2 = Math.log(2);
	
	
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String[] names = {"italien.txt", "japan.txt", "schweiz.txt", "spanien.txt", "usa.txt"};
			//String[] names = {"doc1.txt", "doc2.txt", "doc3.txt", "doc4.txt", "doc5.txt"};
			
			watch.start("analyse");
			VectorSpaceModel model = new VectorSpaceModel(names);
			watch.stop("analyse");
			
			String query;
			do {
				System.out.println();
				System.out.print("Query: ");
				query = in.readLine();
				if (!query.isEmpty()) {
					System.out.println();
					watch.start("search");
					HashMap<String,Double> result = model.search(query);
					String time = watch.stop("search", null);
					printResult(result);
					System.out.println(time);
					
				}
			} while (!query.isEmpty());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor of the class
	 * Reads the data of all files and make the content analysis
	 * @param fileNames
	 * @throws IOException
	 */
	public VectorSpaceModel(String[] fileNames) throws IOException {
		
		this.fileNames = fileNames;
		
		// read the file contents and calculete the word count 
		for (int i = 0; i < fileNames.length; i++) {
			HashMap<String, Integer> current = FileScanner.readFile(fileNames[i]);
			Iterator<String> it = current.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (!wordCount.containsKey(key))  {
					wordCount.put(key, new HashMap<String, Integer>());
					data.put(key, new HashMap<String, Double>());
				}
				wordCount.get(key).put(fileNames[i], current.get(key));
				
			}
		}
		
		// calculate the TF/IDF values of all files and words
		for (int i = 0; i < fileNames.length; i++) {
			Iterator<String> it = wordCount.keySet().iterator();
			while (it.hasNext()) {
				String token = it.next();
				double value;
				if (wordCount.get(token).containsKey(fileNames[i])) {
					double TF = Math.log(wordCount.get(token).get(fileNames[i])) / LOG2 + 1;
					double IDF = getIDF(token);
					value = TF * IDF;
				} else value = 0;
				data.get(token).put(fileNames[i], value);
			}
		}
		
		// calculate the absolute values and normalize the data HashMap
		for (int i = 0; i < fileNames.length; i++) {
			
			// calculate the absolute value
			Iterator<String> it = wordCount.keySet().iterator();
			double absValue = 0;
			while (it.hasNext()) {
				double value = data.get(it.next()).get(fileNames[i]);
				absValue += value * value;
			}
			absValue = Math.sqrt(absValue);
			
			// normalize the data HashMap
			it = wordCount.keySet().iterator();
			while (it.hasNext()) {
				String token = it.next();
				data.get(token).put(fileNames[i], data.get(token).get(fileNames[i]) / absValue);
			}
		}
	}
	
	/**
	 * Do the search
	 * @param query
	 * @return result
	 */
	public HashMap<String, Double> search(String query) {
		return getSearchResult(getSerachVector(getWordsCount(query)));
	}
	
	/**
	 * Clean the search Query and calculate the word count
	 * @param query
	 * @return result
	 */
	private HashMap<String, Integer> getWordsCount(String query) {
		HashMap<String, Integer> searchedWords = new HashMap<>();
		FileScanner.checkLine(query, searchedWords);
		return searchedWords;
	}
	
	/**
	 * Calculates the search vector
	 * @param searchedWords
	 * @return search vector
	 */
	private HashMap<String, Double> getSerachVector(HashMap<String, Integer> searchedWords) {
		HashMap<String, Double> vector = new HashMap<>();
		
		// calculate the search vector and absolute value
		double absoluteValue = 0;
		Iterator<String> it = searchedWords.keySet().iterator();
		while (it.hasNext()) {
			String word = it.next();
			double value = 0;
			if (data.containsKey(word)) {
				double TF = Math.log(searchedWords.get(word)) / LOG2 + 1;
				double IDF = getIDF(word);
				value = TF * IDF;
			}
			vector.put(word, value);
			absoluteValue += value * value;
		}
		absoluteValue = Math.sqrt(absoluteValue);
		
		// normalize the search vector
		it = vector.keySet().iterator();
		while (it.hasNext()) {
			String word = it.next();
			vector.put(word, vector.get(word) / absoluteValue);
		}
		return vector;
	}

	/**
	 * Do the real search with the preprocessed data and the search vector
	 * @param vector
	 * @return search result
	 */
	private HashMap<String, Double> getSearchResult(HashMap<String, Double> vector) {
		HashMap<String, Double> result = new HashMap<>();
		for (int i = 0; i < fileNames.length; i++) {
			
			double value = 0;
			Iterator<String> it = vector.keySet().iterator();
			while (it.hasNext()) {
				String word = it.next();
				value += (data.containsKey(word) ? data.get(word).get(fileNames[i]) * vector.get(word) : 0);
			}
			result.put(fileNames[i], value);
			
		}
		return result;
	}
	
	/**
	 * Prints the search result formated into the system console
	 * @param result
	 */
	public static void printResult(HashMap<String, Double> result) {
		if (isGoodResult(result)) {
			System.out.println("Document \t Cos \t Rank");
			System.out.println("----------------------------");
			Map<String, Double> res = Util.sortByValue(result);
			Iterator<String> it = res.keySet().iterator();
			int i = 1;
			while (it.hasNext()) {
				String doc =  it.next();
				System.out.println(doc + " \t "+String.format("%.2f", res.get(doc)) + " \t " + (i++));
			}
		} else {
			System.out.println("Search Query is to global");
		}
	}
	
	/**
	 * Checks is the result isn't corrupt
	 * @param result
	 * @return boolean
	 */
	private static boolean isGoodResult(HashMap<String, Double> result) {
		boolean isResult = true;
		Iterator<String> it = result.keySet().iterator();
		while (it.hasNext())
			if ("NaN".equals(String.valueOf(result.get(it.next())))) 
				isResult = false;
		return isResult;
	}

	/**
	 * Helper function for the IDF calculation
	 * @param token
	 * @return IDF
	 */
	private double getIDF(String token) {
		double N = fileNames.length;
		double Nf = 0;
		Iterator<String> it = wordCount.get(token).keySet().iterator();
		while (it.hasNext()) if (wordCount.get(token).get(it.next()) > 0.0) Nf++;
		return Math.log(N/Nf) / LOG2;
	}

}
