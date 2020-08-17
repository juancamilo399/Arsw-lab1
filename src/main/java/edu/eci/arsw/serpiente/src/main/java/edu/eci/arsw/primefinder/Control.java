/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Timer;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private List<Integer> primes;

    private boolean restart;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        primes = new CopyOnWriteArrayList<>();
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA , primes);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1 , primes);
    }
    
    public static Control newControl() {
        return new Control();
    }

    public void pressEnter(){
        System.out.println("La ejecucion se detiene cada 3 segundos");
        System.out.println("Oprima Enter para continuar");
        Scanner t = new Scanner(System.in);
        String enterkey = t.nextLine();
        if (enterkey.isEmpty()) {
            restart = true;
            for (int i = 0; i < NTHREADS; i++) {
                pft[i].restart();
            }
        }
    }


    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ){
            pft[i].start();
        }
        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(int i=0 ; i<NTHREADS ; i++){
                    pft[i].halt();
                }
                restart = false;
                System.out.println("Los numeros primos que lleva hasta el momento son");
                System.out.println(primes);
                while(!restart){
                    pressEnter();
                }
            }
        });
        timer.start();
    }

}
