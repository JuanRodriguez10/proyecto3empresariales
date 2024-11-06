package com.felipeyjuanr.servidor.controller;

import com.felipeyjuanr.servidor.model.Autor;
import com.felipeyjuanr.servidor.services.ServicioAutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/autores")
public class ControllerAutor {
    private ServicioAutor servicioAutor = ServicioAutor.getServicioAutor();

    @GetMapping
    public ResponseEntity<List<Autor>> getAutores() {
        List<Autor> autores = servicioAutor.getAutores();
        if (autores.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(autores);
    }

    @GetMapping("/nacionalidad/{nacionalidad}")
    public ResponseEntity<List<Autor>> getAutoresNacionalidad(@PathVariable String nacionalidad) {
        List<Autor> autores = servicioAutor.getAutoresNacionalidad(nacionalidad);
        if (autores.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(autores);
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarLibro(@RequestBody Autor autor) {

        boolean agregado = servicioAutor.agregarAutor(
                autor.getNombre(),
                autor.getEdad(),
                autor.getNacionalidad()
        );
        if (agregado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Autor agregado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo agregar el autor");
        }
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<Autor> buscarAutor(@PathVariable String nombre) {
        Autor autor = servicioAutor.buscarAutor(nombre);
        if (autor != null) {
            return ResponseEntity.ok(autor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/borrar/{nombre}")
    public ResponseEntity<String> eliminarAutor(@PathVariable String nombre) {
        boolean eliminado = servicioAutor.eliminarAutor(nombre);
        if (eliminado) {
            return ResponseEntity.ok("Autor eliminado exitosamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/actualizar/{nombreAntiguo}")
    public ResponseEntity<String> actualizarAutor(
            @PathVariable String nombreAntiguo,
            @RequestBody Autor autorActualizado) {
        boolean actualizado = servicioAutor.actualizarAutor(
                nombreAntiguo,
                autorActualizado.getNombre(),
                autorActualizado.getEdad(),
                autorActualizado.getNacionalidad()
        );
        if (actualizado) {
            return ResponseEntity.ok("Autor actualizado exitosamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
