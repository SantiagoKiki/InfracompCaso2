package app.src;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class GestorMemoria {
    private final LinkedHashMap<Integer, PaginaInfo> paginas;
    private int hits = 0;
    private int fallos = 0;
    private final int numMarcos;

    public GestorMemoria(int numMarcos) {
        this.numMarcos = numMarcos;
        this.paginas = new LinkedHashMap<Integer, PaginaInfo>(numMarcos, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, PaginaInfo> eldest) {
                return size() > numMarcos;
            }
        };
    }

    public static class PaginaInfo {
        public boolean accedida;
        public boolean modificada;

        public PaginaInfo(boolean modificada) {
            this.accedida = true;
            this.modificada = modificada;
        }
    }

    public boolean contienePagina(int pagina) {
        return paginas.containsKey(pagina);
    }

    public void actualizarPagina(int pagina, boolean escritura) {
        PaginaInfo info = paginas.get(pagina);
        info.accedida = true;
        if (escritura) info.modificada = true;
        hits++; 
    }

    public void manejarFalloPagina(int pagina, boolean modificada) {
        Iterator<Map.Entry<Integer, PaginaInfo>> it = paginas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, PaginaInfo> entry = it.next();
            if (!entry.getValue().accedida) {
                it.remove();
                break;
            }
            entry.getValue().accedida = false;
        }
        
        paginas.put(pagina, new PaginaInfo(modificada));
        fallos++; 
    }

    public void resetearBitsAcceso() {
        paginas.values().forEach(info -> info.accedida = false);
    }

    public void mostrarEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS ===");
        System.out.println("Hits: " + hits);
        System.out.println("Fallos: " + fallos);
        System.out.printf("Tasa hits: %.2f%%\n", (hits * 100.0 / (hits + fallos)));
        System.out.println("Páginas en memoria: " + paginas.size());
    }
}