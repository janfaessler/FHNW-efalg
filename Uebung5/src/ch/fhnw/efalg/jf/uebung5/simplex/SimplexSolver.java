package ch.fhnw.efalg.jf.uebung5.simplex;

import java.util.Arrays;

import ch.fhnw.efalg.jf.uebung5.simplex.SimplexResult.Type;

public class SimplexSolver {
	
	private final boolean DEBUG = false;
	
	private double[][] table;
	private double[] targetFunction, shiftingValues;
	private boolean[] negPolicy;
	private int[] colVars, rowVars;
	private boolean needsSecondPhase, isMax;
	private int rowCount, colCount;

	public SimplexSolver(double[][] data, boolean isMaximizingProblem, boolean[] negativismPolicy) {
		isMax = isMaximizingProblem;
		negPolicy = negativismPolicy;
		shiftingValues = getShiftingValues(data);

		// shift values
        for (int iCol = 0; iCol < negPolicy.length; iCol++) {
            if (!negPolicy[iCol]) 
                for (int iRow = 0; iRow < data.length - 1; iRow++)
                	data[iRow][data[0].length - 1] += data[iRow][iCol] * -shiftingValues[iCol];
        }		
        
        needsSecondPhase = needsSecondPhase(data);
        int colStart = needsSecondPhase ? 1 : 0;
		rowCount = data.length;
		colCount = data[0].length + colStart;
		table = new double[rowCount][colCount];
		
		//copy and add helper variable
		for (int iRow = 0; iRow < rowCount; iRow++) {
			if (needsSecondPhase) table[iRow][0] = 1;
			for (int iCol = colStart; iCol < colCount; iCol++) {
				table[iRow][iCol] = data[iRow][iCol - colStart];
			}
		}
		
        // prepare table header
		colVars = new int[colCount];
		rowVars = new int[rowCount];
		for (int i = 0; i < colCount; i++) colVars[i] = i + (needsSecondPhase ? 0 : 1);
		for (int i = 0; i < rowCount; i++) rowVars[i] = i + colCount + 1;

		// if it is a min problem turn it to a max problem
		if (!isMaximizingProblem) {
			for (int iCol = 0; iCol < colCount; iCol++)
				if (colVars[iCol] != 0 || !needsSecondPhase)
					table[rowCount - 1][iCol] = -table[rowCount - 1][iCol];
		}
		
		// save target function
		targetFunction = new double[data[0].length - 1];
		for (int i = 0; i < targetFunction.length; i++) 
			targetFunction[i] = data[rowCount - 1][i];
		
		if (needsSecondPhase) table[rowCount - 1][0] = -1;
		
		debugTable("START");
	}

	private boolean needsSecondPhase(double[][] table) {
		for (int i = 0; i < table.length; i++) if (table[i][table[i].length - 1] < 0) return true;
        return false;
	}

	public SimplexResult solve() {
		double result = 0;
		String comment = null;
		double[] variables = null;
		
		try {
			// if it needs two phases, do the preparation and the first phase
			if (needsSecondPhase)  {
				debug("TwoPhaseCalculation - First phase starting");
				double tmpResult = runFirstPhase();
				debug("TwoPhaseCalculation - First phase finished (Result: "+tmpResult+")");
			}
	
			// calculate solutions
			result = runPhase();
			
			//handle infinite solutions
			for (int i = 0; i < colCount; i++) {
				if (table[rowCount - 1][i] == 0 && colVars[i] != 0)
					comment = "Infinite Solutions";
			}
			
			// correct the shifting from the constructors
			if (shiftingValues != null) 
				result = invertShifting();
	    	
			// get the solution variables
			variables = getVarSolutions();
	        
		} catch (SimplexException e) {
			// if there is a error, note it
			result = Double.NaN;
			comment = e.getMessage();
		}
		return new SimplexResult(result * (isMax && result != Double.NaN ? 1 : -1), (isMax ? Type.MAX : Type.MIN), variables, comment);
	}
	
	private double runPhase() throws SimplexException {
		int col, row, iterationCounter = 1;
		
		// loop until there is no element in the target function positive
		while ((col = getPivotColumn()) >= 0) {
			
			// choose pivot row
			row = getPivotRow(col);

			// unlimited - no solution
			if (row < 0) throw new SimplexException("Problem ist Unbeschränkt");

			// exchange row/col
			exchange(row, col);
			
			// debug output
			debugTable("IT"+(iterationCounter++));
		}

		return table[rowCount - 1][colCount - 1];
	}
	
    private int getPivotColumn() {
        for (int i = 0; i < colCount - 1; i++)
        	if (table[rowCount - 1][i] > 0) return i;
        return -1;
    }

	private int getPivotRow(int col) {
		double min = Double.MAX_VALUE;
		int row = -1;
		for (int i = 0; i < rowCount - 1; i++) {
			if (table[i][col] < 0) {
				double Q = Math.abs(table[i][colCount - 1] / table[i][col]);
				debug("Q"+i+"="+Q);
				if (Q < min) {
					row = i;
					min = Q;
				}
			}
		}
		return row;
	}

	private void exchange(int rowIndex, int colIndex) {
		int tmp = colVars[colIndex];
		colVars[colIndex] = rowVars[rowIndex];
		rowVars[rowIndex] = tmp;
		
		double pivot = table[rowIndex][colIndex];
		
		debug("EXCHANGE -> row: "+rowIndex+" col: "+colIndex+" value: "+pivot);
		
		for (int iCol = 0; iCol < colCount; iCol++)
        	table[rowIndex][iCol] = (iCol == colIndex ? -1 : table[rowIndex][iCol]) / -pivot;

		for (int iRow = 0; iRow < rowCount; iRow++) {
        	if (iRow != rowIndex) {
        		for (int iCol = 0; iCol < colCount; iCol++)
                	table[iRow][iCol] += (iCol != colIndex ? table[iRow][colIndex] * table[rowIndex][iCol] : 0);
        		table[iRow][colIndex] = table[iRow][colIndex] * pivot;
        	}                    
        }
	}
	
