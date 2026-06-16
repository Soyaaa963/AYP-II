package biblioteca.logica;

import java.time.LocalDate;

import net.datastructures.ProbeHashMap;
import net.datastructures.LinkedPositionalList;
import net.datastructures.LinkedQueue;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Socio;
import biblioteca.modelo.Prestamo;

public class Logica {

	// Mapa que almacena todos los libros del catálogo, indexados por ISB
    private ProbeHashMap<String, Libro> catalogo;
    
    // Mapa que almacena todos los socios registrados, indexados por nroSocio
    private ProbeHashMap<String, Socio> socios;
    
    // TODO: definir las estructuras adicionales que necesite
    
    // Pensar: ¿dónde guardar los préstamos activos? (ya vienen cargados desde  datos)
    private ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamosActivos;

    // Pensar: ¿cómo modelar la lista de espera por libro?
    // Pensar: ¿dónde guardar el historial de préstamos por socio?

    
    public Logica(ProbeHashMap<String, Libro> catalogo,
                  ProbeHashMap<String, Socio> socios,
                  ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamosActivos) {
        this.catalogo = catalogo;
        this.socios   = socios;
        this.prestamosActivos = prestamosActivos;
        // TODO: inicializar las estructuras internas a partir de los datos recibidos
    }

    // ── INCREMENTO 1 ──────────────────────────────────────────────

    /**
     * Registra el préstamo de un libro a un socio.
     * La fecha de préstamo es la fecha actual y el vencimiento se calcula
     * automáticamente (14 días).
     * Condiciones: el socio debe estar activo y debe haber ejemplares disponibles.
     * @return true si el préstamo se realizó, false en caso contrario
     */
    public boolean prestar(String nroSocio, String isbn) {
    	
    	 // Busco al socio en el mapa; si no existe, no se puede prestar
    	 Socio socio = socios.get(nroSocio);  
    	 if (socio == null) {
    		 return false;// el socio no existe 
    	 }
    	 
     // Si el socio existe pero no está activo, no se puede prestar
    	 if (!socio.isActivo()) {
    		 return false;
    	 }
    	 
    	//Busco el libro en el catálogo usando como clave el ISBN
    	 Libro libro = catalogo.get(isbn); 
    	 if (libro == null) {
    		 return false;//el libro no existe en catalogo
    	 }
    	 
    	 // Si no hay ejemplares disponibles, no se puede prestar
    	 if (libro.getEjemplaresDisponibles() <= 0) {
    		 return false; 
    	 }
    	 
    	 LocalDate fechaPrestamo = LocalDate.now();//fecha actual
    	 
    	// La fecha de vencimiento es 14 días después del préstamo
    	 LocalDate fechaVencimiento = fechaPrestamo.plusDays(14); //fechaActual + 14
    	 
    	 //crea al objeto prestamo
    	 Prestamo prestamo = new Prestamo(socio, libro, fechaPrestamo, fechaVencimiento);
    	 
    	 //busco si el socio ya tiene una lista de prestamos activos en el mapa
    	 LinkedPositionalList<Prestamo> listaPrestamos = prestamosActivos.get(nroSocio);
    	 
    	 if (listaPrestamos == null) {
    		 
    		 listaPrestamos = new LinkedPositionalList<>();//si no tiene una lista de prestamos creo una 
    		 
    		 prestamosActivos.put(nroSocio, listaPrestamos);
    	 }
    	 
    	 	listaPrestamos.addLast(prestamo);
    	 
    	 // Como el libro fue prestado, resto 1 ejemplar disponible
    	 libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
    	 
    	 	return true;//prestamo realizado
     }

    /**
     * Registra la devolución de un libro.
     * Actualiza el estado del préstamo y la disponibilidad del libro.
     * @return true si la devolución se realizó, false en caso contrario
     */
    public boolean devolver(String nroSocio, String isbn) {
    	
    	// Obtengo la lista de préstamos del socio
    	LinkedPositionalList<Prestamo> listaPrestamos = prestamosActivos.get(nroSocio);
    	
    	//Si el socio no tiene préstamos registrados, no hay nada que devolver
    	 if (listaPrestamos == null) {
    		 return false;
    	 }
    	
    	 // Recorro la lista buscando un préstamo activo con el ISBN indicado
    	 for (Prestamo p : listaPrestamos) { 
    		 
    		 if (p.getLibro().getIsbn().equals(isbn) && p.isActivo()) {
    			 
    			// Marco el préstamo como inactivo (devuelto)
    			 	p.setActivo(false);
    			 
    // Devuelvo el ejemplar al catálogo: aumento los disponibles en 1		 	
    			 Libro libro = p.getLibro();
    			 libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
    			 
    			 return true ;//devolucion echa
    		 }
    	 }
    	 //si no encontre un prestamo activo con ese ISBN falla 
        return false;
    }

