package app.src;

import app.src.Imagen;
import java.io.*;
import java.util.*;

public class SimuladorReferencias {

    static final int INT_SIZE = 4;
    static final int BYTE_SIZE = 1;

    public class CrearReferencias {

        public void crearReferencia(String archivoBMP, int TP) {
            Imagen imagen = new Imagen(archivoBMP);

            int[] dimensiones = imagen.getAltoAncho();

            int alto = dimensiones[0];
            int ancho = dimensiones[1];

            System.out.println("Alto: " + alto);
            System.out.println("Ancho: " + ancho);

            if (alto < 3 || ancho < 3) {
                System.out.println("La imagen debe tener al menos 3x3 pixeles.");
                return;
            }
            if (alto > 300 || ancho > 500) {
                System.out.println("La imagen debe tener máximo 300x500 pixeles.");
                return;
            }

            List<String> referencias = new ArrayList<>();
            int direccionBase = 0;

            int tamImagen = alto * ancho * 3 * BYTE_SIZE;
            int tamFiltroX = 3 * 3 * INT_SIZE;
            int tamFiltroY = 3 * 3 * INT_SIZE;
            int tamImagenOut = alto * ancho * 3 * BYTE_SIZE;

            int paginasImagen = ((tamImagen + tamFiltroX + tamFiltroY + tamImagenOut) + TP - 1) / TP;

            //asignar memoria virtual
            int dirImagenIn = direccionBase;
            int dirFiltroX = dirImagenIn + tamImagen;
            int dirFiltroY = dirFiltroX + tamFiltroX;
            int dirImagenOut = dirFiltroY + tamFiltroY;

            //acceso a memoria en applySobel
            for (int i = 1; i < alto - 1; i++) {
                for (int j = 1; j < ancho - 1; j++) {
                    for (int ki = -1; ki <= 1; ki++) {
                        for (int kj = -1; kj <= 1; kj++) {

                            int op = (dirImagenIn + ((i + ki) * ancho + (j + kj)) * 3);
                            int op2 = ((ki + 1) * 3 + (kj + 1)) * INT_SIZE;

                            referencias.add(String.format("Imagen[%d][%d].r,%d,%d,R", i + ki, j + kj, op / TP, op % TP));
                            referencias.add(String.format("Imagen[%d][%d].g,%d,%d,R", i + ki, j + kj, (op + 1) / TP, (op + 1) % TP));
                            referencias.add(String.format("Imagen[%d][%d].b,%d,%d,R", i + ki, j + kj, (op + 2) / TP, (op + 2) % TP));

                            referencias.add(String.format("SOBEL_X[%d][%d],%d,%d,R", ki + 1, kj + 1, (dirFiltroX + op2) / TP, (dirFiltroX + op2) % TP));
                            referencias.add(String.format("SOBEL_X[%d][%d],%d,%d,R", ki + 1, kj + 1, (dirFiltroX + op2) / TP, (dirFiltroX + op2) % TP));
                            referencias.add(String.format("SOBEL_X[%d][%d],%d,%d,R", ki + 1, kj + 1, (dirFiltroX + op2) / TP, (dirFiltroX + op2) % TP));
                            referencias.add(String.format("SOBEL_Y[%d][%d],%d,%d,R", ki + 1, kj + 1, (dirFiltroY + op2) / TP, (dirFiltroY + op2) % TP));
                            referencias.add(String.format("SOBEL_Y[%d][%d],%d,%d,R", ki + 1, kj + 1, (dirFiltroY + op2) / TP, (dirFiltroY + op2) % TP));
                            referencias.add(String.format("SOBEL_Y[%d][%d],%d,%d,R", ki + 1, kj + 1, (dirFiltroY + op2) / TP, (dirFiltroY + op2) % TP));
                        }
                    }
                    int op3 = (dirImagenOut + (i * ancho + j) * 3);

                    referencias.add(String.format("RTA[%d][%d].r,%d,%d,W", i, j, op3 / TP, op3 % TP));
                    referencias.add(String.format("RTA[%d][%d].g,%d,%d,W", i, j, (op3 + 1) / TP, (op3 + 1) % TP));
                    referencias.add(String.format("RTA[%d][%d].b,%d,%d,W", i, j, (op3 + 2) / TP, (op3 + 2) % TP));
                }
            }

            try (PrintWriter writer = new PrintWriter(new File("referencias.txt"))) {
                writer.println("TP=" + TP);
                writer.println("NF=" + alto);
                writer.println("NC=" + ancho);
                writer.println("NR=" + referencias.size());
                writer.println("NP=" + paginasImagen);
                for (String ref : referencias) {
                    writer.println(ref);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        System.out.println("Archivo de referencias creado con nombre referencias.txt) " );
        System.out.println("Con un número de referencias: " + referencias.size());
        }
        

    }

    
}
