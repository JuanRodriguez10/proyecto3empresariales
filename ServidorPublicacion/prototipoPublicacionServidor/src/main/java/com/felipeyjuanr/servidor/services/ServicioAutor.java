package com.felipeyjuanr.servidor.services;

import com.felipeyjuanr.servidor.model.Autor;

import java.util.ArrayList;

public class ServicioAutor {

    private static ServicioAutor servicioAutor;
    private static ServicioLibro servicioLibro;
    private ArrayList<Autor> autores;

    private ServicioAutor(){
        autores = new ArrayList<Autor>();
    }

    public static ServicioAutor getServicioAutor(){
        if(servicioAutor == null){
            servicioAutor = new ServicioAutor();
            servicioLibro = ServicioLibro.getServicioLibro();
        }

        return servicioAutor;
    }

    public ArrayList<Autor> getAutores() {
        return autores;
    }

    public ArrayList<Autor> getAutoresNacionalidad(String nacionalidad) {
        ArrayList<Autor> autors = new ArrayList<Autor>();

        for(Autor autor : autores){
            if(autor.getNacionalidad().equalsIgnoreCase(nacionalidad))
            {
                autors.add(autor);
            }
        }

        return autors;
    }

    public ArrayList<Autor> getAutoresNombres(ArrayList<String> nombres)
    {
        ArrayList<Autor> buscados = new ArrayList<>();

        for(String nombre : nombres)
        {
            if(buscarAutor(nombre) != null)
            {
                buscados.add(buscarAutor(nombre));
            }
        }

        return buscados;
    }

    public ArrayList<String> obtenerNombres(ArrayList<Autor> autoresX)
    {
        ArrayList<String> nombres = new ArrayList<>();

        for(Autor a : autoresX)
        {
            nombres.add(a.getNombre());
        }

        return nombres;
    }

    public boolean agregarAutor(String nombre, int edad, String nacionalidad)
    {
        Autor autor = new Autor(nombre, edad, nacionalidad);


        if (buscarAutor(autor.getNombre())==null)
        {
            autores.add(autor);
            servicioLibro.agregarAutorDisponible(autor.getNombre());
            return true;
        }
        return false;
    }

    public Autor buscarAutor(String nombre)
    {
        for (Autor autorcito : autores) {

            if(autorcito.getNombre().equalsIgnoreCase(nombre))
            {
                return autorcito;
            }
        }

        return null;
    }

    public boolean eliminarAutor(String nombre) {
        Autor autor = buscarAutor(nombre);

        if (!servicioLibro.darAutoresDisponibles().contains(nombre))
        {
            servicioLibro.eliminarAutorLibro(nombre);
        }

        servicioLibro.eliminarAutorDisponible(autor.getNombre());
        return autores.remove(autor);
    }

    public boolean actualizarAutor(String nombreAntiguo, String nombre, int edad, String nacionalidad)
    {
        Autor autor = buscarAutor(nombreAntiguo);

        if (autor != null)
        {
            boolean modificado = false;

            if(servicioLibro.darAutoresDisponibles().contains(nombreAntiguo))
            {
                servicioLibro.eliminarAutorDisponible(autor.getNombre());
                modificado = true;
            }

            autor.setNombre(nombre);
            autor.setEdad(edad);
            autor.setNacionalidad(nacionalidad);

            if(modificado)
            {
                servicioLibro.agregarAutorDisponible(autor.getNombre());
            }

            return true;
        }

        return false;
    }
}