    /**
     * Busca un libro por su ISBN.
     * @return el Libro encontrado, o null si no existe
     */
    public Libro buscarPorIsbn(String isbn) {
        return catalogo.get(isbn); //buscamos el ISBN(clave) del libro
    }

    /**
     * Busca libros cuyo título contenga la cadena indicada (sin distinguir mayúsculas).
     */
    public LinkedPositionalList<Libro> buscarPorTitulo(String titulo) {
    	
    	LinkedPositionalList<Libro> resultados = new LinkedPositionalList<Libro>();
    	
    	//recorro los libros del catalogo
    	for (Libro libro : catalogo.values()) {
    		
    		// Comparo en minúsculas para ignorar diferencias de mayúsculas
    		if (libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
    			resultados .addLast(libro);	
    		}
    	}
        return resultados ;
    }

    /**
     * Busca libros de un autor dado (sin distinguir mayúsculas).
     */
    public LinkedPositionalList<Libro> buscarPorAutor(String autor) {
    	
    	LinkedPositionalList<Libro> resultados  = new LinkedPositionalList<Libro>();
    	
    	for (Libro libro : catalogo.values()) {
    		
    		  // Comparo en minúsculas para ignorar diferencias de mayúsculas
    		if (libro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
    			resultados .addLast(libro);
    		}
    	}
        return resultados ;
    }

    /**
     * Retorna todos los libros con al menos un ejemplar disponible.
     */
    public LinkedPositionalList<Libro> listarDisponibles() {
       
    	LinkedPositionalList<Libro> disponibles = new LinkedPositionalList<Libro>();
    	
    	for (Libro libro : catalogo.values()) {
            if (libro.getEjemplaresDisponibles() > 0) {
                disponibles.addLast(libro);
            }
        }
        return disponibles;
    }

    /**
     * Retorna los préstamos activos de un socio.
     */
    public LinkedPositionalList<Prestamo> prestamosActivosDeSocio(String nroSocio) {
    	 
    	// Lista donde se acumulan solo los préstamos activos
        LinkedPositionalList<Prestamo> activos = new LinkedPositionalList<>();

     // Obtengo la lista completa de préstamos del socio
        LinkedPositionalList<Prestamo> listaSocio = prestamosActivos.get(nroSocio);

        // Si el socio no tiene préstamos registrados,retorno la lista vacia 
        if (listaSocio == null) {
            return activos;
        }

        for (Prestamo p : listaSocio) {

            if (p.isActivo()) {
                activos.addLast(p);
            }
        }

        return activos;
    }
       
    // ── INCREMENTO 2 ──────────────────────────────────────────────

    /**
     * Agrega un socio a la cola de espera de un libro.
     * Se invoca cuando no hay ejemplares disponibles al momento del pedido.
     */
    public void agregarEspera(String nroSocio, String isbn) {
        // TODO: implementar
    }

    /**
     * Al devolver un libro, si hay socios en espera, asigna el ejemplar
     * automáticamente al primero en la cola y lo notifica.
     */
    public void asignarSiguienteEnEspera(String isbn) {
        // TODO: implementar
    }

    /**
     * Retorna el historial completo de préstamos de un socio
     * (activos e históricos), en orden cronológico.
     */
    public LinkedPositionalList<Prestamo> historialDeSocio(String nroSocio) {
        // TODO: implementar
        return null;
    }

    /**
     * Retorna los N libros más solicitados (préstamos activos + históricos).
     * @param n cantidad de libros a retornar
     */
    public LinkedPositionalList<Libro> librosMasSolicitados(int n) {
        // TODO: implementar
        return null;
    }

    /**
     * Retorna todos los préstamos cuya fecha de vencimiento expiró
     * y que aún no fueron devueltos.
     * @param hoy fecha actual
     */
    public LinkedPositionalList<Prestamo> prestamosVencidos(LocalDate hoy) {
        // TODO: implementar
        return null;
    }
}
