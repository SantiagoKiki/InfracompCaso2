package app.src;

import java.util.List;

public class SimulacionPaginacion {
    private GestorMemoria gestor;

    public void iniciarSimulacion(List<String> referencias, int numMarcos) {
        this.gestor = new GestorMemoria(numMarcos);
        ProcesadorReferencias procesador = new ProcesadorReferencias(referencias, gestor, numMarcos);
        ActualizadorEstado actualizador = new ActualizadorEstado(gestor);

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
