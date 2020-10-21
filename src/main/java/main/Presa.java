package main;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Presa {
    private int id;
    private Lock lockAtordoar;
    private Lock lockMorder;
    private Semaphore esperaMordida;
    private boolean atordoada = false;
    private boolean mordida = false;
    public Presa(int id) {
        this.id = id;
        lockAtordoar = new ReentrantLock();
        lockMorder = new ReentrantLock();
        esperaMordida = new Semaphore(0);
    }

    public void tryAtordoar(int id) {
        if (atordoada ||
            !lockAtordoar.tryLock() || atordoada) {
            return;
        }
        System.out.println("Orc " + id + " gritou");
        atordoada = true;
        lockAtordoar.unlock();
    }

    public void tryMorder(int id) {
        if (mordida ||
            !lockMorder.tryLock() || mordida) {
            return;
        }
        System.out.println("Orc " + id + " mordeu");
        mordida = true;
        lockMorder.unlock();
        esperaMordida.release();
    }

    public void esperarMordida() throws InterruptedException {
        esperaMordida.acquire();
    }
}
