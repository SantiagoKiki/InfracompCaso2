package app.src;

import java.util.Scanner;

import app.src.SimuladorReferencias.CrearReferencias;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuPrincipal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        
        do {
            mostrarMenu();
            opcion = obtenerOpcionValida(scanner);
            
            switch(opcion) {
                case 1:
                try {
                    System.out.print("Ingrese el tamaño de la pagina: ");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String valor1 = reader.readLine();

                    System.out.print("Ingrese el nombre del archivo BMP: ");
                    String valor2 = reader.readLine();

                    CrearReferencias cr = new SimuladorReferencias().new CrearReferencias();
                    cr.crearReferencia(valor2, Integer.parseInt(valor1));

                } catch (IOException e) {
                    System.out.println("Error leyendo de la consola");
                }
                case 2:
                    ejecutarSimulacionPaginacion(scanner);
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while(opcion != 3);
        
        scanner.close();
    }

    
    
    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Generar archivo de referencias");
        System.out.println("2. Ejecutar simulación de paginación");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    private static int obtenerOpcionValida(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor ingrese un número.");
            scanner.next(); // Limpiar entrada inválida
            System.out.print("Seleccione una opción: ");
        }
        return scanner.nextInt();
    }
    
    
    private static void ejecutarSimulacionPaginacion(Scanner scanner) {
        System.out.print("\nIngrese la ruta del archivo de referencias (ej: referencias_ref.txt): ");
        scanner.nextLine(); // Limpiar buffer
        String rutaReferencias = scanner.nextLine();
        
        // Validar extensión .txt
        if (!rutaReferencias.toLowerCase().endsWith(".txt")) {
            System.out.println("Error: El archivo debe tener extensión .txt");
            return;
        }
        
        File archivoRef = new File(rutaReferencias);
        if (!archivoRef.exists()) {
            System.out.println("Error: El archivo no existe en la ruta especificada");
            return;
        }
        
        System.out.print("Ingrese el número de marcos de página: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor ingrese un número.");
            scanner.next(); // Limpiar entrada inválida
            System.out.print("Ingrese el número de marcos de página: ");
        }
        int numMarcos = scanner.nextInt();
        
        if (numMarcos <= 0) {
            System.out.println("Error: El número de marcos debe ser mayor que cero");
            return;
        }
        
        System.out.println("\nIniciando simulación con " + numMarcos + " marcos...");
        List<String> referencias = cargarReferencias(rutaReferencias);
        
        if (referencias.isEmpty()) {
            System.out.println("No se encontraron referencias válidas en el archivo.");
            return;
        }

        new SimulacionPaginacion().iniciarSimulacion(referencias, numMarcos);
    }
    
    private static List<String> cargarReferencias(String archivo) {
        List<String> referencias = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean encabezadosLeidos = false;
            
            while ((linea = br.readLine()) != null) {
                if (!encabezadosLeidos) {
                    if (linea.startsWith("TP=") || linea.startsWith("NF=") || 
                        linea.startsWith("NC=") || linea.startsWith("NR=") || 
                        linea.startsWith("NP=")) {
                        continue;
                    }
                    encabezadosLeidos = true;
                }
                
                if (!linea.trim().isEmpty() && linea.matches("^[^,]+,\\d+,\\d+,[RW]$")) {
                    referencias.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        
        return referencias;
    }
}