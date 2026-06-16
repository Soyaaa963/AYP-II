package biblioteca.aplicacion;

import java.io.IOException;
import java.time.LocalDate;

import net.datastructures.LinkedPositionalList;
import net.datastructures.ProbeHashMap;
import biblioteca.datos.CargarParametros;
import biblioteca.datos.Dato;
import biblioteca.interfaz.Interfaz;
import biblioteca.logica.Logica;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Socio;

public class Aplicacion {

    public static void main(String[] args) {

        // 1. Cargar parámetros de configuración
        try {
            CargarParametros.parametros();
        } catch (IOException e) {
            System.err.println("Error al cargar config.properties");
            System.exit(-1);
        }

        // 2. Cargar datos desde archivos
        ProbeHashMap<String, Libro>   catalogo  = null;
        ProbeHashMap<String, Socio>   socios    = null;
        ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos = null;

        try {
            catalogo  = Dato.cargarLibros(CargarParametros.getArchivoLibros());
            socios    = Dato.cargarSocios(CargarParametros.getArchivoSocios());
            prestamos = Dato.cargarPrestamos(CargarParametros.getArchivoPrestamos(),
                                             socios, catalogo);
        } catch (Exception e) {
            System.err.println("Error al cargar archivos de datos: " + e.getMessage());
            System.exit(-1);
        }

        // 3. Inicializar capa lógica
        Logica logica = new Logica(catalogo, socios, prestamos);

        // 4. Ciclo principal de la aplicación
        int opcion;
        do {
            opcion = Interfaz.menu();

            switch (opcion) {
                case Constante.OPCION_PRESTAR:
                    // TODO: pedir datos al usuario y llamar a logica.prestar(...)
                	String nroSocioPrestar = Interfaz.pedirNroSocio(); 
                	String isbnPrestar = Interfaz.pedirIsbn(); 
                	if(logica.prestar(nroSocioPrestar,isbnPrestar)) {
                		Interfaz.mostrarMensaje("prestamo registrado correctamente");
                	}else {
                		Interfaz.mostrarError("no se pudo registrar el prestamo!");                	}
                	
                    break;

                case Constante.OPCION_DEVOLVER:
                    // TODO: pedir datos al usuario y llamar a logica.devolver(...)
                	String nroSocioDevolver = Interfaz.pedirNroSocio();
                	String isbnDevolver = Interfaz.pedirIsbn();
                	
                	if(logica.devolver(nroSocioDevolver, isbnDevolver )) {
                		Interfaz.mostrarMensaje("debolucion registrada correctamente");
                	}else {
                		Interfaz.mostrarError("no se pudo completar la debolucion");
                	}
                    break;

                case Constante.OPCION_BUSCAR_ISBN:
                    // TODO: pedir ISBN y mostrar resultado de logica.buscarPorIsbn(...)
                	String isbnBuscar = Interfaz.pedirIsbn(); 
                	Libro libroEncontrado = logica.buscarPorIsbn(isbnBuscar);
                	Interfaz.mostrarLibro(libroEncontrado);
                    break;

                case Constante.OPCION_BUSCAR_TITULO:
                    // TODO: pedir título y mostrar resultados de logica.buscarPorTitulo(...)
                	String tituloBuscar = Interfaz.pedirTitulo();
                	Interfaz.mostrarListaLibros(logica.buscarPorTitulo(tituloBuscar)); 
                    break;

                case Constante.OPCION_BUSCAR_AUTOR:
                    // TODO: pedir autor y mostrar resultados de logica.buscarPorAutor(...)
                	String buscarAutor = Interfaz.pedirAutor();
                	Interfaz.mostrarListaLibros(logica.buscarPorAutor(buscarAutor));
                    break;

                case Constante.OPCION_DISPONIBLES:
                    // TODO: mostrar resultado de logica.listarDisponibles()
                	Interfaz.mostrarListaLibros(logica.listarDisponibles());
                    break;

                case Constante.OPCION_PRESTAMOS_SOCIO:
                    // TODO: pedir nroSocio y mostrar logica.prestamosActivosDeSocio(...)
                	String nrosocio = Interfaz.pedirNroSocio();
                	Interfaz.mostrarListaPrestamos(logica.prestamosActivosDeSocio(nrosocio));
                    break;

                case Constante.OPCION_HISTORIAL:
                    // TODO: pedir nroSocio y mostrar logica.historialDeSocio(...)
                    break;

                case Constante.OPCION_RANKING:
                    // TODO: pedir N y mostrar logica.librosMasSolicitados(N)
                    break;

                case Constante.OPCION_VENCIDOS:
                    // TODO: pedir fecha con Interfaz.pedirFecha(...) y mostrar
                    //       logica.prestamosVencidos(LocalDate)
                    break;

                case Constante.OPCION_SALIR:
                    Interfaz.mostrarMensaje("Hasta luego.");
                    break;

                default:
                    Interfaz.mostrarError("Opción no válida.");
            }

        } while (opcion != Constante.OPCION_SALIR);
    }
}
