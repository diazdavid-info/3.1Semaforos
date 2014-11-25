package david;

import java.util.concurrent.Semaphore;

public class Semaforos implements Runnable {
	
	public static final int NUMERO_SUMADO = 10000;
	public static final long NUM_VECES = 10000;
	
	private volatile long _suma = 0;
	protected Semaphore _semaphore = new Semaphore(1, true);
	
	private static long sumaN(long acumulador, int n){
		long total = acumulador;
		for (int i = 0; i < n; i++) {
			total += 1;
		}
		return total;
	}
	
	@Override
	public void run() {
		int numHebra;
		if(Thread.currentThread().getName().equals(("Hebra0"))){
			numHebra = 0;
		}else{
			numHebra = 1;
		}
		
		for (int i = 1; i <= NUM_VECES; i++) {
			entradaSeccionCritica(numHebra);
			_suma = sumaN(_suma, NUMERO_SUMADO);
			salidaSeccionCritica(numHebra);
		}
		
	}
	
	protected void entradaSeccionCritica(int numHebra) {
		_semaphore.acquireUninterruptibly();
	}
	
	protected void salidaSeccionCritica(int numHebra) {
		_semaphore.release();
	}
	
	public long getSuma() {
		return _suma;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Semaforos race = new Semaforos();
		Thread t1, t2;
		
		t1 = new Thread(race, "Hebra0");
		t2 = new Thread(race, "Hebra1");
		
		t1.start();
		t2.start();
		
		long resultadoEsperado = NUMERO_SUMADO * NUM_VECES * 2;
		
		t1.join();
		t2.join();
		
		System.out.println("El resultado final es "+race.getSuma());
		System.out.println("Esperamos "+resultadoEsperado);
		
		if(race.getSuma() != resultadoEsperado){
			System.out.println("NO COINCIDE");
		}
	}

	

}