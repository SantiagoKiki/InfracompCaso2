package app.src;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GestorMemoria {
    private final Map<Integer, PaginaInfo> paginas;
    private int hits = 0;
    private int fallos = 0;
    private final int numMarcos;
    private final Random random = new Random();

    public GestorMemoria(int numMarcos) {
        this.numMarcos = numMarcos;
        this.paginas = new HashMap<>();
    }

    public static class PaginaInfo {
        public boolean accedida;
        public boolean modificada;

        public PaginaInfo(boolean modificada) {
            this.accedida = true;
            this.modificada = modificada;
        }

        public int getClase() {
            if (!accedida && !modificada) return 0;
            if (!accedida && modificada) return 1;
            if (accedida && !modificada) return 2;
            return 3; // accedida y modificada
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
        if (paginas.size() >= numMarcos) {
            int clase = 0;
            Integer paginaAReemplazar = null;
            
            while (clase < 4 && paginaAReemplazar == null) {
                for (Map.Entry<Integer, PaginaInfo> entry : paginas.entrySet()) {
                    if (entry.getValue().getClase() == clase) {
                        paginaAReemplazar = entry.getKey();
                        break;
                    }
                }
                clase++;
            }
            
            if (paginaAReemplazar == null) {
                paginaAReemplazar = (Integer) paginas.keySet().toArray()[
                    random.nextInt(paginas.size())];
            }
            
            paginas.remove(paginaAReemplazar);
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