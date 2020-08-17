package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	
	private List<Integer> primes;
	private static Object object = new Object();
	private static boolean halt = false;
	
	public PrimeFinderThread(int a, int b , List<Integer> primes) {
		super();
		this.primes = primes;
		this.a = a;
		this.b = b;
	}

	@Override
	public void run() {
		for (int i = a; i < b; i++) {
			if (isPrime(i)) {
				primes.add(i);
			}
			synchronized (this) {
				while (halt) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

	public synchronized  void halt() {
		halt = true;
	}

	public synchronized  void restart() {
		halt = false;
		this.notifyAll();
	}
}
