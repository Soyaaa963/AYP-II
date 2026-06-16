package biblioteca.interfaz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import biblioteca.aplicacion.Constante;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;

public class Interfaz {

    private static final Scanner           SC  = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Muestra el menú principal y retorna la opción elegida por el usuario.
     */
    public static int menu() {
        System.out.println("\n===== SISTEMA DE GESTIÓN DE BIBLIOTECA =====");
        System.out.println(Constante.OPCION_PRESTAR       + ". Registrar préstamo");
        System.out.println(Constante.OPCION_DEVOLVER      + ". Registrar devolución");
        System.out.println(Constante.OPCION_BUSCAR_ISBN   + ". Buscar libro por ISBN");
        System.out.println(Constante.OPCION_BUSCAR_TITULO + ". Buscar libro por título");
        System.out.println(Constante.OPCION_BUSCAR_AUTOR  + ". Buscar libro por autor");
        System.out.println(Constante.OPCION_DISPONIBLES   + ". Listar libros disponibles");
        System.out.println(Constante.OPCION_PRESTAMOS_SOCIO + ". Ver préstamos activos de un socio");
        System.out.println(Constante.OPCION_HISTORIAL     + ". Ver historial de un socio");
        System.out.println(Constante.OPCION_RANKING       + ". Libros más solicitados");
        System.out.println(Constante.OPCION_VENCIDOS      + ". Préstamos vencidos");
        System.out.println(Constante.OPCION_SALIR         + ". Salir");
        System.out.print("Ingrese una opción: ");

     // TODO: validar que la entrada sea un número dentro del rango válido
     		while (!SC.hasNextInt()) {// verifica que el numero ingresado por el usuario sea un numero y no una palabra
     			SC.next();
     			System.out.println("opcion invalida ingrese el numero:");
     		}
     		return SC.nextInt();//devuelve el numero ingresado por el usuario
     	}
    

    public static String pedirIsbn() {
        System.out.print("Ingrese ISBN: ");
        return SC.next();//devuelve el ISBN ingresado por el usuario
    }

    public static String pedirNroSocio() {
        System.out.print("Ingrese número de socio: ");
        return SC.next();//devuelve el numero de socio ingresado por el usuario
    }

    public static String pedirTitulo() {
        System.out.print("Ingrese título (o parte del título): ");
        SC.nextLine(); // limpiar buffer
        return SC.nextLine();//devuelve el tutulo ingresado por el usuario
    }

    public static String pedirAutor() {
        System.out.print("Ingrese nombre del autor: ");
        SC.nextLine(); // limpiar buffer
        return SC.nextLine();//devuelve el nombre del autor ingresado por el usuario
    }

    public static int pedirN() {
        System.out.print("Ingrese cantidad de libros a mostrar: ");
     // TODO: implementar
     		while (!SC.hasNextInt()) {// Repite hasta que el usuario ingrese un número entero válido
     			SC.next();//limpia el buffer 
     			System.out.println("valor invalido ingrese un numero:");
     		}
     		return SC.nextInt();//devuelve el numero ingresado por el usuario
     	}
    
    /**
     * Solicita una fecha al usuario en formato dd/MM/yyyy y la retorna
     * como LocalDate. Debe validar el formato antes de retornar.
     */
    public static LocalDate pedirFecha(String etiqueta) {
    	  System.out.print("Ingrese " + etiqueta + " (dd/MM/yyyy): ");//ingrese una fecha valida en formato (dd/MM/yyyy) 
     while(true) {
        // TODO: implementar y validar formato usando DateTimeFormatter FMT
        String entrada = SC.next();
        try {
        	return LocalDate.parse(entrada, FMT); 
        	
         // Si el formato es incorrecto, avisa y vuelve a pedir la fecha
      } catch (DateTimeParseException e) {
        	System.out.println("formato invalido use dd/MM/yyyy");
         }
      }
   }

    // ── Métodos de presentación de resultados ──

    public static void mostrarLibro(Libro libro) {
    	if(libro == null) {//verifico si se encuetra el libro
			System.out.println("libro no encntrado");
			return;
     }
   //si el libro se encontro muestro todas las partes del libro que la conforman
    	System.out.println("isbn:" + libro.getIsbn());
    	System.out.println("titulo:" + libro.getTitulo());
    	System.out.println("autor:" + libro.getAutor());
    	System.out.println("genero:" + libro.getGenero());
    	System.out.println("año de publicacion:" + libro.getAnioPublicacion());
    	System.out.println("copias disponibles:" + libro.getEjemplaresDisponibles());
    	System.out.println("----------------------------------------------------------------");
    }

    public static void mostrarListaLibros(Iterable<Libro> libros) {
    	boolean haylibros = false;
		for(Libro libro: libros) {
			mostrarLibro(libro); 
			haylibros = true; 
		}
		if(!haylibros) {
			System.out.println("no se encontraron libros");
		}
	}

    public static void mostrarListaPrestamos(Iterable<Prestamo> prestamos) {
    	
    	System.out.println("------------------------------------------------------------");
    		boolean hayprestamos = false;
    		for(Prestamo p: prestamos) {
    			System.out.println("socio:"+p.getSocio().getNroSocio());
    			System.out.println("libro:"+p.getLibro().getTitulo());
    			System.out.println("prestado:"+ p.getFechaPrestamo().format(FMT));
    			System.out.println("vence:"+p.getFechaVencimiento().format(FMT));
    			System.out.println("activo:"+p.isActivo());
    			System.out.println("-----------------------------------------------------");
    			hayprestamos=true;
    			
    		}
    		if(!hayprestamos) {
    			System.out.println("no hay prestamos");
    		}
    }

    public static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public static void mostrarError(String mensaje) {
        System.err.println("ERROR: " + mensaje);
    }
}