	private double runFirstPhase() throws SimplexException {
		int x0 = 0;
		
		//copy of target function
		double[] oldTargetFunction = new double[colCount];
		for (int i = 1; i < colCount; i++) {
			oldTargetFunction[i] = table[rowCount - 1][i];
			table[rowCount - 1][i] = 0;
		}

		// swap x0 in the row with min c
		exchange(getMinRow(), 0);
		
		// run phase one
		double tempResult = runPhase();

		// unsolvable if the result of phase 1 is not equals zero
		if (Double.compare(Math.abs(tempResult), 0) != 0) throw new SimplexException("Problem ist nicht lösbar");
		
		{ // if x0 is in a row, swap it to a column.
			int iRow = -1;
			boolean found = false;
			while (iRow++ < rowCount-1 && !found) {
				 if (rowVars[iRow] == 0) {
					 exchange(iRow, 0);
					 found = true;
				 }
			}
		}
		
		//copy in target function, if the x variable is still in a column and remove x0
		for (int iCol = 0; iCol < colCount; iCol++) {
			if (colVars[iCol] == 0) {
				x0 = iCol;
				for (int iRow = 0; iRow < rowCount; iRow++) table[iRow][iCol] = 0;
			} else {
				if (colVars[iCol] < colVars.length) 
					table[rowCount - 1][colVars[iCol]] = oldTargetFunction[colVars[iCol]];
			}
		}
		int shift = 1;
		for (int i = 0; i < oldTargetFunction.length; i++) {
			if (i != x0) {
				if (colVars[i] >= colCount) {
					int rowIndex = getRowIndex(i + shift);
					for (int iCol = 0; iCol < colCount; iCol++) 
						table[rowCount - 1][iCol] += oldTargetFunction[i + shift] * table[rowIndex][iCol];
				}
			} else shift = 0;
		}
		return tempResult;
	}

	private int getMinRow() {
		int pRow = -1;
		double min = Double.MAX_VALUE;
		for (int iRow = 0; iRow < rowCount; iRow++) {
			if (table[iRow][colCount - 1] < min) {
				pRow = iRow;
				min = table[iRow][colCount - 1];
			}
		}
		return pRow;
	}

	private int getRowIndex(int var) {
		for (int i = 0; i < rowCount; i++)
			if (rowVars[i] == var) return i;
		return -1;
	}
	
    private double[] getShiftingValues(double[][] table) {
    	double[] result = null;
    	
    	// check if there could be negative results
    	boolean nonNegative = true;
    	for (int i = 0; i < negPolicy.length; i++) nonNegative = nonNegative & negPolicy[i];
    	
    	if (!nonNegative) {
    		
    		// if there could be negative results, get the shifting maximas
    		result = new double[negPolicy.length];
	        for (int iCol = 0; iCol < negPolicy.length; iCol++) {
	            if (!negPolicy[iCol]) {
	                for (int iRow = 0; iRow < table.length - 1; iRow++) {
	                    double val = Math.abs(table[iRow][table[0].length - 1] / table[iRow][iCol]);
	                    if (!Double.isInfinite(val) && result[iCol] < val)
	                    	result[iCol] = val;
	                }
	            }
	        }
	        debug("Shifting: "+Arrays.toString(result));
	        
    	}
        return result;
    }
    
	private double invertShifting() {
		double result = 0;
		double[] vars = new double[targetFunction.length];
		for (int i = 0; i < rowCount; i++) {
			if (rowVars[i] < targetFunction.length && rowVars[i] > 0)
				vars[rowVars[i] - 1] = table[i][colCount - 1];
		}
		
		for (int i = 0; i < negPolicy.length; i++) 
		    if (!negPolicy[i]) vars[i] = vars[i] - shiftingValues[i];

		for (int i = 0; i < targetFunction.length; i++)
			result += targetFunction[i] * vars[i];
		return result;
	}
	
	private double[] getVarSolutions() {
		double[] variables;
		variables = new double[colVars.length-1-(needsSecondPhase?1:0)];
		for (int i = 0; i < colVars.length; i++)
			if (colVars[i] < colVars.length-1 && colVars[i] > 0) 
				variables[colVars[i]-1] = table[rowCount - 1][i];
		
		for (int i = 0; i < rowVars.length; i++)
			if (rowVars[i] < colVars.length && rowVars[i] > 0) 
				variables[rowVars[i]-1] = table[i][colCount - 1];
		return variables;
	}
	
	private void debug(String msg) {
		if (DEBUG) System.out.println(msg);
	}
	
    private void debugTable(String status) {
    	if (DEBUG) {
    		System.out.println("-----------------------------------------");
    		System.out.print(status.toUpperCase()+"\t:\t");
    		for (int i = 0; i < colVars.length; i++) 
    			System.out.print((colVars[i] != colCount ? (colVars[i] < colCount ? "x"+colVars[i] : "y"+(colVars[i]-colCount)) : "C")+"\t");
    		System.out.println();
    		for (int i = 0; i < table.length; i++) {
    			System.out.print((rowVars[i] != colCount+rowCount ? (rowVars[i] < colCount ? "x"+rowVars[i] : "y"+(rowVars[i]-colCount)) : "Z")+"\t|\t");
    			for (int j = 0; j < table[i].length; j++) System.out.print(String.format("%.1f", table[i][j])+"\t");
    			System.out.println();
    		}
    		System.out.println("-----------------------------------------");
    	}
    }

}