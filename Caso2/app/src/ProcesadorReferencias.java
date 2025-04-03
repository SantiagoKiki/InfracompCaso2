package app.src;

import java.util.List;

public class ProcesadorReferencias implements Runnable {
    private final List<String> referencias;
    private final GestorMemoria gestorMemoria;
    private volatile boolean activo = true;
    private final int numMarcos;

    public ProcesadorReferencias(List<String> referencias, GestorMemoria gestorMemoria, int numMarcos) {
        this.referencias = referencias;
        this.gestorMemoria = gestorMemoria;
        this.numMarcos = numMarcos;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < referencias.size() && activo; i++) {
                procesarReferencia(referencias.get(i));
                if (i % 10000 == 0) {
                    Thread.sleep(1);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void procesarReferencia(String referencia) {
        String[] partes = referencia.split(",");
        int pagina = Integer.parseInt(partes[1]);
        boolean escritura = partes[3].equals("W");

        // Sincronizamos sobre el objeto gestorMemoria para acceder de forma exclusiva
        synchronized (gestorMemoria) {
            if (gestorMemoria.contienePagina(pagina)) {
                gestorMemoria.actualizarPagina(pagina, escritura);
            } else {
                gestorMemoria.manejarFalloPagina(pagina, escritura);
            }
        }
    }

    public void detener() {
        activo = false;
    }
}
