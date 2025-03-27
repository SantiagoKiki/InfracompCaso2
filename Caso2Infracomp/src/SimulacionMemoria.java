import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulacionMemoria {

    static class Page {
        int id;
        long lastUsed;

        public Page(int id) {
            this.id = id;
            this.lastUsed = System.currentTimeMillis();
        }
    }

    static class Memory {
        int numFrames;
        LinkedList<Page> frames;

        public Memory(int numFrames) {
            this.numFrames = numFrames;
            frames = new LinkedList<>();
        }

        // Verificar si la página está en memoria (hit o miss)
        public boolean accessPage(int pageId) {
            for (Page page : frames) {
                if (page.id == pageId) {
                    page.lastUsed = System.currentTimeMillis(); // Actualizamos la última vez que se usó
                    return true; // Hit
                }
            }
            return false; // Miss
        }

        // Reemplazar la página menos recientemente usada
        public void replacePage(int pageId) {
            if (frames.size() >= numFrames) {
                // Reemplazar la página menos recientemente usada
                Page leastUsedPage = null;
                for (Page page : frames) {
                    if (leastUsedPage == null || page.lastUsed < leastUsedPage.lastUsed) {
                        leastUsedPage = page;
                    }
                }
                frames.remove(leastUsedPage); // Remover la página menos recientemente usada
            }
            frames.add(new Page(pageId)); // Añadir la nueva página
        }
    }

    public static void main(String[] args) throws Exception {
        // Simular lectura de imagen BMP
        BufferedImage image = ImageIO.read(new File("src\\docs\\caso2-parrotspeq.bmp"));
        int pageSize = 64; // Tamaño de página en píxeles (ajustar según sea necesario)

        // Cargar datos de la imagen y generar referencias
        int width = image.getWidth();
        int height = image.getHeight();
        int totalPages = (width * height) / pageSize;
        List<Integer> references = generatePageReferences(image);

        // Crear la memoria virtual con un número de marcos
        Memory memory = new Memory(12); // Ejemplo: 4 marcos de página
        AtomicInteger pageFaults = new AtomicInteger(0);
        AtomicInteger hits = new AtomicInteger(0);

        // Hilo para manejar las referencias de página
        Thread referenceThread = new Thread(() -> {
            for (int i = 0; i < references.size(); i++) {
                int pageId = references.get(i);
                boolean hit = memory.accessPage(pageId);
                if (hit) {
                    hits.incrementAndGet();
                } else {
                    pageFaults.incrementAndGet();
                    memory.replacePage(pageId);
                }
            }
        });

        // Iniciar el hilo de referencia
        referenceThread.start();
        referenceThread.join();

        System.out.println("Fallas de página: " + pageFaults.get());
        System.out.println("Hits: " + hits.get());
        System.out.println("Porcentaje de hits: " + (hits.get() * 100.0 / (hits.get() + pageFaults.get())) + "%");
    }

    private static List<Integer> generatePageReferences(BufferedImage image) {
        // Generar las referencias de páginas (puede ser aleatorio o secuencial)
        List<Integer> references = new ArrayList<>();
        Random random = new Random();
        int numPages = (image.getWidth() * image.getHeight()) / 64;  // Tamaño de página en este caso 64
        for (int i = 0; i < 1000; i++) {  // Generar las referencias
            references.add(random.nextInt(numPages));
        }
        return references;
    }
}
