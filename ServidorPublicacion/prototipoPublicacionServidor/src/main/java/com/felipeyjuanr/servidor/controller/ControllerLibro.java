package com.felipeyjuanr.servidor.controller;

import com.felipeyjuanr.servidor.model.Libro;
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
    public ResponseEntity<List<Libro>> getLibros() {
        List<Libro> libros = servicioLibro.getLibros();
        return (libros.isEmpty()) ?
                ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarLibro(@RequestBody Libro libro) {
        if (!libroCorrecto(libro)) {
            return ResponseEntity.badRequest().body("Datos del libro inválidos");
        }
        boolean agregado = servicioLibro.agregarLibro(
                libro.getTitulo(),
                libro.getCantidadPaginas(),
                libro.getFechaCreacion(),
                libro.getPrecio(),
                libro.isTapaDura()
        );
        return agregado ?
                ResponseEntity.status(HttpStatus.CREATED).body("Libro agregado exitosamente") :
                ResponseEntity.status(HttpStatus.CONFLICT).body("No se pudo agregar el libro");
    }

    @GetMapping("/buscar")
    public ResponseEntity<Libro> buscarLibro(
            @RequestParam String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Libro libro = servicioLibro.buscarLibro(titulo);
        return libro != null ?
                ResponseEntity.ok(libro) :
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
            @RequestBody Libro libroActualizado) {
        if (tituloAntiguo == null || tituloAntiguo.trim().isEmpty() || !libroCorrecto(libroActualizado)) {
            return ResponseEntity.badRequest().body("Datos inválidos para la actualización");
        }
        boolean actualizado = servicioLibro.actualizarLibro(
                tituloAntiguo,
                libroActualizado.getTitulo(),
                libroActualizado.getCantidadPaginas(),
                libroActualizado.getFechaCreacion(),
                libroActualizado.getPrecio(),
                libroActualizado.isTapaDura()
        );
        return actualizado ?
                ResponseEntity.ok("Libro actualizado exitosamente") :
                ResponseEntity.notFound().build();
    }

    private boolean libroCorrecto(Libro libro) {
        return libro != null &&
                libro.getTitulo() != null && !libro.getTitulo().trim().isEmpty() &&
                libro.getCantidadPaginas() > 0 &&
                libro.getFechaCreacion() != null &&
                libro.getPrecio() >= 0;
    }
}
