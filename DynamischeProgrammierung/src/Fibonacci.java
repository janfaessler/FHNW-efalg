
public class Fibonacci {
	
	static long[] cache = new long[100000];

	public static void main(String[] args) {
		int n = 10;
		long start = System.currentTimeMillis();
		Fibo(n);
		System.out.println(n+": "+(System.currentTimeMillis()-start)+"ms");
		
		n= 20;
		start = System.currentTimeMillis();
		Fibo(n);
		System.out.println(n+": "+(System.currentTimeMillis()-start)+"ms");

		n= 30;
		start = System.currentTimeMillis();
		Fibo(n);
		System.out.println(n+": "+(System.currentTimeMillis()-start)+"ms");

		n= 40;
		start = System.currentTimeMillis();
		Fibo(n);
		System.out.println(n+": "+(System.currentTimeMillis()-start)+"ms");
		
		n= 6000;
		start = System.currentTimeMillis();
		Fibo2(n);
		System.out.println(n+": "+(System.currentTimeMillis()-start)+"ms");
		

	}
	
	
	private static long Fibo(int n) {
		if (n < 0) return 0;
		if (n == 1) return 1;
		return Fibo(n-1) + Fibo(n-2);
	}
	
	private static long Fibo2(int n) {
		if (n < 0) return 0;
		if (n == 1) return 1;
		if (cache[n] > 0) return cache[n];
		cache[n] = Fibo2(n-1) + Fibo2(n-2);
		return cache[n];
	}

}
