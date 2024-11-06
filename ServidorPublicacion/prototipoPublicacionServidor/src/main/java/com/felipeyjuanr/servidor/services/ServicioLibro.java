package com.felipeyjuanr.servidor.services;

import com.felipeyjuanr.servidor.model.Libro;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Service
public class ServicioLibro {

    private static ServicioLibro servicioLibro;
    private ArrayList<Libro> libros;

    private ServicioLibro(){
        libros = new ArrayList<Libro>();
    }

    public static ServicioLibro getServicioLibro(){
        if(servicioLibro == null){
            servicioLibro = new ServicioLibro();
        }

        return servicioLibro;
    }

    public ArrayList<Libro> getLibros() {
        return libros;
    }

    public boolean agregarLibro(String titulo, int cantidadPaginas, LocalDateTime fechaCreacion, double precio, boolean tapaDura)
    {
        if (verificarDuplicidadLibros(titulo))
        {
            return false;
        }

        libros.add(new Libro(titulo, cantidadPaginas, fechaCreacion, precio, tapaDura));
        return true;
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

    public Libro buscarLibro(String titulo)
    {
        for (Libro librito : libros) {
            if (librito.getTitulo().equalsIgnoreCase(titulo)) {
                return librito;
            }
        }
        return null;
    }

    public boolean eliminarLibro(String titulo) {
        return libros.remove(buscarLibro(titulo));
    }

    public boolean actualizarLibro(String tituloAntiguo, String titulo, int cantidadPaginas, LocalDateTime fechaCreacion, double precio, boolean tapaDura)
    {
        Libro buscado = buscarLibro(tituloAntiguo);

        if (buscado != null)
        {

            buscado.setTitulo(titulo);
            buscado.setCantidadPaginas(cantidadPaginas);
            buscado.setFechaCreacion(fechaCreacion);
            buscado.setPrecio(precio);
            buscado.setTapaDura(tapaDura);

            return true;
        }

        return false;
    }
}

