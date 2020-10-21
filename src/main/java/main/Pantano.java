package main;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Pantano implements Runnable {
    private Orc[] orcs;
    // Libera o acesso dos Orcs à Presa quando gerada
    private Semaphore esperandoPresa;
    // Usado para selecionar qual Orc irá morder a presa
    private Semaphore[] sincronizacaoOrcs;
    private Random orcSelector;
    // Condição de término do programa
    private int presasGeradas, totalPresas;
    private boolean done = false;

    private Presa presa;

    public Pantano(int nOrcs, int totalPresas) {
        orcs = new Orc[nOrcs];
        sincronizacaoOrcs = new Semaphore[nOrcs];
        for (int c = 0; c < nOrcs; c++) {
            orcs[c] = new Orc(c + 1);
            sincronizacaoOrcs[c] = new Semaphore(0);
        }
        orcSelector = new Random(123);
        this.totalPresas = totalPresas;
        esperandoPresa = new Semaphore(0);
    }

    @Override
    public void run() {
        try {
            for (Orc o : orcs) {
                o.setPantano(this);
                o.start();
            }
            while (presasGeradas < totalPresas) {
                Thread.sleep(1000);
                System.out.printf("Gerando presa #%d\n", presasGeradas + 1);
                gerarPresa();
                // Espera que os Orcs estejam prontos para morder
                //esperarTodosOrcs();
                int selecionado = selecionarOrc();
                // Desbloqueia para o orc selecionado
                sincronizacaoOrcs[selecionado].release();
                presa.esperarMordida();
                // Desbloqueia para os demais orcs
                for (int c = 0; c < orcs.length; c++) {
                    if (c == selecionado)
                        continue;
                    sincronizacaoOrcs[c].release();
                }
            }
            gerarPresa();
            done = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void gerarPresa() {
        esperandoPresa.drainPermits();
        if (presasGeradas < totalPresas) {
            presa = new Presa(presasGeradas + 1);
            presasGeradas++;
        } else {
            presa = null;
        }
        esperandoPresa.release(orcs.length);
    }

    public Presa esperarPresa() throws InterruptedException {
        if (!done) {
            esperandoPresa.acquire();
        }
        return this.presa;
    }

    public boolean isDone() {
        return this.done;
    }

    public void sincronizarOrc(int id) throws InterruptedException {
        sincronizacaoOrcs[id - 1].acquire();
    }

    private int selecionarOrc() {
        return orcSelector.nextInt(orcs.length);
    }
}
