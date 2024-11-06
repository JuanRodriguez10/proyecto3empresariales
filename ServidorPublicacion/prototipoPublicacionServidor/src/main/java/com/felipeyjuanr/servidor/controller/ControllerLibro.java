package com.felipeyjuanr.servidor.controller;

import com.felipeyjuanr.servidor.model.Libro;
import com.felipeyjuanr.servidor.model.LibroRecord;
import com.felipeyjuanr.servidor.services.ServicioLibro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/libros")
public class ControllerLibro {

    private ServicioLibro servicioLibro = ServicioLibro.getServicioLibro();

    @GetMapping
    public ResponseEntity<List<LibroRecord>> getLibros() {
        List<Libro> libros = servicioLibro.getLibros();
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<LibroRecord> libroRecords = libros.stream()
                .map(servicioLibro::conversionLibroRecord)
                .toList();
        return ResponseEntity.ok(libroRecords);
    }

    @GetMapping("/autoresDisponibles")
    public ResponseEntity<ArrayList<String>> darAutoresDisponibles()
    {
        ArrayList<String> autoresDisponibles = servicioLibro.darAutoresDisponibles();

        return autoresDisponibles.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(autoresDisponibles);
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LibroRecord>> getLibrosAutor(@PathVariable String autor) {
        List<Libro> libros;
        if (autor == null || autor.trim().isEmpty()) {
            libros =  servicioLibro.getLibrosSinAutores();
        }
        else {
            libros = servicioLibro.getLibrosAutor(autor);
        }

        System.out.println(libros);

        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<LibroRecord> libroRecords = libros.stream()
                .map(servicioLibro::conversionLibroRecord)
                .toList();
        return ResponseEntity.ok(libroRecords);
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarLibro(@RequestBody LibroRecord libro) {
        if (libro == null || !libroRecordCorrecto(libro)) {
            return ResponseEntity.badRequest().body("Datos del libro inválidos");
        }
        boolean agregado = servicioLibro.agregarLibro(
                libro.titulo(),
                libro.autores(),
                libro.cantidadPaginas(),
                libro.fechaCreacion(),
                libro.precio(),
                libro.tapaDura()
        );
        return agregado ?
                ResponseEntity.status(HttpStatus.CREATED).body("Libro agregado exitosamente") :
                ResponseEntity.status(HttpStatus.CONFLICT).body("No se pudo agregar el libro");
    }

    @GetMapping("/buscarTitulo/{titulo}")
    public ResponseEntity<LibroRecord> buscarLibroPorTitulo(@PathVariable String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Libro libro = servicioLibro.buscarLibroTitulo(titulo);
        return libro != null ?
                ResponseEntity.ok(servicioLibro.conversionLibroRecord(libro)) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/buscarAutor/{autor}")
    public ResponseEntity<LibroRecord> buscarLibroPorAutor(@PathVariable String autor) {
        Libro libro = servicioLibro.buscarLibroAutor(autor);
        return libro != null ?
                ResponseEntity.ok(servicioLibro.conversionLibroRecord(libro)) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<LibroRecord> buscarLibro(
            @RequestParam String titulo,
            @RequestParam String autor) {
        if (titulo == null || autor == null || titulo.trim().isEmpty() || autor.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Libro libro = servicioLibro.buscarLibroCompleto(titulo, autor);
        return libro != null ?
                ResponseEntity.ok(servicioLibro.conversionLibroRecord(libro)) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/borrar/{titulo}")
    public ResponseEntity<String> eliminarLibro(@PathVariable String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Título inválido");
        }
        boolean eliminado = servicioLibro.eliminarLibro(titulo);
        return eliminado ?
                ResponseEntity.ok("Libro eliminado exitosamente") :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/actualizar/{tituloAntiguo}")
    public ResponseEntity<String> actualizarLibro(
            @PathVariable String tituloAntiguo,
            @RequestBody LibroRecord libroActualizado) {
        if (tituloAntiguo == null || tituloAntiguo.trim().isEmpty() || !libroRecordCorrecto(libroActualizado)) {
            return ResponseEntity.badRequest().body("Datos inválidos para la actualización");
        }
        boolean actualizado = servicioLibro.actualizarLibro(
                tituloAntiguo,
                libroActualizado.titulo(),
                libroActualizado.autores(),
                libroActualizado.cantidadPaginas(),
                libroActualizado.fechaCreacion(),
                libroActualizado.precio(),
                libroActualizado.tapaDura()
        );
        return actualizado ?
                ResponseEntity.ok("Libro actualizado exitosamente") :
                ResponseEntity.notFound().build();
    }

    private boolean libroRecordCorrecto(LibroRecord libro) {
        return libro != null &&
                libro.titulo() != null && !libro.titulo().trim().isEmpty() &&
                libro.autores() != null && !libro.autores().isEmpty() &&
                libro.cantidadPaginas() > 0 &&
                libro.fechaCreacion() != null &&
                libro.precio() >= 0;
    }
}
