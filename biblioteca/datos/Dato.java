package biblioteca.datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import net.datastructures.ProbeHashMap;
import net.datastructures.LinkedPositionalList;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Socio;
import biblioteca.modelo.Prestamo;

/**
 * Clase utilitaria que se encarga de leer archivos de texto
 * y cargar los datos en las estructuras correspondientes.
 */
public class Dato {

    /**
     * Lee el archivo de libros y los carga en un mapa clave-valor.
     * Formato de cada línea: isbn;titulo;autor;genero;anio;ejemplares
     * Ejemplo: 978-0;Cien años de soledad;García Márquez;Novela;1967;3
     *
     * @param fileName ruta del archivo a leer
     * @return ProbeHashMap con ISBN como clave y Libro como valor
     */
    public static ProbeHashMap<String, Libro> cargarLibros(String fileName) throws FileNotFoundException {

        // Mapa vacío donde se almacenarán los libros leídos
        ProbeHashMap<String, Libro> libros = new ProbeHashMap<>();

        // Abre el archivo para leerlo línea por línea
        Scanner lectorArchivo = new Scanner(new File(fileName));

        while (lectorArchivo.hasNextLine()) {

            // Lee la línea y la divide en partes usando ";" como separador
            String[] partes_del_libro = lectorArchivo.nextLine().split(";");

            String isbn      = partes_del_libro[0]; // ej: 978-0
            String titulo    = partes_del_libro[1]; // ej: Cien años de soledad
            String autor     = partes_del_libro[2]; // ej: García Márquez
            String genero    = partes_del_libro[3]; // ej: Novela
            int anio         = Integer.parseInt(partes_del_libro[4]); // ej: 1967
            int ejemplares   = Integer.parseInt(partes_del_libro[5]); // ej: 3

            // Crea el objeto Libro con los datos leídos
            Libro libro = new Libro(isbn, titulo, autor, genero, anio, ejemplares);

            // Guarda el libro en el mapa usando el ISBN como clave única
            libros.put(isbn, libro);
        }

        lectorArchivo.close(); // Libera el recurso del archivo
        return libros;         // Devuelve el mapa completo
    }

    /**
     * Lee el archivo de socios y los carga en un mapa clave-valor.
     * Formato de cada línea: nroSocio;nombre;apellido;email;activo
     * Ejemplo: S001;Juan;Perez;juan@mail.com;true
     *
     * @param fileName ruta del archivo a leer
     * @return ProbeHashMap con nroSocio como clave y Socio como valor
     */
    public static ProbeHashMap<String, Socio> cargarSocios(String fileName) throws FileNotFoundException {

        // Mapa vacío donde se almacenarán los socios leídos
        ProbeHashMap<String, Socio> socios = new ProbeHashMap<>();

        // Abre el archivo para leerlo línea por línea
        Scanner lecturaSocios = new Scanner(new File(fileName));

        while (lecturaSocios.hasNextLine()) {

            // Lee la línea y la divide en partes usando ";" como separador
            String[] p_Socios = lecturaSocios.nextLine().split(";");

            String nroSocio = p_Socios[0]; // ej: S001
            String nombre   = p_Socios[1]; // ej: Juan
            String apellido = p_Socios[2]; // ej: Perez
            String email    = p_Socios[3]; // ej: juan@mail.com
            boolean activo  = Boolean.parseBoolean(p_Socios[4]); // ej: true

            // Crea el objeto Socio con los datos leídos
            Socio socio = new Socio(nroSocio, nombre, apellido, email, activo);

            // Guarda el socio en el mapa usando el nroSocio como clave única
            socios.put(nroSocio, socio);
        }

        lecturaSocios.close(); // Libera el recurso del archivo
        return socios;         // Devuelve el mapa completo
    }

    /**
     * Lee el archivo de préstamos y los carga en un mapa donde cada socio
     * tiene asociada una lista de sus préstamos activos.
     * Formato de cada línea: nroSocio;isbn;fechaPrestamo;fechaVencimiento
     * Ejemplo: S001;978-0;01/06/2026;15/06/2026
     *
     * @param fileName ruta del archivo a leer
     * @param socios   mapa de socios ya cargado (para obtener el objeto Socio)
     * @param libros   mapa de libros ya cargado (para obtener el objeto Libro)
     * @return ProbeHashMap con nroSocio como clave y lista de préstamos como valor
     */
    public static ProbeHashMap<String, LinkedPositionalList<Prestamo>> cargarPrestamos(
            String fileName,
            ProbeHashMap<String, Socio> socios,
            ProbeHashMap<String, Libro> libros)
            throws FileNotFoundException {

        // Mapa vacío donde cada clave (nroSocio) apunta a una lista de préstamos
        ProbeHashMap<String, LinkedPositionalList<Prestamo>> prestamos = new ProbeHashMap<>();

        // Abre el archivo para leerlo línea por línea
        Scanner lecturaPrestamos = new Scanner(new File(fileName));

        // Define el formato de fecha esperado en el archivo
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (lecturaPrestamos.hasNextLine()) {

            // Lee la línea y la divide en partes usando ";" como separador
            String[] p_prestamos = lecturaPrestamos.nextLine().split(";");

            String nroSocio          = p_prestamos[0]; // ej: S001
            String isbn              = p_prestamos[1]; // ej: 978-0
            LocalDate fechaPrestamo  = LocalDate.parse(p_prestamos[2], formato); // ej: 01/06/2026
            LocalDate fechaVencimiento = LocalDate.parse(p_prestamos[3], formato); // ej: 15/06/2026

            Libro libro = libros.get(isbn);
            Socio socio = socios.get(nroSocio);

            // Crea el objeto Prestamo con todos sus datos
            Prestamo prestamo = new Prestamo(socio, libro, fechaPrestamo, fechaVencimiento);

            // Busca si el socio ya tiene una lista de préstamos en el mapa
            LinkedPositionalList<Prestamo> lista = prestamos.get(nroSocio);

            if (lista == null) {
                // Primera vez que aparece este socio, crea una lista nueva
                lista = new LinkedPositionalList<>();
                prestamos.put(nroSocio, lista); // Asocia la lista al socio en el mapa
            }

            // Agrega el préstamo al final de la lista del socio
            lista.addLast(prestamo);
        }

        lecturaPrestamos.close(); // Libera el recurso del archivo
        return prestamos;         // Devuelve el mapa con todas las listas de préstamos
    }
}