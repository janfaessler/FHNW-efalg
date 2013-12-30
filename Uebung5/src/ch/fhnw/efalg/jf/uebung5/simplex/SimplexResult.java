package ch.fhnw.efalg.jf.uebung5.simplex;

public class SimplexResult {
	public enum Type { MAX, MIN };
	
	public final double value;
	public final Type type;
	public final double[] variables;
	public final String comment;
	
	public SimplexResult(double val, Type t, double[] vars, String c) {
		value = val;
		type = t;
		variables = vars;
		comment = c;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SimplexResult\n");
		sb.append("\t"+(type == Type.MAX ? "maxVal: " : "minVal: ")+value+"\n");
		if (variables != null) {
			for (int i = 0; i < variables.length; i++)
				sb.append("\tx"+(i+1)+": "+variables[i]+"\n");
		}
		if (comment != null) sb.append("\t"+comment);
		return sb.toString();
	}
}
