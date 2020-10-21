package main;

public class Orc extends Thread {
    boolean done = false;
    private Presa presaAlvo;
    Pantano pantano;
    private int id;
    public Orc(int id) {
        this.id = id;
    }

    public void setPantano(Pantano p) {
        this.pantano = p;
    }

    @Override
    public void run() {
        try {
            loop();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Comportamento do Orc
    private void loop() throws InterruptedException {
        if (done) {
            return;
        }
        while (!pantano.isDone()) {
            presaAlvo = pantano.esperarPresa();
            if (presaAlvo == null) {
                continue;
            }
            gritar();
            // Espera ser escolhido para morder
            pantano.sincronizarOrc(id);
            morder();
        }
        done = true;
    }

    private void gritar() {
        presaAlvo.tryAtordoar(id);
    }

    private void morder() {
        presaAlvo.tryMorder(id);
    }
}
