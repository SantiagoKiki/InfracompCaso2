package app.src;

import java.util.concurrent.locks.Lock;

public class ActualizadorEstado implements Runnable {
    private final GestorMemoria gestorMemoria;
    private final Lock lock;
    private volatile boolean activo = true;

    public ActualizadorEstado(GestorMemoria gestorMemoria, Lock lock) {
        this.gestorMemoria = gestorMemoria;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (activo) {
            lock.lock();
            try {
                gestorMemoria.resetearBitsAcceso();
            } finally {
                lock.unlock();
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void detener() {
        activo = false;
    }
}