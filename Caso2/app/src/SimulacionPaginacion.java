package app.src;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class SimulacionPaginacion {
    private GestorMemoria gestor;
    private final ReentrantLock lock = new ReentrantLock();

    public void iniciarSimulacion(List<String> referencias, int numMarcos) {
        this.gestor=    new GestorMemoria(numMarcos);
        ProcesadorReferencias procesador = new ProcesadorReferencias(referencias, gestor, lock, numMarcos);
        ActualizadorEstado actualizador = new ActualizadorEstado(gestor, lock);


        Thread hiloProcesador = new Thread(procesador);
        Thread hiloActualizador = new Thread(actualizador);
        
        hiloProcesador.start();
        hiloActualizador.start();
        try {
            hiloProcesador.join();
            hiloActualizador.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        gestor.mostrarEstadisticas();
    }
}