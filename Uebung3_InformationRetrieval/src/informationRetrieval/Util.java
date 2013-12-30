package informationRetrieval;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class Util
 * utility class with some 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 *
 */
public class Util {
	
	/**
	 * Cleans a String
	 * @param str
	 * @return
	 */
	public static String cleanString(String str) {
		return deAccent(str).toLowerCase()
							.replaceAll("\\W", " ")
				   			.replaceAll("ß", "ss");
	}
	
	/**
	 * removes all accents from a string
	 * @param str
	 * @return
	 */
	private static String deAccent(String str) {
	    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(nfdNormalizedString).replaceAll("");
	}
	
	/**
	 * Returns a sorted map with magic code
	 * http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
	 * 
	 * @param map
	 * @return sortet map
	 */
    public static <K, V extends Comparable<? super V>> Map<K, V>  sortByValue( Map<K, V> map) {
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
	            return (o1.getValue()).compareTo( o2.getValue() ) * -1;
	        }
	    });
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list) result.put( entry.getKey(), entry.getValue() );
	   
	    return result;
    }
}
