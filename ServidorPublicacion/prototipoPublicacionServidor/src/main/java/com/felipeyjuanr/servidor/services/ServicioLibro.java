package com.felipeyjuanr.servidor.services;

import com.felipeyjuanr.servidor.model.Autor;
import com.felipeyjuanr.servidor.model.Libro;
import com.felipeyjuanr.servidor.model.LibroRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ServicioLibro {

    private static ServicioLibro servicioLibro;
    private static ServicioAutor servicioAutor;
    private ArrayList<Libro> libros;
    private ArrayList<String> autoresDisponibles;

    private ServicioLibro(){
        libros = new ArrayList<Libro>();
        autoresDisponibles = new ArrayList<>();
    }

    public static ServicioLibro getServicioLibro(){
        if(servicioLibro == null){
            servicioLibro = new ServicioLibro();
            servicioAutor = ServicioAutor.getServicioAutor();
        }

        return servicioLibro;
    }

    public void agregarAutorDisponible(String autor)
    {
        autoresDisponibles.add(autor);

        System.out.println(autoresDisponibles);
    }

    public void eliminarAutorDisponible(String autor)
    {
        autoresDisponibles.remove(autor);

        System.out.println(autoresDisponibles);
    }

    public ArrayList<String> darAutoresDisponibles()
    {
        System.out.println(autoresDisponibles);

        return autoresDisponibles;
    }

    public ArrayList<Libro> getLibros() {
        return libros;
    }

    public ArrayList<Libro> getLibrosAutor(String autor) {
        ArrayList<Libro> autores = new ArrayList<Libro>();
        for(Libro librito : libros){
            for(Autor autorcito : librito.getAutores())
            {
                if(autorcito.getNombre().equalsIgnoreCase(autor))
                {
                    autores.add(librito);
                    break;
                }
            }
        }
        return autores;
    }

    public ArrayList<Libro> getLibrosSinAutores() {
        ArrayList<Libro> librosSinAutores = new ArrayList<Libro>();
        for (Libro librito : libros) {
            if (librito.getAutores().isEmpty()) {
                librosSinAutores.add(librito);
            }
        }
        return librosSinAutores;
    }

    public boolean agregarLibro(String titulo, ArrayList<String> autores, int cantidadPaginas, LocalDateTime fechaCreacion, double precio, boolean tapaDura)
    {
        ArrayList<Autor> autoresL = servicioAutor.getAutoresNombres(autores);

        Libro libro = new Libro(titulo, autoresL, cantidadPaginas, fechaCreacion, precio, tapaDura);

        if (!verificarDuplicidadLibros(titulo))
        {
            for(Autor autor : libro.getAutores())
            {
                eliminarAutorDisponible(autor.getNombre());
            }

            libros.add(libro);

            return true;
        }
        return false;
    }

    public boolean verificarDuplicidadLibros(String titulo)
    {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return true;
            }
        }
        return false;
    }

    public Libro buscarLibroCompleto(String titulo, String autor)
    {
        return libros.stream()
                .filter(libro -> libro.getTitulo().equalsIgnoreCase(titulo))
                .filter(libro -> libro.getAutores().stream()
                        .anyMatch(a -> a.getNombre().equalsIgnoreCase(autor)))
                .findFirst()
                .orElse(null);
    }

    public Libro buscarLibroTitulo(String titulo)
    {
        for (Libro librito : libros) {
            if (librito.getTitulo().equalsIgnoreCase(titulo)) {
                return librito;
            }
        }
        return null;
    }

    public Libro buscarLibroAutor(String autor)
    {
        return libros.stream()
                .filter(libro -> libro.getAutores().stream()
                        .anyMatch(a -> a.getNombre().equalsIgnoreCase(autor)))
                .findFirst()
                .orElse(null);
    }

    public boolean eliminarLibro(String titulo) {
        boolean eliminado = false;
        Libro librito = buscarLibroTitulo(titulo);
        for(Autor autor : librito.getAutores())
        {
            agregarAutorDisponible(autor.getNombre());
        }
        eliminado = libros.remove(librito);
        return eliminado;
    }

    public void eliminarAutorLibro(String nombre)
    {
        for(Libro libro : libros)
        {
            for (Autor autor : libro.getAutores())
            {
                if(autor.getNombre().equalsIgnoreCase(nombre))
                {
                    libro.getAutores().remove(autor);
                }
            }
        }
    }

    public boolean actualizarLibro(String tituloAntiguo, String titulo, ArrayList<String> autores, int cantidadPaginas, LocalDateTime fechaCreacion, double precio, boolean tapaDura)
    {
        Libro buscado = buscarLibroTitulo(tituloAntiguo);

        if (buscado != null)
        {
            for(Autor autor : buscado.getAutores())
            {
                agregarAutorDisponible(autor.getNombre());
            }

            buscado.setTitulo(titulo);
            buscado.setAutores(servicioAutor.getAutoresNombres(autores));
            buscado.setCantidadPaginas(cantidadPaginas);
            buscado.setFechaCreacion(fechaCreacion);
            buscado.setPrecio(precio);
            buscado.setTapaDura(tapaDura);

            for(Autor autor : buscado.getAutores())
            {
                eliminarAutorDisponible(autor.getNombre());
            }

            return true;
        }

        return false;
    }

    public LibroRecord conversionLibroRecord(Libro libro)
    {
        String titulo = libro.getTitulo();
        ArrayList<String> autoresNombres = servicioAutor.obtenerNombres(libro.getAutores());
        int paginas = libro.getCantidadPaginas();
        LocalDateTime fecha = libro.getFechaCreacion();
        double precio = libro.getPrecio();
        boolean tapaDura = libro.isTapaDura();

        return new LibroRecord(titulo, autoresNombres, paginas, fecha, precio, tapaDura);
    }
}

