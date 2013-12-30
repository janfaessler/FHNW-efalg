package informationRetrieval;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * class Time
 * Class for time tracking
 * 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 *
 */
public class Timer {
	
	private final HashMap<String, BigInteger> times;
	private static Timer instance;
	
	/**
	 * private Constructor of the Class
	 */
	private Timer() {
		times = new HashMap<>();
	}
	
	/**
	 * Returns a instance of a class
	 * @return
	 */
	public static Timer getInstance() {
		if (instance == null) instance = new Timer();
		return instance;
	}
	
	/**
	 * Starts a time calculation
	 * @param task identifier of the calculation
	 */
	public void start(String task) {
		times.put(task, BigInteger.valueOf(System.nanoTime()));
	}
	
	/**
	 * Stops a time calculation, prints the result to System.out and returns the result as a String
	 * @param task identifier of the calculation
	 * @return Output String
	 */
	public String stop(String task) {
		return stop(task, System.out);
	}
	/**
	 * Stops a time calculation, prints the result to the outputstream and returns the result as a String
	 * @param task identifier of the calculation
	 * @param out Outputstream
	 * @return Output String
	 */
	public String stop(String task, PrintStream out) {
		long time = System.nanoTime() - times.get(task).longValue();
		String result = "task '"+task+"' finished after: "+String.format("%.9f", time / 1000000000.0)+" seconds";
		if (out != null) out.println(result);
		return result;
	}

}
