package jantarfilosofos;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class JantarDosFilosofos {
	 public static final int Filosofos = 5;
	 public static final Semaphore[] Garfos = new Semaphore[Filosofos];
	 public static final Random Random = new Random();
	 public static final String[] Nomes = {"Aristóteles", "Platão", "Sócrates", "Pithon", "Descartes"};

	    public static void main(String[] args) {
	        for (int i = 0; i < Filosofos; i++) {
	        	Garfos[i] = new Semaphore(1); 
	        }

	        Thread[] filosofos = new Thread[Filosofos];

	        for (int i = 0; i < Filosofos; i++) {
	            filosofos[i] = new Thread(new Filosofo(i));
	            filosofos[i].start();
	        }

	        try {
	            Thread.sleep(5000); 
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	      
	        for (Thread filosofo : filosofos) {
	            filosofo.interrupt();
	        }

	        
	        for (int i = 0; i < Filosofos; i++) {
	            System.out.println(Nomes[i] + " comeu " + Filosofo.vezesComeu[i] + " vezes e pensou " + Filosofo.vezesPensou[i] + " vezes.");
	        }
	    }

	    static class Filosofo implements Runnable {
	        private int id;
	        public static int[] vezesComeu = new int[Filosofos];
	        public static int[] vezesPensou = new int[Filosofos];

	        public Filosofo(int id) {
	            this.id = id;
	        }

	        public void pegarGarfos() throws InterruptedException {
	            int garfoEsquerda = id;
	            int garfoDireita = (id + 1) % Filosofos;

	            if (id % 2 == 0) {
	            	Garfos[garfoEsquerda].acquire();
	                Thread.sleep(Random.nextInt(1000)); 
	                Garfos[garfoDireita].acquire();
	            } else {
	            	Garfos[garfoDireita].acquire();
	                Thread.sleep(Random.nextInt(1000));
	                Garfos[garfoEsquerda].acquire();
	            }
	        }

	        public void soltarGarfos() {
	        	Garfos[id].release();
	        	Garfos[(id + 1) % Filosofos].release();
	        }

	        @Override
	        public void run() {
	            try {
	                while (!Thread.interrupted()) {
	                	 int acaoAleatoria = Random.nextInt(3); 
	                     if (acaoAleatoria == 0) {
	                         System.out.println(Nomes[id] + " está pensando.");
	                         Thread.sleep(Random.nextInt(500));
	                         vezesPensou[id]++;
	                     } else if (acaoAleatoria == 1) {
	             
	                         pegarGarfos();
	                         System.out.println(Nomes[id] + " está comendo.");
	                         Thread.sleep(Random.nextInt(500));
	                         vezesComeu[id]++;
	                         System.out.println(Nomes[id] + " terminou de comer.");
	                         soltarGarfos();
	                     } else {
	                         System.out.println(Nomes[id] + " está esperando");
	                     }
	                }
	            } catch (InterruptedException e) {

	            }
	        }
	    }
}
