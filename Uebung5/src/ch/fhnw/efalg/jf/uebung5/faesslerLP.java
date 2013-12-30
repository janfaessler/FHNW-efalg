package ch.fhnw.efalg.jf.uebung5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ch.fhnw.efalg.jf.uebung5.simplex.SimplexSolver;
import ch.fhnw.efalg.jf.uebung5.simplex.SimplexResult;

public class faesslerLP {

	public static void main(String[] args) {
		SimplexSolver simplexData = null;
		try {
			//simplexData = parseFile("res/JanTest2.csv");
			simplexData = parseFile(args[0]);
		} catch (IOException e) { System.err.println(e.getMessage()); }
		
		SimplexResult result = simplexData.solve();
		System.out.println(result.toString());
	}
	
	private static SimplexSolver parseFile(String filePath) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filePath));
		
		// initialize
		int colCount, rowCount;
		double[][] table;
		Double[] targetFunction;
		boolean isMax;
		boolean[] negativismPolicy;
		
		
		{ // get var count
			String[] tmp = in.readLine().split(";");
	        colCount = Integer.parseInt(tmp[0]) + 1;
	        rowCount = Integer.parseInt(tmp[1]) + 1;
		}
		
		{ // target function
			targetFunction = new Double[colCount];
			String[] tmp = in.readLine().split(";");
	        isMax = tmp[0].contains("max");
	        for (int iCol = 0; iCol < colCount; iCol++) targetFunction[iCol] = Double.parseDouble(tmp[iCol+1]);
		}
		
		{ // negativism policy
	        negativismPolicy = new boolean[colCount - 1];
	        String[] tmp = in.readLine().split(";");
	        for (int iCol = 0; iCol < colCount - 1; iCol++) {
	        	negativismPolicy[iCol] = "true".equals(tmp[iCol].trim());
	        }
		}
		
		{ // data table
			ArrayList<Double[]> tableList = new ArrayList<>();
	        for (int iRow = 0; iRow < rowCount - 1; iRow++) {
	        	String[] tmp = in.readLine().split(";");
	        	if (tmp[0].trim().equals("=")) {
	        		Double[] row1 = new Double[colCount];
	        		Double[] row2 = new Double[colCount];
	        		 for (int iCol = 0; iCol < colCount; iCol++) {
	        			 row1[iCol] = -Double.parseDouble(tmp[iCol+1]);
	        			 row2[iCol] = Double.parseDouble(tmp[iCol+1]);
		            }
	        		row1[colCount - 1] = -row1[colCount - 1];
	 				row2[colCount - 1] = -row2[colCount - 1];
		            tableList.add(row1);
		            tableList.add(row2);
	        	} else {
	        		Double[] row = new Double[colCount];
		            for (int iCol = 0; iCol < colCount; iCol++) 
		            	row[iCol] = (tmp[0].trim().contains("<") ? -1 : 1) * Double.parseDouble(tmp[iCol+1]);
		            row[colCount - 1] = -row[colCount - 1];
		            tableList.add(row);
	        	}
	        	
	        }
	        tableList.add(targetFunction);		
	        
	        // fill table
	        table = new double[tableList.size()][colCount];
	        int iRow = 0, iCol = 0;
	        for (Double[] row : tableList) {
	        	for (Double val : row) table[iRow][iCol++] = (double) val;
	        	iCol = 0;
	        	iRow++;
	        }
		}
		in.close();

		return new SimplexSolver(table, isMax, negativismPolicy);
	}
}