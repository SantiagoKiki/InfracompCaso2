package app.src;

public class ActualizadorEstado implements Runnable {
    private final GestorMemoria gestorMemoria;
    private volatile boolean activo = true;

    public ActualizadorEstado(GestorMemoria gestorMemoria) {
        this.gestorMemoria = gestorMemoria;
    }

    @Override
    public void run() {
        while (activo) {
            // Sincronizamos sobre el objeto gestorMemoria
            synchronized (gestorMemoria) {
                gestorMemoria.resetearBitsAcceso();
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
